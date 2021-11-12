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

import static org.jclouds.vcloud.director.v1_5.VCloudDirectorMediaType.CONTROL_ACCESS;

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
import org.jclouds.vcloud.director.v1_5.domain.AdminCatalog;
import org.jclouds.vcloud.director.v1_5.domain.Owner;
import org.jclouds.vcloud.director.v1_5.domain.params.ControlAccessParams;
import org.jclouds.vcloud.director.v1_5.domain.params.PublishCatalogParams;
import org.jclouds.vcloud.director.v1_5.features.CatalogApi;
import org.jclouds.vcloud.director.v1_5.filters.AddVCloudAuthorizationAndCookieToRequest;
import org.jclouds.vcloud.director.v1_5.functions.URNToAdminHref;

/**
 * Provides synchronous access to {@link AdminCatalog} objects.
 */
@RequestFilters(AddVCloudAuthorizationAndCookieToRequest.class)
public interface AdminCatalogApi extends CatalogApi {

   /**
    * Creates a catalog in an organization. The catalog will always be addd in unpublished state.
    *
    * <pre
    * POST /admin/org/{id}/catalogs
    * </pre
    *
    * @param orgUrn
    *           the urn for the org
    * @return contains a , which will point to the running asynchronous creation operation.
    */
   @POST
   @Path("/catalogs")
   @Consumes(VCloudDirectorMediaType.ADMIN_CATALOG)
   @Produces(VCloudDirectorMediaType.ADMIN_CATALOG)
   @JAXBResponseParser
   AdminCatalog addCatalogToOrg(@BinderParam(BindToXMLPayload.class) AdminCatalog catalog,
            @EndpointParam(parser = URNToAdminHref.class) String orgUrn);

   /**
    * Retrieves a catalog.
    *
    * <pre
    * GET /admin/catalog/{id}
    * </pre
    *
    * @param orgHref
    *           the urn for the catalog
    * @return a catalog
    */
   @POST
   @Path("/catalogs")
   @Consumes(VCloudDirectorMediaType.ADMIN_CATALOG)
   @Produces(VCloudDirectorMediaType.ADMIN_CATALOG)
   @JAXBResponseParser
   AdminCatalog addCatalogToOrg(@BinderParam(BindToXMLPayload.class) AdminCatalog catalog,
            @EndpointParam URI orgHref);

   /**
    * @see AdminCatalogApi#get(String)
    */
   @Override
   @GET
   @Consumes
   @JAXBResponseParser
   @Fallback(NullOnNotFoundOr404.class)
   AdminCatalog get(@EndpointParam(parser = URNToAdminHref.class) String catalogUrn);

   /**
    * @see AdminCatalogApi#get(URI)
    */
   @Override
   @GET
   @Consumes
   @JAXBResponseParser
   @Fallback(NullOnNotFoundOr404.class)
   AdminCatalog get(@EndpointParam URI orgHref);

   /**
    * Modifies a catalog. A catalog could be published or unpublished. The IsPublished property is
    * treated as a read only value by the server. In order to control publishing settings use the
    * 'publish' action must be used.
    *
    * <pre
    * PUT /admin/catalog/{id}
    * </pre
    *
    * @return the edited catalog
    */
   @PUT
   @Consumes(VCloudDirectorMediaType.ADMIN_CATALOG)
   @Produces(VCloudDirectorMediaType.ADMIN_CATALOG)
   @JAXBResponseParser
   AdminCatalog edit(@EndpointParam(parser = URNToAdminHref.class) String catalogUrn,
            @BinderParam(BindToXMLPayload.class) AdminCatalog catalog);

   /**
    * @see AdminCatalogApi#edit(URI, AdminCatalog)
    */
   @PUT
   @Consumes(VCloudDirectorMediaType.ADMIN_CATALOG)
   @Produces(VCloudDirectorMediaType.ADMIN_CATALOG)
   @JAXBResponseParser
   AdminCatalog edit(@EndpointParam URI catalogAdminHref,
            @BinderParam(BindToXMLPayload.class) AdminCatalog catalog);

   /**
    * Deletes a catalog. The catalog could be removed if it is either published or unpublished.
    *
    * <pre
    * DELETE /admin/catalog/{id}
    * </pre
    */
   @DELETE
   @Consumes
   @JAXBResponseParser
   Void remove(@EndpointParam(parser = URNToAdminHref.class) String catalogUrn);

