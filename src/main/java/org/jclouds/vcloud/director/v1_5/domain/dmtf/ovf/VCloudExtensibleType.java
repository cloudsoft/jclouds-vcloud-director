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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VCloudExtensibleType", propOrder = {"vCloudExtension"})
public abstract class VCloudExtensibleType
{

  @XmlElement(name = "VCloudExtension")
  protected List<VCloudExtensionType> vCloudExtension;

  @XmlAnyAttribute
  private Map<QName, String> otherAttributes = new HashMap<QName, String>();

  public List<VCloudExtensionType> getVCloudExtension()
  {
    if (this.vCloudExtension == null) {
      this.vCloudExtension = new ArrayList<VCloudExtensionType>();
    }
    return this.vCloudExtension;
  }

  public Map<QName, String> getOtherAttributes()
  {
    return this.otherAttributes;
  }
}
