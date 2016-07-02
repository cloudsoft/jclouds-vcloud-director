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

import java.util.Set;

import org.jclouds.http.HttpCommand;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.annotation.ServerError;
import org.jclouds.http.handlers.BackoffLimitedRetryHandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Handles Retryable responses with error codes in the 5xx range
 * 
 * See org.jclouds.aws.handlers.AWSServerErrorRetryHandler for the pattern used here. 
 */
@Singleton
public class VcloudDirectorServerErrorRetryHandler extends BackoffLimitedRetryHandler {

   @Inject
   public VcloudDirectorServerErrorRetryHandler(VcloudDirectorUtils utils, @ServerError Set<String> retryableServerCodes) {
      if (!retryableServerCodes.isEmpty()) {
         throw new IllegalStateException("retryable server codes not supported, but given " + retryableServerCodes);
      }
   }

   @Override
   public boolean shouldRetryRequest(HttpCommand command, HttpResponse response) {
      return false;
   }
}
