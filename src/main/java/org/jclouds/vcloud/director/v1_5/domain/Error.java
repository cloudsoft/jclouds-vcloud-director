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
import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;

import javax.annotation.Resource;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.jclouds.logging.Logger;
import org.jclouds.vcloud.director.v1_5.VCloudDirectorMediaType;


import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * The standard error message type used in the vCloud REST API.
 *
 * <pre>
 * &lt;xs:complexType name="ErrorType" /&gt;
 * </pre>
 */
@XmlRootElement(name = "Error")
public class Error {

   @Resource
   protected static Logger logger = Logger.NULL;

   @XmlType
   @XmlEnum(Integer.class)
   public static enum Code {
      @XmlEnumValue("200") OK(200),
      @XmlEnumValue("201") CREATED(201),
      @XmlEnumValue("202") ACCEPTED(202),
      @XmlEnumValue("204") NO_CONTENT(204),
      @XmlEnumValue("303") SEE_OTHER(303),
      @XmlEnumValue("400") BAD_REQUEST(400),
      @XmlEnumValue("401") UNAUTHORIZED(401),
      @XmlEnumValue("403") FORBIDDEN(403), // NOTE also means 'not found' for entities
      @XmlEnumValue("404") NOT_FOUND(404),
      @XmlEnumValue("405") NOT_ALLOWED(405),
      @XmlEnumValue("500") INTERNAL_ERROR(500),
      @XmlEnumValue("501") NOT_IMPLEMENTED(501),
      @XmlEnumValue("503") UNAVAILABLE(503),
      UNRECOGNIZED(-1);

      private final Integer majorErrorCode;
      
      private Code(Integer majorErrorCode) {
         this.majorErrorCode = majorErrorCode;
      }

      public Integer getCode() {
         return majorErrorCode;
      }

      public static Code fromCode(final int majorErrorCode) {
         Optional<Code> found = Iterables.tryFind(Arrays.asList(values()), new Predicate<Code>() {
            @Override
            public boolean apply(Code code) {
               return code.getCode().equals(majorErrorCode);
            }
         });
         if (found.isPresent()) {
            return found.get();
         } else {
            logger.warn("Unrecognized major error code '%d'", majorErrorCode);
            return UNRECOGNIZED;
         }
      }
   }

   public static final String MEDIA_TYPE = VCloudDirectorMediaType.ERROR;

   public static Builder builder() {
      return new Builder();
   }

   public Builder toBuilder() {
      return new Builder().fromError(this);
   }

   public static class Builder {

      protected String message;
      protected int majorErrorCode;
      protected String minorErrorCode;
      protected String vendorSpecificErrorCode;
      protected String stackTrace;

      /**
       * @see Error#getMessage()
       */
      public Builder message(String message) {
         this.message = message;
         return this;
      }

      /**
       * @see Error#getMajorErrorCode()
       */
      public Builder majorErrorCode(int majorErrorCode) {
         this.majorErrorCode = majorErrorCode;
         return this;
      }

      /**
       * @see Error#getMinorErrorCode()
       */
      public Builder minorErrorCode(String minorErrorCode) {
         this.minorErrorCode = minorErrorCode;
         return this;
      }

      /**
       * @see Error#getVendorSpecificErrorCode()
       */
      public Builder vendorSpecificErrorCode(String vendorSpecificErrorCode) {
         this.vendorSpecificErrorCode = vendorSpecificErrorCode;
         return this;
      }

      /**
       * @see Error#getStackTrace()
       */
      public Builder stackTrace(String stackTrace) {
         this.stackTrace = stackTrace;
         return this;
      }

      public Error build() {
         return new Error(message, majorErrorCode, minorErrorCode, vendorSpecificErrorCode, stackTrace);
      }

      public Builder fromError(Error in) {
         return message(in.getMessage())
               .majorErrorCode(in.getMajorErrorCode())
               .minorErrorCode(in.getMinorErrorCode())
               .vendorSpecificErrorCode(in.getVendorSpecificErrorCode())
               .stackTrace(in.getStackTrace());
      }
   }

   @XmlAttribute
   private String message;
   @XmlAttribute
   private Integer majorErrorCode;
   @XmlAttribute
   private String minorErrorCode;
   @XmlAttribute
   private String vendorSpecificErrorCode;
   @XmlAttribute
   private String stackTrace;

   private Error(String message, Integer majorErrorCode, String minorErrorCode, String vendorSpecificErrorCode, String stackTrace) {
      this.message = checkNotNull(message, "message");
      this.majorErrorCode = checkNotNull(majorErrorCode, "majorErrorCode");
      this.minorErrorCode = checkNotNull(minorErrorCode, "minorErrorCode");
      this.vendorSpecificErrorCode = vendorSpecificErrorCode;
      this.stackTrace = stackTrace;
   }

   private Error() {
      // For JAXB
   }

   /**
    * An one line, human-readable message describing the error that occurred.
    */
   public String getMessage() {
      return message;
   }

   /**
    * The class of the error. Matches the HTTP status code.
    */
   public Integer getMajorErrorCode() {
      return majorErrorCode;
   }

   /**
    * Specific API error code.
    *
    * For example - can indicate that vApp power on failed by some reason.
    */
   public String getMinorErrorCode() {
      return minorErrorCode;
   }

   /**
    * A vendor/implementation specific error code that point to specific
    * modules/parts of the code and can make problem diagnostics easier.
    */
   public String getVendorSpecificErrorCode() {
      return vendorSpecificErrorCode;
   }

   /**
    * The stack trace of the exception which when examined might make problem
    * diagnostics easier.
    */
   public String getStackTrace() {
      return stackTrace;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      Error that = (Error) o;
      return equal(this.message, that.message) &&
            equal(this.majorErrorCode, that.majorErrorCode) &&
            equal(this.minorErrorCode, that.minorErrorCode) &&
            equal(this.vendorSpecificErrorCode, that.vendorSpecificErrorCode) &&
            equal(this.stackTrace, that.stackTrace);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(message, majorErrorCode, minorErrorCode, vendorSpecificErrorCode, stackTrace);
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper("")
            .add("message", message).add("majorErrorCode", majorErrorCode).add("minorErrorCode", minorErrorCode)
            .add("vendorSpecificErrorCode", vendorSpecificErrorCode).add("stackTrace", stackTrace)
            .toString();
   }
}
