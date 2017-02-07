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
package org.jclouds.vcloud.director.v1_5.domain.dmtf.ovf;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Java class for Item element declaration.
 *
 * <pre>
 * &lt;element name="Item"&gt;
 *   &lt;complexType&gt;
 *     &lt;complexContent&gt;
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *         &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *         &lt;attribute name="order" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" /&gt;
 *         &lt;attribute name="startDelay" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" default="0" /&gt;
 *         &lt;attribute name="waitingForGuest" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *         &lt;attribute name="stopDelay" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" default="0" /&gt;
 *         &lt;attribute name="startAction" type="{http://www.w3.org/2001/XMLSchema}string" default="powerOn" /&gt;
 *         &lt;attribute name="stopAction" type="{http://www.w3.org/2001/XMLSchema}string" default="powerOff" /&gt;
 *         &lt;anyAttribute processContents='lax'/&gt;
 *       &lt;/restriction&gt;
 *     &lt;/complexContent&gt;
 *   &lt;/complexType&gt;
 * &lt;/element&gt;
 * </pre>
 */
@XmlType
@XmlRootElement(name = "Item")
public class StartupSectionItem extends Item {
   
   // TODO Builder

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      StartupSectionItem that = StartupSectionItem.class.cast(obj);
      return super.equals(that);
   }

}
