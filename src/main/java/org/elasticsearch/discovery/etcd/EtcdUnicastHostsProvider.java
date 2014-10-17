/*
 * (C) Copyright 2014 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     * Damien Metzler <dmetzler@nuxeo.com>
 */
package org.elasticsearch.discovery.etcd;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.Version;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.discovery.zen.ping.unicast.UnicastHostsProvider;
import org.elasticsearch.discovery.zen.ping.unicast.UnicastZenPing;
import org.elasticsearch.plugin.cloud.etcd.EtcdServiceImpl;
import org.elasticsearch.plugin.cloud.etcd.Location;
import org.elasticsearch.transport.TransportService;

public class EtcdUnicastHostsProvider extends AbstractComponent implements
        UnicastHostsProvider {

    private TransportService transportService;

    private EtcdServiceImpl etcdService;

    @Inject
    public EtcdUnicastHostsProvider(Settings settings,
            TransportService transportService, EtcdServiceImpl etcdService) {
        super(settings);
        this.transportService = transportService;
        this.etcdService = etcdService;

    }

    @Override
    public List<DiscoveryNode> buildDynamicNodes() {
        List<DiscoveryNode> nodes = new ArrayList<>();
        try {
            for (Location location : etcdService.transports()) {
                TransportAddress[] addresses = transportService.addressesFromString(location.toAdress());
                for (int i = 0; (i < addresses.length && i < UnicastZenPing.LIMIT_PORTS_COUNT); i++) {
                    nodes.add(new DiscoveryNode("#cloud-" + location.id + "-" + i, addresses[i],
                            Version.CURRENT));
                }
            }
            return nodes;
        } catch (Exception e) {
            return nodes;
        }
    }
}
