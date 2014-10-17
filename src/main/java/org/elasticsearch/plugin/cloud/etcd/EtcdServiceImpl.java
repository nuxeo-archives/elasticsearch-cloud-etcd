package org.elasticsearch.plugin.cloud.etcd;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class EtcdServiceImpl extends
        AbstractLifecycleComponent<EtcdServiceImpl> implements EtcdService {

    private String etcdHost;

    private String etcdKey;

    @Inject
    public EtcdServiceImpl(Settings settings, SettingsFilter settingsFilter) {
        super(settings);
        etcdHost = settings.get("cloud.etcd.host","http://127.0.0.1:4001");
        etcdKey = settings.get("cloud.etcd.key","/services/elasticsearch");
    }

    @Override
    public List<Location> transports() {
        List<Location> locations = new ArrayList<>();

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(String.format("%s/v2/keys",
                etcdHost));

        ClientResponse response = service.path(etcdKey).queryParam("recursive", "true").get(
                ClientResponse.class);

        if (response.getStatus() != Response.Status.OK.getStatusCode()
                && response.getStatus() != Response.Status.NOT_FOUND.getStatusCode()) {
            logger.error("Error when fetching etcd");
        }

        try {

            ObjectMapper mapper = new ObjectMapper();
            EtcdResult result = mapper.readValue(
                    response.getEntityInputStream(), EtcdResult.class);

            if(result.node.nodes != null && !result.node.nodes.isEmpty()) {
                for(EtcdNode node : result.node.nodes) {
                    String serviceKey = node.key;
                    String id = serviceKey.substring(serviceKey.lastIndexOf("/"));
                    for( EtcdNode subnode : node.nodes) {
                        if((serviceKey + "/transport").equals(subnode.key)) {
                            Location location = mapper.readValue(subnode.value, Location.class);
                            location.id = id;
                            locations.add(location);
                        }
                    }
                }
            } else {
                logger.error("No response from etcd");
            }

            return locations;
        } catch (Exception e) {
            logger.error("No response from etcd");
            logger.trace("No response from etcd", e);
            return locations;
        }

    }

    @Override
    protected void doClose() throws ElasticsearchException {
    }

    @Override
    protected void doStart() throws ElasticsearchException {

    }

    @Override
    protected void doStop() throws ElasticsearchException {
    }
}