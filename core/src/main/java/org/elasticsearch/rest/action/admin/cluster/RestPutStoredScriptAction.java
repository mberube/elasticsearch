/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.rest.action.admin.cluster;

import org.elasticsearch.action.admin.cluster.storedscripts.PutStoredScriptRequest;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.AcknowledgedRestListener;

import java.io.IOException;

import static org.elasticsearch.rest.RestRequest.Method.POST;
import static org.elasticsearch.rest.RestRequest.Method.PUT;

public class RestPutStoredScriptAction extends BaseRestHandler {
    public RestPutStoredScriptAction(Settings settings, RestController controller) {
        this(settings, controller, true);
    }

    protected RestPutStoredScriptAction(Settings settings, RestController controller, boolean registerDefaultHandlers) {
        super(settings);
        if (registerDefaultHandlers) {
            controller.registerHandler(POST, "/_scripts/{lang}/{id}", this);
            controller.registerHandler(PUT, "/_scripts/{lang}/{id}", this);
        }
    }

    protected String getScriptLang(RestRequest request) {
        return request.param("lang");
    }

    @Override
    public RestChannelConsumer prepareRequest(final RestRequest request, NodeClient client) throws IOException {
        PutStoredScriptRequest putRequest = new PutStoredScriptRequest(getScriptLang(request), request.param("id"));
        putRequest.script(request.content());
        return channel -> client.admin().cluster().putStoredScript(putRequest, new AcknowledgedRestListener<>(channel));
    }
}
