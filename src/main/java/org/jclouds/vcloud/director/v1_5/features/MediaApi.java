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
import org.jclouds.vcloud.director.v1_5.domain.Media;
import org.jclouds.vcloud.director.v1_5.domain.Owner;
import org.jclouds.vcloud.director.v1_5.domain.Task;
import org.jclouds.vcloud.director.v1_5.domain.params.CloneMediaParams;
import org.jclouds.vcloud.director.v1_5.filters.AddAcceptHeaderToRequest;
import org.jclouds.vcloud.director.v1_5.filters.AddVCloudAuthorizationAndCookieToRequest;
import org.jclouds.vcloud.director.v1_5.functions.URNToHref;

/**
 * Provides access to {@link Media} API
 *
 */
@RequestFilters({AddVCloudAuthorizationAndCookieToRequest.class, AddAcceptHeaderToRequest.class})
public interface MediaApi {

   /**
    * Retrieves a media.
    *
    * @return the media or null if not found
    */
   @GET
   @Consumes
   @JAXBResponseParser
   @Fallback(NullOnNotFoundOr404.class)
   Media get(@EndpointParam(parser = URNToHref.class) String mediaUrn);

   /**
    * Creates a media (and present upload link for the floppy/iso file).
    *
    * @return The response will return a link to transfer site to be able to continue with uploading
    *         the media.
    */
   @POST
   @Consumes(VCloudDirectorMediaType.MEDIA)
   @Produces(VCloudDirectorMediaType.MEDIA)
   @JAXBResponseParser
   Media add(@EndpointParam URI updateHref, @BinderParam(BindToXMLPayload.class) Media media);

   /**
    * Clones a media into new one. The status of the returned media is UNRESOLVED(0) until the task
    * for cloning finish.
    *
    * @return a Media resource which will contain a task. The user should monitor the contained task
    *         status in order to check when it is completed.
    */
   @POST
   @Path("/action/cloneMedia")
   @Consumes(VCloudDirectorMediaType.MEDIA)
   @Produces(VCloudDirectorMediaType.CLONE_MEDIA_PARAMS)
   @JAXBResponseParser
   Media clone(@EndpointParam(parser = URNToHref.class) String mediaUrn,
            @BinderParam(BindToXMLPayload.class) CloneMediaParams params);

   /**
    * Updates the name/description of a media.
    *
    * @return a task. This operation is asynchronous and the user should monitor the returned task
    *         status in order to check when it is completed.
    */
   @PUT
   @Consumes(VCloudDirectorMediaType.TASK)
   @Produces(VCloudDirectorMediaType.MEDIA)
   @JAXBResponseParser
   Task edit(@EndpointParam(parser = URNToHref.class) String mediaUrn,
            @BinderParam(BindToXMLPayload.class) Media media);

   /**
    * Deletes a media.
    */
   @DELETE
   @Consumes(VCloudDirectorMediaType.TASK)
   @JAXBResponseParser
   Task remove(@EndpointParam(parser = URNToHref.class) String mediaUrn);

   /**
    * Retrieves an owner.
    *
    * @return the owner or null if not found
    */
   @GET
   @Path("/owner")
   @Consumes
   @JAXBResponseParser
   @Fallback(NullOnNotFoundOr404.class)
   Owner getOwner(@EndpointParam(parser = URNToHref.class) String mediaUrn);

   /**
    * @see MediaApi#get(URI)
    */
   @GET
   @Consumes
   @JAXBResponseParser
   @Fallback(NullOnNotFoundOr404.class)
   Media get(@EndpointParam URI mediaHref);

   /**
    * @see MediaApi#clone(URI, CloneMediaParams)
    */
   @POST
   @Path("/action/cloneMedia")
   @Consumes(VCloudDirectorMediaType.MEDIA)
   @Produces(VCloudDirectorMediaType.CLONE_MEDIA_PARAMS)
   @JAXBResponseParser
   Media clone(@EndpointParam URI mediaHref,
            @BinderParam(BindToXMLPayload.class) CloneMediaParams params);

   /**
    * @see MediaApi#editMedia(URI, Media)
    */
   @PUT
   @Consumes(VCloudDirectorMediaType.TASK)
   @Produces(VCloudDirectorMediaType.MEDIA)
   @JAXBResponseParser
   Task edit(@EndpointParam URI mediaHref, @BinderParam(BindToXMLPayload.class) Media media);

   /**
    * @see MediaApi#removeMedia(URI)
    */
   @DELETE
   @Consumes(VCloudDirectorMediaType.TASK)
   @JAXBResponseParser
   Task remove(@EndpointParam URI mediaHref);

   /**
    * @see MediaApi#getOwner(URI)
    */
   @GET
   @Path("/owner")
   @Consumes
   @JAXBResponseParser
   @Fallback(NullOnNotFoundOr404.class)
   Owner getOwner(@EndpointParam URI mediaHref);
}
