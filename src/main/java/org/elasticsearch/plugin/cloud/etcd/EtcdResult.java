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
 *     nuxeo.io Team
 */

package org.elasticsearch.plugin.cloud.etcd;

import java.util.List;

/**
 * @since 1.0
 */
public class EtcdResult {

    public String action;

    public EtcdNode node;

    public EtcdNode prevNode;

    public List<EtcdNode> nodes;

    public int errorCode;

    public String message;

    public String cause;

    public int index;
}
