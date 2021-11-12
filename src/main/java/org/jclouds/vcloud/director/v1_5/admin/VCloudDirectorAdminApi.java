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
package org.jclouds.vcloud.director.v1_5.admin;

import java.net.URI;

import org.jclouds.rest.annotations.Delegate;
import org.jclouds.rest.annotations.EndpointParam;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.vcloud.director.v1_5.features.MetadataApi;
import org.jclouds.vcloud.director.v1_5.features.admin.AdminCatalogApi;
import org.jclouds.vcloud.director.v1_5.features.admin.AdminNetworkApi;
import org.jclouds.vcloud.director.v1_5.features.admin.AdminOrgApi;
import org.jclouds.vcloud.director.v1_5.features.admin.AdminQueryApi;
import org.jclouds.vcloud.director.v1_5.features.admin.AdminVdcApi;
import org.jclouds.vcloud.director.v1_5.features.admin.GroupApi;
import org.jclouds.vcloud.director.v1_5.features.admin.UserApi;
import org.jclouds.vcloud.director.v1_5.filters.AddVCloudAuthorizationAndCookieToRequest;
import org.jclouds.vcloud.director.v1_5.functions.URNToAdminHref;

/**
 * Provides access to VCloudDirector Admin via their REST API.
 */
@RequestFilters(AddVCloudAuthorizationAndCookieToRequest.class)
public interface VCloudDirectorAdminApi {
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
   
   /**
    * @return access to {@link org.jclouds.vcloud.director.v1_5.features.MetadataApi} features
    */
   @Delegate
   MetadataApi getMetadataApi(@EndpointParam(parser = URNToAdminHref.class) String urn);

   @Delegate
   MetadataApi getMetadataApi(@EndpointParam URI href);
}
