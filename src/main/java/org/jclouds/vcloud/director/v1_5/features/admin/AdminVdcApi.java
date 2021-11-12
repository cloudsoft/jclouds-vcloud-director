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
import org.jclouds.rest.annotations.EndpointParam;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.JAXBResponseParser;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.vcloud.director.v1_5.VCloudDirectorMediaType;
import org.jclouds.vcloud.director.v1_5.domain.AdminVdc;
import org.jclouds.vcloud.director.v1_5.domain.Task;
import org.jclouds.vcloud.director.v1_5.features.VdcApi;
import org.jclouds.vcloud.director.v1_5.filters.AddVCloudAuthorizationAndCookieToRequest;
import org.jclouds.vcloud.director.v1_5.functions.URNToAdminHref;

/**
 * Provides access to {@link AdminVdc}.
 */
@RequestFilters(AddVCloudAuthorizationAndCookieToRequest.class)
public interface AdminVdcApi extends VdcApi {

   /**
    * Retrieves an admin view of virtual data center. The redwood admin can disable an
    * organization vDC. This will prevent any further allocation to be used by the organization.
    * Changing the state will not affect allocations already used. For example, if an organization
    * vDC is disabled, an organization user cannot deploy or add a new virtual machine in the
    * vDC (deploy uses memory and cpu allocations, and add uses storage allocation).
    *
    * @return the admin vDC or null if not found
    */
   @Override
   @GET
   @Consumes
   @JAXBResponseParser
   @Fallback(NullOnNotFoundOr404.class)
   AdminVdc get(@EndpointParam(parser = URNToAdminHref.class) String vdcUrn);

   /**
    * Modifies a Virtual Data Center. Virtual Data Center could be enabled or disabled.
    * Additionally it could have one of these states FAILED_CREATION(-1), NOT_READY(0),
    * READY(1), UNKNOWN(1) and UNRECOGNIZED(3).
    */
   @PUT
   @Consumes
   @Produces(VCloudDirectorMediaType.ADMIN_VDC)
   @JAXBResponseParser
   Task edit(@EndpointParam(parser = URNToAdminHref.class) String vdcUrn, AdminVdc vdc);

   /**
    * Deletes a Virtual Data Center. The Virtual Data Center should be disabled when remove is issued.
    * Otherwise error code 400 Bad Request is returned.
    */
   @DELETE
   @Consumes
   @JAXBResponseParser
   Task remove(@EndpointParam(parser = URNToAdminHref.class) String vdcUrn);

   /**
    * Enables a Virtual Data Center. This operation enables disabled Virtual Data Center.
    * If it is already enabled this operation has no effect.
    */
   @POST
   @Consumes
   @Path("/action/enable")
   @JAXBResponseParser
   Void enable(@EndpointParam(parser = URNToAdminHref.class) String vdcUrn);

   /**
    * Disables a Virtual Data Center. If the Virtual Data Center is disabled this operation does not
    * have an effect.
    */
   @POST
   @Consumes
   @Path("/action/disable")
   @JAXBResponseParser
   Void disable(@EndpointParam(parser = URNToAdminHref.class) String vdcUrn);

   /**
    * @see AdminVdcApi#get(URI)
    */
   @Override
   @GET
   @Consumes
   @JAXBResponseParser
   @Fallback(NullOnNotFoundOr404.class)
   AdminVdc get(@EndpointParam URI vdcAdminHref);

   /**
    * @see AdminVdcApi#edit(URI, AdminVdc)
    */
   @PUT
   @Consumes
   @Produces(VCloudDirectorMediaType.ADMIN_VDC)
   @JAXBResponseParser
   Task edit(@EndpointParam URI vdcAdminHref, AdminVdc vdc);

   /**
    * @see AdminVdcApi#remove(URI)
    */
   @DELETE
   @Consumes
   @JAXBResponseParser
   Task remove(@EndpointParam URI vdcAdminHref);

   /**
    * @see AdminVdcApi#enable(URI)
    */
   @POST
   @Consumes
   @Path("/action/enable")
   @JAXBResponseParser
   Void enable(@EndpointParam URI vdcAdminHref);

   /**
    * @see AdminVdcApi#disable(URI)
    */
   @POST
   @Consumes
   @Path("/action/disable")
   @JAXBResponseParser
   Void disable(@EndpointParam URI vdcAdminHref);
}
