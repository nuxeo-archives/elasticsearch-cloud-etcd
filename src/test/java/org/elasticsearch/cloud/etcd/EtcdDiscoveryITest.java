/*
 * (C) Copyright 2014 Nuxeo SA (http://nuxeo.com/) and contributors.
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
 *     dmetzler
 */
package org.elasticsearch.cloud.etcd;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;

import org.elasticsearch.cloud.etcd.AbstractEtcdTest.EtcdTest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugin.cloud.etcd.EtcdModule;
import org.elasticsearch.plugins.PluginsService;
import org.elasticsearch.test.ElasticsearchIntegrationTest.ClusterScope;
import org.elasticsearch.test.ElasticsearchIntegrationTest.Scope;
import org.junit.Test;

@EtcdTest
@ClusterScope(scope = Scope.TEST, numDataNodes = 0, numClientNodes = 0, transportClientRatio = 0.0)
public class EtcdDiscoveryITest extends AbstractEtcdTest {

    @Test
    public void startTest() throws Exception {
        Settings nodeSettings = settingsBuilder() //
        .put("plugins." + PluginsService.LOAD_PLUGIN_FROM_CLASSPATH, true) //
        .put("cloud.enabled", true) //
        .put("discovery.type", "etcd") //
        .put(EtcdModule.ETCD_SERVICE_TYPE_KEY, TestEtcdService.class)//
        .build();
        internalCluster().startNode(nodeSettings);
    }
}
