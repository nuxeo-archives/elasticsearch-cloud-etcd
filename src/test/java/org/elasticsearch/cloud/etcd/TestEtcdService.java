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

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.plugin.cloud.etcd.EtcdService;
import org.elasticsearch.plugin.cloud.etcd.Location;

public class TestEtcdService implements EtcdService {

    @Override
    public List<Location> transports() {
        return new ArrayList<>();
    }

}
