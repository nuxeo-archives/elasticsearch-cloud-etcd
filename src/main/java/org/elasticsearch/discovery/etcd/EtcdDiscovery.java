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

import org.elasticsearch.Version;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.node.DiscoveryNodeService;
import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.discovery.DiscoverySettings;
import org.elasticsearch.discovery.zen.ZenDiscovery;
import org.elasticsearch.discovery.zen.ping.ZenPing;
import org.elasticsearch.discovery.zen.ping.ZenPingService;
import org.elasticsearch.discovery.zen.ping.unicast.UnicastZenPing;
import org.elasticsearch.node.settings.NodeSettingsService;
import org.elasticsearch.plugin.cloud.etcd.EtcdServiceImpl;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

public class EtcdDiscovery extends ZenDiscovery {

    @Inject
    public EtcdDiscovery(Settings settings, ClusterName clusterName,
            ThreadPool threadPool, TransportService transportService,
            ClusterService clusterService,
            NodeSettingsService nodeSettingsService,
            DiscoveryNodeService discoveryNodeService,
            ZenPingService pingService, Version version,
            DiscoverySettings discoverySettings, EtcdServiceImpl etcdService) {
        super(settings, clusterName, threadPool, transportService,
                clusterService, nodeSettingsService, discoveryNodeService,
                pingService, version, discoverySettings);

        if (settings.getAsBoolean("cloud.enabled", true)) {
            ImmutableList<? extends ZenPing> zenPings = pingService.zenPings();
            UnicastZenPing unicastZenPing = null;
            for (ZenPing zenPing : zenPings) {
                if (zenPing instanceof UnicastZenPing) {
                    unicastZenPing = (UnicastZenPing) zenPing;
                    break;
                }
            }

            if (unicastZenPing != null) {
                // update the unicast zen ping to add cloud hosts provider
                // and, while we are at it, use only it and not the multicast
                // for example
                unicastZenPing.addHostsProvider(new EtcdUnicastHostsProvider(
                        settings, transportService, etcdService));
                pingService.zenPings(ImmutableList.of(unicastZenPing));
            } else {
                logger.warn("failed to apply etcd unicast discovery, no unicast ping found");
            }
        }
    }
}
