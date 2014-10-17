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

import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;

public class EtcdModule extends AbstractModule {
    private Settings settings;

    public static final String ETCD_SERVICE_TYPE_KEY = "cloud.aws.s3service.type";

    @Inject
    public EtcdModule(Settings settings) {
        this.settings = settings;
    }

    @Override
    protected void configure() {
        bind(EtcdService.class).to(EtcdServiceImpl.class).asEagerSingleton();
    }


    public static Class<? extends EtcdService> getS3ServiceClass(Settings settings) {
        return settings.getAsClass(ETCD_SERVICE_TYPE_KEY, EtcdServiceImpl.class);
    }
}
