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

import org.jclouds.http.functions.ParseSax;
import org.jclouds.vcloud.director.v1_5.domain.VcloudDirectorError;
import org.xml.sax.Attributes;

/**
 * Parses the error from the vCloudDirectdor API. For example:
 * 
 * <pre>
 * {@code
 * <Error xmlns="http://www.vmware.com/vcloud/v1.5" 
 *     minorErrorCode="OPERATION_LIMITS_EXCEEDED" 
 *     message="The maximum number of simultaneous operations for user &quot;myname&quot; on organization &quot;my-org&quot; has been reached." 
 *     majorErrorCode="400" 
 *     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
 *     xsi:schemaLocation="http://www.vmware.com/vcloud/v1.5 http://acme.com/api/v1.5/schema/master.xsd">
 * </Error>
 * }
 * </pre>
 */
public class ErrorHandler extends ParseSax.HandlerWithResult<VcloudDirectorError> {

   private VcloudDirectorError error = new VcloudDirectorError();

   @Override
   public VcloudDirectorError getResult() {
      return error;
   }
   
   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes) {
      if (qName.equals("Error")) {
         for (int i = 0; i < attributes.getLength(); i++) {
            String attribQname = attributes.getQName(i);
            String attribValue = attributes.getValue(i);
            if (attribValue != null) {
               if (attribQname.equals("minorErrorCode")) {
                  error.setMinorErrorCode(attribValue.trim());
               } else if (attribQname.equals("message")) {
                  error.setMessage(attribValue.trim());
               } else if (attribQname.equalsIgnoreCase("majorErrorCode")) {
                  error.setMajorErrorCode(attribValue.trim());
               }
            }
         }
      }
   }
}
