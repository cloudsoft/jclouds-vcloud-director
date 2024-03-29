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
import javax.ws.rs.DELETE;
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
import org.jclouds.vcloud.director.v1_5.domain.Group;
import org.jclouds.vcloud.director.v1_5.filters.AddVCloudAuthorizationAndCookieToRequest;
import org.jclouds.vcloud.director.v1_5.functions.URNToAdminHref;
import org.jclouds.vcloud.director.v1_5.functions.URNToHref;

/**
 * Provides access to {@link Group} objects.
 */
@RequestFilters(AddVCloudAuthorizationAndCookieToRequest.class)
public interface GroupApi {

   /**
    * Imports a group in an organization.
    *
    * <pre>
    * POST /admin/org/{id}/groups
    * </pre>
    *
    * @param adminUrn the admin org to add the group in
    * @return the added group
    */
   @POST
   @Path("/groups")
   @Consumes(VCloudDirectorMediaType.GROUP)
   @Produces(VCloudDirectorMediaType.GROUP)
   @JAXBResponseParser
   Group addGroupToOrg(@BinderParam(BindToXMLPayload.class) Group group,
            @EndpointParam(parser = URNToAdminHref.class) String adminUrn);

   /**
    * Retrieves a group.
    *
    * @param groupString the reference for the group
    * @return a group
    */
   @GET
   @Consumes
   @JAXBResponseParser
   @Fallback(NullOnNotFoundOr404.class)
   Group get(@EndpointParam(parser = URNToHref.class) String groupUri);

   /**
    * Modifies a group.
    *
    * <pre
    * PUT /admin/group/{id}
    * </pre
    *
    * @return the edited group
    */
   @PUT
   @Consumes(VCloudDirectorMediaType.GROUP)
   @Produces(VCloudDirectorMediaType.GROUP)
   @JAXBResponseParser
   Group edit(@EndpointParam(parser = URNToHref.class) String groupUrn,
            @BinderParam(BindToXMLPayload.class) Group group);

   /**
    * Deletes a group.
    *
    * <pre
    * DELETE /admin/group/{id}
    * </pre
    */
   @DELETE
   @Consumes
   @JAXBResponseParser
   Void remove(@EndpointParam(parser = URNToHref.class) String groupUrn);

   /**
    * @see GroupApi#addGroupToOrg(Group, URI)
    */
   @POST
   @Path("/groups")
   @Consumes(VCloudDirectorMediaType.GROUP)
   @Produces(VCloudDirectorMediaType.GROUP)
   @JAXBResponseParser
   Group addGroupToOrg(@BinderParam(BindToXMLPayload.class) Group group, @EndpointParam URI adminUrn);

   /**
    * @see GroupApi#get(URI)
    */
   @GET
   @Consumes
   @JAXBResponseParser
   @Fallback(NullOnNotFoundOr404.class)
   Group get(@EndpointParam URI groupUri);

   /**
    * @see GroupApi#edit(URI, Group)
    */
   @PUT
   @Consumes(VCloudDirectorMediaType.GROUP)
   @Produces(VCloudDirectorMediaType.GROUP)
   @JAXBResponseParser
   Group edit(@EndpointParam URI groupUrn, @BinderParam(BindToXMLPayload.class) Group group);

   /**
    * @see GroupApi#remove(URI)
    */
   @DELETE
   @Consumes
   @JAXBResponseParser
   Void remove(@EndpointParam URI groupUrn);
}
