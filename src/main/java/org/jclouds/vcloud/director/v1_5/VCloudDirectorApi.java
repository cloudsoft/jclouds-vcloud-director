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
package org.jclouds.vcloud.director.v1_5;

import java.io.Closeable;
import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.jclouds.Fallbacks.NullOnNotFoundOr404;
import org.jclouds.rest.annotations.Delegate;
import org.jclouds.rest.annotations.EndpointParam;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.JAXBResponseParser;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.vcloud.director.v1_5.domain.Entity;
import org.jclouds.vcloud.director.v1_5.domain.Session;
import org.jclouds.vcloud.director.v1_5.features.CatalogApi;
import org.jclouds.vcloud.director.v1_5.features.MediaApi;
import org.jclouds.vcloud.director.v1_5.features.MetadataApi;
import org.jclouds.vcloud.director.v1_5.features.NetworkApi;
import org.jclouds.vcloud.director.v1_5.features.OrgApi;
import org.jclouds.vcloud.director.v1_5.features.QueryApi;
import org.jclouds.vcloud.director.v1_5.features.TaskApi;
import org.jclouds.vcloud.director.v1_5.features.UploadApi;
import org.jclouds.vcloud.director.v1_5.features.VAppApi;
import org.jclouds.vcloud.director.v1_5.features.VAppTemplateApi;
import org.jclouds.vcloud.director.v1_5.features.VdcApi;
import org.jclouds.vcloud.director.v1_5.features.VmApi;
import org.jclouds.vcloud.director.v1_5.features.admin.AdminCatalogApi;
import org.jclouds.vcloud.director.v1_5.features.admin.AdminNetworkApi;
import org.jclouds.vcloud.director.v1_5.features.admin.AdminOrgApi;
import org.jclouds.vcloud.director.v1_5.features.admin.AdminQueryApi;
import org.jclouds.vcloud.director.v1_5.features.admin.AdminVdcApi;
import org.jclouds.vcloud.director.v1_5.features.admin.GroupApi;
import org.jclouds.vcloud.director.v1_5.features.admin.UserApi;
import org.jclouds.vcloud.director.v1_5.filters.AddAcceptHeaderToRequest;
import org.jclouds.vcloud.director.v1_5.filters.AddVCloudAuthorizationAndCookieToRequest;
import org.jclouds.vcloud.director.v1_5.functions.URNToHref;
import org.jclouds.vcloud.director.v1_5.login.SessionApi;

import com.google.inject.Provides;

/**
 * Provides access to VCloudDirector via their REST API.
 */
@RequestFilters({AddVCloudAuthorizationAndCookieToRequest.class, AddAcceptHeaderToRequest.class})
public interface VCloudDirectorApi extends Closeable {

   /**
    * Redirects to the URL of an entity with the given VCD ID.
    *
    * <pre>
    * GET /entity/{id}
    * </pre>
    */
   @GET
   @Path("/entity/{id}")
   @Consumes
   @JAXBResponseParser
   @Fallback(NullOnNotFoundOr404.class)
   Entity resolveEntity(@PathParam("id") String id);

   /**
    * @return the current login session
    */
   @Provides
   Session getCurrentSession();

   /**
    * @return access to session features
    */
   @Delegate
   SessionApi getSessionApi();

   /**
    * @return access to query features
    */
   @Delegate
   QueryApi getQueryApi();

   /**
    * @return access to {@link org.jclouds.vcloud.director.v1_5.features.OrgApi} features
    */
   @Delegate
   OrgApi getOrgApi();

   /**
    * @return access to {@link org.jclouds.vcloud.director.v1_5.features.TaskApi} features
    */
   @Delegate
   TaskApi getTaskApi();

   /**
    * @return access to {@link org.jclouds.vcloud.director.v1_5.features.NetworkApi} features
    */
   @Delegate
   NetworkApi getNetworkApi();

   /**
    * @return access to {@link org.jclouds.vcloud.director.v1_5.features.CatalogApi} features
    */
   @Delegate
   CatalogApi getCatalogApi();

   /**
    * @return access to {@link org.jclouds.vcloud.director.v1_5.features.MediaApi} features
    */
   @Delegate
   MediaApi getMediaApi();

   /**
    * @return access to {@link org.jclouds.vcloud.director.v1_5.features.VdcApi} features
    */
   @Delegate
   VdcApi getVdcApi();

   /**
    * @return access to Upload features
    */
   @Delegate
   UploadApi getUploadApi();

   /**
    * @return access to {@link org.jclouds.vcloud.director.v1_5.features.VAppApi} features
    */
   @Delegate
   VAppApi getVAppApi();

   /**
    * @return access to {@link org.jclouds.vcloud.director.v1_5.features.VAppTemplateApi} features
    */
   @Delegate
   VAppTemplateApi getVAppTemplateApi();

   /**
    * @return access to {@link org.jclouds.vcloud.director.v1_5.features.VmApi} features
    */
   @Delegate
   VmApi getVmApi();
   
   /**
    * @return access to {@link org.jclouds.vcloud.director.v1_5.features.MetadataApi} features
    */
   @Delegate
   MetadataApi getMetadataApi(@EndpointParam(parser = URNToHref.class) String urn);

   @Delegate
   MetadataApi getMetadataApi(@EndpointParam URI href);

   /////// admin
   /**
    * @return access to admin query features
    */
   @Delegate
   AdminQueryApi getAdminQueryApi();


   /**
    * @return access to {@link org.jclouds.vcloud.director.v1_5.features.admin.AdminCatalogApi} admin features
    */
   @Delegate
   AdminCatalogApi getAdminCatalogApi();

   /**
    * @return access to admin {@link org.jclouds.vcloud.director.v1_5.features.admin.GroupApi} features
    */
   @Delegate
   GroupApi getGroupApi();

   /**
    * @return access to {@link org.jclouds.vcloud.director.v1_5.features.admin.AdminOrgApi} features
    */
   @Delegate
   AdminOrgApi getAdminOrgApi();

   /**
    * @return access to {@link org.jclouds.vcloud.director.v1_5.features.admin.UserApi} features
    */
   @Delegate
   UserApi getUserApi();

   /**
    * @return access to {@link org.jclouds.vcloud.director.v1_5.features.admin.AdminVdcApi} features
    */
   @Delegate
   AdminVdcApi getAdminVdcApi();

   /**
    * @return access to admin {@link org.jclouds.vcloud.director.v1_5.features.admin.AdminNetworkApi} features
    */
   @Delegate
   AdminNetworkApi getAdminNetworkApi();

}
