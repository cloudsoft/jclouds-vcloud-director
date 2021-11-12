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
package org.jclouds.vcloud.director.v1_5.login;

import java.io.Closeable;
import java.net.URI;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;

import org.jclouds.rest.annotations.EndpointParam;
import org.jclouds.rest.annotations.JAXBResponseParser;
import org.jclouds.rest.annotations.MapBinder;
import org.jclouds.rest.annotations.PayloadParam;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.ResponseParser;
import org.jclouds.vcloud.director.v1_5.binders.BindUserOrgAndPasswordAsBasicAuthorizationHeader;
import org.jclouds.vcloud.director.v1_5.domain.Session;
import org.jclouds.vcloud.director.v1_5.domain.SessionWithToken;
import org.jclouds.vcloud.director.v1_5.filters.AddAcceptHeaderToRequest;
import org.jclouds.vcloud.director.v1_5.parsers.SessionWithTokenFromXMLAndHeader;

/**
 * Provides access to Session via their REST API.
 */
@RequestFilters(AddAcceptHeaderToRequest.class)
public interface SessionApi extends Closeable {

   /**
    * TODO
    */
   @Named("login")
   @POST
   @Consumes
   @ResponseParser(SessionWithTokenFromXMLAndHeader.class)
   @MapBinder(BindUserOrgAndPasswordAsBasicAuthorizationHeader.class)
   SessionWithToken loginUserInOrgWithPassword(@EndpointParam URI loginUrl,
                                               @PayloadParam("user") String user,
                                               @PayloadParam("org") String org,
                                               @PayloadParam("password") String password);

   /**
    * TODO
    */
   @Named("getSession")
   @GET
   @Consumes
   @JAXBResponseParser
   Session getSessionWithToken(@EndpointParam URI session,
                               @HeaderParam("x-vcloud-authorization") String authenticationToken);

   /**
    * TODO
    */
   @Named("logout")
   @DELETE
   @Consumes
   @JAXBResponseParser
   void logoutSessionWithToken(@EndpointParam URI session,
                               @HeaderParam("x-vcloud-authorization") String authenticationToken);
}
