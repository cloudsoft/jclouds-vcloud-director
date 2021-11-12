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
package org.jclouds.vcloud.director.v1_5.features;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.jclouds.rest.annotations.JAXBResponseParser;
import org.jclouds.rest.annotations.QueryParams;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.vcloud.director.v1_5.domain.query.CatalogReferences;
import org.jclouds.vcloud.director.v1_5.domain.query.QueryList;
import org.jclouds.vcloud.director.v1_5.domain.query.QueryResultRecords;
import org.jclouds.vcloud.director.v1_5.domain.query.VAppReferences;
import org.jclouds.vcloud.director.v1_5.filters.AddAcceptHeaderToRequest;
import org.jclouds.vcloud.director.v1_5.filters.AddVCloudAuthorizationAndCookieToRequest;

/**
 * Provides access to the REST API query interface.
 */
@RequestFilters({AddVCloudAuthorizationAndCookieToRequest.class, AddAcceptHeaderToRequest.class})
public interface QueryApi {

   // TODO Add a typed object for filter syntax, or at least a fluent builder

   /**
    * <pre>
    * GET /query
    * </pre>
    */
   @GET
   @Path("/query")
   @Consumes
   @JAXBResponseParser
   QueryList queryList();

   /**
    * Retrieves a list of entities by using REST API general QueryHandler.
    *
    * If filter is provided it will be applied to the corresponding result set.
    * Format determines the elements representation - references or records.
    * Default format is references.
    *
    * <pre>
    * GET /query
    * </pre>
    */
   @GET
   @Path("/query")
   @Consumes
   @JAXBResponseParser
   QueryResultRecords queryAll(@QueryParam("type") String type);

   /** @see #queryAll(String) */
   @GET
   @Path("/query")
   @Consumes
   @JAXBResponseParser
   QueryResultRecords query(@QueryParam("type") String type, @QueryParam("filter") String filter);

   /** @see #queryAll(String) */
   @GET
   @Path("/query")
   @Consumes
   @JAXBResponseParser
   QueryResultRecords query(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
         @QueryParam("format") String format, @QueryParam("type") String type, @QueryParam("filter") String filter);

   /** @see #queryAll(String) */
   @GET
   @Path("/query")
   @Consumes
   @JAXBResponseParser
   QueryResultRecords query(@QueryParam("type") String type , @QueryParam("page") String page, @QueryParam("pageSize") String pageSize,
                            @QueryParam("format") String format);


   /**
    * Retrieves a list of {@link org.jclouds.vcloud.director.v1_5.endpoints.Catalog}s by using REST API general QueryHandler.
    *
    * <pre>
    * GET /catalogs/query
    * </pre>
    *
    * @see #queryAll(String)
    */
   @GET
   @Path("/catalogs/query")
   @Consumes
   @JAXBResponseParser
   QueryResultRecords catalogsQueryAll();

   /** @see #catalogsQueryAll()  */
   @GET
   @Path("/catalogs/query")
   @Consumes
   @JAXBResponseParser
   QueryResultRecords catalogsQuery(@QueryParam("filter") String filter);

   /** @see #catalogsQueryAll()  */
   @GET
   @Path("/catalogs/query")
   @Consumes
   @JAXBResponseParser
   QueryResultRecords catalogsQuery(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
         @QueryParam("filter") String filter);

   /**
    * Retrieves a list of {@link CatalogReference}s by using REST API general QueryHandler.
    *
    * <pre>
    * GET /catalogs/query?format=references
    * </pre>
    *
    * @see #queryAll(String)
    */
   @GET
   @Path("/catalogs/query")
   @Consumes
   @QueryParams(keys = { "format" }, values = { "references" })
   @JAXBResponseParser
   CatalogReferences catalogReferencesQueryAll();

