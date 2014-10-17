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
package org.elasticsearch.plugin.cloud.etcd;

import java.util.Collection;

import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.component.LifecycleComponent;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.AbstractPlugin;

/**
 *
 *
 * @since TODO
 */
public class CloudEtcdPlugin extends AbstractPlugin {

    private final Settings settings;

    public CloudEtcdPlugin(Settings settings) {
        this.settings = settings;
    }

    @Override
    public String description() {
        return "Cloud Etcd plugin";
    }

    @Override
    public String name() {
        return "cloud-etcd";
    }

    @Override
    public Collection<Module> modules(Settings settings) {
        Collection<Module> modules = Lists.newArrayList();
        if (settings.getAsBoolean("cloud.enabled", true)) {

        }
        return modules;
    }

    @Override
    public Collection<Class<? extends LifecycleComponent>> services() {
        Collection<Class<? extends LifecycleComponent>> services = Lists.newArrayList();
        if (settings.getAsBoolean("cloud.enabled", true)) {

            services.add(EtcdServiceImpl.class);
        }
        return services;
    }

}
