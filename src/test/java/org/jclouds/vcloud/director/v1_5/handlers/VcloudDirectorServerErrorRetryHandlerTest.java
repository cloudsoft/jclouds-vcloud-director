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

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.testng.Assert.assertFalse;

import org.jclouds.http.HttpCommand;
import org.jclouds.http.HttpResponse;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;

/**
 * Tests behavior of {@code VcloudDirectorServerErrorRetryHandler}
 */
@Test(groups = "unit", testName = "VcloudDirectorServerErrorRetryHandlerTest")
public class VcloudDirectorServerErrorRetryHandlerTest {
   @Test
   public void testUnknown500DoesNotRetry() {
      VcloudDirectorUtils utils = createMock(VcloudDirectorUtils.class);
      HttpCommand command = createMock(HttpCommand.class);

      replay(utils, command);

      VcloudDirectorServerErrorRetryHandler retry = new VcloudDirectorServerErrorRetryHandler(utils,
            ImmutableSet.<String> of());

      assertFalse(retry.shouldRetryRequest(command, HttpResponse.builder().statusCode(INTERNAL_SERVER_ERROR.getStatusCode()).build()));

      verify(utils, command);
   }
}