   /** @see #catalogReferencesQueryAll() */
   @GET
   @Path("/catalogs/query")
   @Consumes
   @QueryParams(keys = { "format" }, values = { "references" })
   @JAXBResponseParser
   CatalogReferences catalogReferencesQuery(@QueryParam("filter") String filter);

   /** @see #catalogReferencesQueryAll() */
   @GET
   @Path("/catalogs/query")
   @Consumes
   @QueryParams(keys = { "format" }, values = { "references" })
   @JAXBResponseParser
   CatalogReferences catalogReferencesQuery(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
         @QueryParam("filter") String filter);

   /**
    * Retrieves a list of {@link org.jclouds.vcloud.director.v1_5.domain.VAppTemplate}s by using REST API general QueryHandler.
    *
    * <pre>
    * GET /vAppTemplates/query
    * </pre>
    *
    * @see #queryAll(String)
    */
   @GET
   @Path("/vAppTemplates/query")
   @Consumes
   @JAXBResponseParser
   QueryResultRecords vAppTemplatesQueryAll();

   /**
    * @see #vAppReferencesQueryAll
    */
   @GET
   @Path("/vAppTemplates/query")
   @Consumes
   @JAXBResponseParser
   QueryResultRecords vAppTemplatesQuery(@QueryParam("filter") String filter);

   @GET
   @Path("/vAppTemplates/query")
   @Consumes
   @JAXBResponseParser
   QueryResultRecords vAppTemplatesQuery(@QueryParam("page") String page, @QueryParam("pageSize") String pageSize,
                                 @QueryParam("format") String format);


   /**
    * Retrieves a list of {@link VApp}s by using REST API general QueryHandler.
    */
   @GET
   @Path("/vApps/query")
   @Consumes
   @JAXBResponseParser
   QueryResultRecords vAppsQueryAll();

   @GET
   @Path("/vApps/query")
   @Consumes
   @JAXBResponseParser
   QueryResultRecords vAppsQuery(@QueryParam("filter") String filter);

   @GET
   @Path("/vApps/query")
   @Consumes
   @JAXBResponseParser
   QueryResultRecords vAppsQuery(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
         @QueryParam("filter") String filter);

   /**
    * Retrieves a list of {@link VAppReference}s by using REST API general QueryHandler.
    *
    * <pre>
    * GET /vApps/query?format=references
    * </pre>
    *
    * @see #queryAll(String)
    */
   @GET
   @Path("/vApps/query")
   @Consumes
   @QueryParams(keys = { "format" }, values = { "references" })
   @JAXBResponseParser
   VAppReferences vAppReferencesQueryAll();

   @GET
   @Path("/vApps/query")
   @Consumes
   @QueryParams(keys = { "format" }, values = { "references" })
   @JAXBResponseParser
   VAppReferences vAppReferencesQuery(@QueryParam("filter") String filter);

   @GET
   @Path("/vApps/query")
   @Consumes
   @QueryParams(keys = { "format" }, values = { "references" })
   @JAXBResponseParser
   VAppReferences vAppReferencesQuery(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
         @QueryParam("filter") String filter);

   /**
    * Retrieves a list of {@link org.jclouds.vcloud.director.v1_5.domain.Vm}s by using REST API general QueryHandler.
    *
    * <pre>
    * GET /vms/query
    * </pre>
    *
    * @see #queryAll(String)
    */
   @GET
   @Path("/vms/query")
   @Consumes
   @JAXBResponseParser
   QueryResultRecords vmsQueryAll();

   @GET
   @Path("/vms/query")
   @Consumes
   @JAXBResponseParser
   QueryResultRecords vmsQuery(@QueryParam("filter") String filter);

   @GET
   @Path("/mediaList/query")
   @Consumes
   @JAXBResponseParser
   QueryResultRecords mediaListQueryAll();

   @GET
   @Path("/mediaList/query")
   @Consumes
   @JAXBResponseParser
   QueryResultRecords mediaListQuery(@QueryParam("filter") String filter);
}
