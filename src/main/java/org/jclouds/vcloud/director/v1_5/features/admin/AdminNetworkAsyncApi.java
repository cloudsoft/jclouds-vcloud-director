/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.vcloud.director.v1_5.features.admin;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jclouds.Fallbacks.NullOnNotFoundOr404;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.EndpointParam;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.JAXBResponseParser;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.binders.BindToXMLPayload;
import org.jclouds.vcloud.director.v1_5.VCloudDirectorMediaType;
import org.jclouds.vcloud.director.v1_5.domain.Task;
import org.jclouds.vcloud.director.v1_5.domain.network.Network;
import org.jclouds.vcloud.director.v1_5.domain.org.OrgNetwork;
import org.jclouds.vcloud.director.v1_5.features.NetworkApi;
import org.jclouds.vcloud.director.v1_5.filters.AddVCloudAuthorizationAndCookieToRequest;
import org.jclouds.vcloud.director.v1_5.functions.URNToAdminHref;

/**
 * Provides synchronous access to admin {@link Network} objects.
 */
@RequestFilters(AddVCloudAuthorizationAndCookieToRequest.class)
public interface AdminNetworkAsyncApi extends NetworkApi {

   /**
    * Gets admin representation of network. This operation could return admin representation of
    * organization network or external network. vApp networks do not have admin representation.
    *
    * <pre>
    * GET /admin/network/{id}
    * </pre>
    *
    * @param networkUrn
    *           the reference for the network
    * @return the network
    */
   @Override
   @GET
   @Consumes
   @JAXBResponseParser
   @Fallback(NullOnNotFoundOr404.class)
   Network get(@EndpointParam(parser = URNToAdminHref.class) String networkUrn);

   /**
    * @see AdminNetworkApi#get(URI)
    */
   @Override
   @GET
   @Consumes
   @JAXBResponseParser
   @Fallback(NullOnNotFoundOr404.class)
   Network get(@EndpointParam URI networkAdminHref);

   /**
    * Modifies an org network
    *
    * <pre>
    * PUT /admin/network/{id}
    * </pre>
    *
    * @param networkUrn
    *           the reference for the network
    * @param network
    *           the edited network
    * @return a task. This operation is asynchronous and the user should monitor the returned task
    *         status in order to check when it is completed.
    */
   @PUT
   @Consumes(VCloudDirectorMediaType.TASK)
   @Produces(VCloudDirectorMediaType.ADMIN_ORG_NETWORK)
   @JAXBResponseParser
   Task edit(@EndpointParam(parser = URNToAdminHref.class) String networkUrn,
            @BinderParam(BindToXMLPayload.class) OrgNetwork network);

   /**
    * @see AdminNetworkApi#edit(URI, OrgNetwork)
    */
   @PUT
   @Consumes(VCloudDirectorMediaType.TASK)
   @Produces(VCloudDirectorMediaType.ADMIN_ORG_NETWORK)
   @JAXBResponseParser
   Task edit(@EndpointParam URI networkAdminHref,
            @BinderParam(BindToXMLPayload.class) OrgNetwork network);

   /**
    * Reset(undeploy & redeploy) networking services on a logical network. The reset operation can
    * be performed on: - external networks - organization networks - vApp networks The reset
    * operation can be performed only on deployed networks.
    *
    * <pre>
    * POST /admin/network/{id}/action/reset
    * </pre>
    *
    * @param networkUrn
    *           the reference for the network
    * @return a task. This operation is asynchronous and the user should monitor the returned task
    *         status in order to check when it is completed.
    */
   @POST
   @Path("/action/reset")
   @Consumes
   @JAXBResponseParser
   Task reset(@EndpointParam(parser = URNToAdminHref.class) String networkUrn);

   /**
    * @see AdminNetworkApi#reset(URI)
    */
   @POST
   @Path("/action/reset")
   @Consumes
   @JAXBResponseParser
   Task reset(@EndpointParam URI networkAdminHref);
}