   /**
    * @see AdminCatalogApi#remove(URI)
    */
   @DELETE
   @Consumes
   @JAXBResponseParser
   Void remove(@EndpointParam URI catalogAdminHref);

   /**
    * Retrieves the owner of a catalog.
    *
    * <pre
    * GET /admin/catalog/{id}/owner
    * </pre
    *
    * @return the owner or null if not found
    */
   @GET
   @Path("/owner")
   @Consumes
   @JAXBResponseParser
   @Fallback(NullOnNotFoundOr404.class)
   Owner getOwner(@EndpointParam(parser = URNToAdminHref.class) String catalogUrn);

   /**
    * @see AdminCatalogApi#getOwner(URI)
    */
   @GET
   @Path("/owner")
   @Consumes
   @JAXBResponseParser
   @Fallback(NullOnNotFoundOr404.class)
   Owner getOwner(@EndpointParam URI catalogAdminHref);

   /**
    * Changes owner for catalog.
    *
    * <pre
    * PUT /admin/catalog/{id}/owner
    * </pre
    */
   @PUT
   @Path("/owner")
   @Consumes
   @Produces(VCloudDirectorMediaType.OWNER)
   @JAXBResponseParser
   Void setOwner(@EndpointParam(parser = URNToAdminHref.class) String catalogUrn,
            @BinderParam(BindToXMLPayload.class) Owner newOwner);

   /**
    * @see AdminCatalogApi#setOwner(URI, Owner)
    */
   @PUT
   @Path("/owner")
   @Consumes
   @Produces(VCloudDirectorMediaType.OWNER)
   @JAXBResponseParser
   Void setOwner(@EndpointParam URI catalogAdminHref,
            @BinderParam(BindToXMLPayload.class) Owner newOwner);

   /**
    * Publish a catalog. Publishing a catalog makes the catalog visible to all organizations in a
    * vCloud.
    * @param catalogUrn
    */
   @POST
   @Path("/action/publish")
   @Consumes
   @Produces(VCloudDirectorMediaType.PUBLISH_CATALOG_PARAMS)
   @JAXBResponseParser
   Void publish(@EndpointParam(parser = URNToAdminHref.class) String catalogUrn,
            @BinderParam(BindToXMLPayload.class) PublishCatalogParams params);

   /**
    * @see AdminCatalogApi#publish(URI, PublishCatalogParams)
    */
   @POST
   @Path("/action/publish")
   @Consumes
   @Produces(VCloudDirectorMediaType.PUBLISH_CATALOG_PARAMS)
   @JAXBResponseParser
   Void publish(@EndpointParam URI catalogAdminHref,
            @BinderParam(BindToXMLPayload.class) PublishCatalogParams params);

   /**
    * Modifies a catalog control access.
    *
    * <pre
    * POST /org/{id}/catalog/{catalogId}/action/controlAccess
    * </pre
    *
    * @return the control access information
    */
   @POST
   @Path("/action/controlAccess")
   @Produces(CONTROL_ACCESS)
   @Consumes(CONTROL_ACCESS)
   @JAXBResponseParser
   ControlAccessParams editAccessControl(@EndpointParam(parser = URNToAdminHref.class) String catalogUrn,
      @BinderParam(BindToXMLPayload.class) ControlAccessParams params);

   /**
    * Retrieves the catalog control access information.
    *
    * <pre
    * GET /org/{id}/catalog/{catalogId}/controlAccess
    * </pre
    *
    * @return the control access information
    */
   @POST
   @Path("/action/controlAccess")
   @Produces(CONTROL_ACCESS)
   @Consumes(CONTROL_ACCESS)
   @JAXBResponseParser
   ControlAccessParams editAccessControl(@EndpointParam URI catalogAdminHref,
      @BinderParam(BindToXMLPayload.class) ControlAccessParams params);
   
   /**
    * @see AdminCatalogApi#getAccessControl(String)
    */
   @GET
   @Path("/controlAccess")
   @Consumes
   @JAXBResponseParser
   @Fallback(NullOnNotFoundOr404.class)
   ControlAccessParams getAccessControl(@EndpointParam(parser = URNToAdminHref.class) String catalogUrn);

   /**
    * @see AdminCatalogApi#getAccessControl(URI)
    */
   @GET
   @Path("/controlAccess")
   @Consumes
   @JAXBResponseParser
   @Fallback(NullOnNotFoundOr404.class)
   ControlAccessParams getAccessControl(@EndpointParam URI catalogAdminHref);
}
