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
package org.jclouds.vcloud.director.v1_5.handlers;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.http.functions.ParseSax.Factory;
import org.jclouds.logging.Logger;
import org.jclouds.vcloud.director.v1_5.domain.VcloudDirectorError;

@Singleton
public class VcloudDirectorUtils {

   private final ParseSax.Factory factory;
   private final Provider<ErrorHandler> errorHandlerProvider;

   @Resource
   protected Logger logger = Logger.NULL;

   @Inject
   VcloudDirectorUtils(Factory factory, Provider<ErrorHandler> errorHandlerProvider) {
      this.factory = factory;
      this.errorHandlerProvider = errorHandlerProvider;
   }

   public VcloudDirectorError parseVcloudDirectorErrorFromContent(HttpRequest request, HttpResponse response) {
      if (response.getPayload() == null)
         return null;
 
      String contentType = response.getPayload().getContentMetadata().getContentType();
      if (contentType == null || !contentType.toLowerCase().contains("vnd.vmware.vcloud.error+xml")) {
         // expected "application/vnd.vmware.vcloud.error+xml;version=1.5"
         return null;
      }

      try {
         VcloudDirectorError error = factory.create(errorHandlerProvider.get()).setContext(request).apply(response);
         return error;
      } catch (RuntimeException e) {
         logger.warn(e, "error parsing error");
         return null;
      }
   }
}
