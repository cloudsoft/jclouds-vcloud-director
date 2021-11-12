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
package org.jclouds.vcloud.director.v1_5.domain;

import com.google.common.base.MoreObjects;

/**
 * @see {@link org.jclouds.vcloud.director.v1_5.handlers.ErrorHandler} for code to parse, and thus instantiate, a vCD error.
 */
public class VcloudDirectorError {
   
   private String minorErrorCode;
   private String message;
   private String majorErrorCode;

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
            .add("minorErrorCode", minorErrorCode)
            .add("message", message)
            .add("majorErrorCode", majorErrorCode)
            .toString();
   }

   public void setMinorErrorCode(String val) {
      this.minorErrorCode = val;
   }

   public String getMinorErrorCode() {
      return minorErrorCode;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public String getMessage() {
      return message;
   }

   public String getMajorErrorCode() {
      return majorErrorCode;
   }

   public void setMajorErrorCode(String val) {
      this.majorErrorCode = val;
   }
}
