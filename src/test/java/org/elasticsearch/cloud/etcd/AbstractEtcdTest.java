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

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.env.FailedToResolveConfigException;
import org.elasticsearch.plugin.cloud.etcd.EtcdModule;
import org.elasticsearch.plugins.PluginsService;
import org.elasticsearch.test.ElasticsearchIntegrationTest;

import com.carrotsearch.randomizedtesting.annotations.TestGroup;

public class AbstractEtcdTest extends ElasticsearchIntegrationTest {

    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @TestGroup(enabled = false, sysProperty = SYSPROP_ETCD)
    public @interface EtcdTest {
    }

    /**
     */
    public static final String SYSPROP_ETCD = "test.etcd";

    @Override
    protected Settings nodeSettings(int nodeOrdinal) {
                ImmutableSettings.Builder settings = ImmutableSettings.builder()
                .put(super.nodeSettings(nodeOrdinal))
                .put("plugins." + PluginsService.LOAD_PLUGIN_FROM_CLASSPATH, true)
                .put(EtcdModule.ETCD_SERVICE_TYPE_KEY, TestEtcdService.class);

        Environment environment = new Environment();

        // if explicit, just load it and don't load from env
        try {
            if (Strings.hasText(System.getProperty("tests.config"))) {
                settings.loadFromUrl(environment.resolveConfig(System.getProperty("tests.config")));
            } else {
                fail("to run integration tests, you need to set -Dtest.etcd=true and -Dtests.config=/path/to/elasticsearch.yml");
            }
        } catch (FailedToResolveConfigException exception) {
            fail("your test configuration file is incorrect: " + System.getProperty("tests.config"));
        }
        return settings.build();
    }
}