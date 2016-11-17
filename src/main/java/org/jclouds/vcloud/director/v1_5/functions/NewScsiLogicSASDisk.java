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
package org.jclouds.vcloud.director.v1_5.functions;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
//import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import org.jclouds.vcloud.director.v1_5.domain.RasdItemsList;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.RasdItem;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.cim.CimString;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.cim.ResourceAllocationSettingData;

import javax.inject.Singleton;
import javax.xml.namespace.QName;

import static org.jclouds.vcloud.director.v1_5.domain.dmtf.cim.ResourceAllocationSettingData.ResourceType;

/**
 * Creates the next unique HDD RasdItem from RasdItemList.
 */
@Singleton
public final class NewScsiLogicSASDisk implements Function<RasdItemsList, RasdItem> {
    public static final Predicate<RasdItem> SCSI_LSILOGICSAS_PREDICATE = new Predicate<RasdItem>() {
        @Override
        public boolean apply(RasdItem input) {
            return input.getResourceType().equals(ResourceAllocationSettingData.ResourceType.PARALLEL_SCSI_HBA)
                    && input.getResourceSubType().equals("lsilogicsas");
        }
    };

    static final Ordering<RasdItem> BY_ADDRESS_ON_PARENT_ORDERING = new Ordering<RasdItem>() {
        public int compare(RasdItem left, RasdItem right) {
            if (left.getAddressOnParent() == null) {
                return -1;
            }
            if (right.getAddressOnParent() == null) {
                return 1;
            }
            Integer leftParent = Integer.parseInt(left.getAddressOnParent());
            Integer rightParent = Integer.parseInt(right.getAddressOnParent());
            return leftParent.compareTo(rightParent);
        }
    };

   @Override
   public RasdItem apply(RasdItemsList virtualHardwareSectionDiskItems) {
       Iterable<RasdItem> scsiLogicSasBuses = Iterables.filter(virtualHardwareSectionDiskItems, SCSI_LSILOGICSAS_PREDICATE);
       Preconditions.checkArgument(scsiLogicSasBuses.iterator().hasNext(), "New disks should be invoked when there is a SCSI Logic SAS controller.");
       final RasdItem scsiLogicSas = Iterables.getOnlyElement(scsiLogicSasBuses);

       Iterable <RasdItem> existingDisks = Iterables.filter(virtualHardwareSectionDiskItems, new Predicate<RasdItem>() {
           @Override public boolean apply(RasdItem rasdItem) {
               return ResourceType.DISK_DRIVE.equals(rasdItem.getResourceType())
                       && rasdItem.getParent().equals(scsiLogicSas.getInstanceID());
           }
       });

       if (!existingDisks.iterator().hasNext()) {
           Iterable<RasdItem> diskDrives = Iterables.filter(virtualHardwareSectionDiskItems, new Predicate<RasdItem>() {
               @Override
               public boolean apply(RasdItem input) {
                   return input.getResourceType().equals(ResourceType.DISK_DRIVE);
               }
           });
           RasdItem newDisk = RasdItem.builder()
                   .addressOnParent("0")
                   .description("Hard Disk")
                   .elementName("" + (Iterables.size(diskDrives) + 1))
                   .hostResource(new CimString("HostResource", ImmutableMap.of(
                           new QName("http://www.vmware.com/vcloud/v1.5", "capacity"), "1024",
                           new QName("http://www.vmware.com/vcloud/v1.5", "busSubType"), "lsilogicsas",
                           new QName("http://www.vmware.com/vcloud/v1.5", "busType"), "6"
                           )))
                   .instanceID("2016")
                   .parent(scsiLogicSas.getInstanceID())
                   .resourceType(ResourceType.DISK_DRIVE)
                   .build();
           return newDisk;
       } else {
           RasdItem lastDisk = BY_ADDRESS_ON_PARENT_ORDERING.max(existingDisks);

           Integer addressOnParent = Integer.parseInt(lastDisk.getAddressOnParent()) + 1;
           Integer instanceId = Integer.parseInt(lastDisk.getInstanceID()) + 1;

           return RasdItem.builder()
                   .fromRasdItem(lastDisk) // The same AddressOnParent (SCSI Controller)
                   // and Description
                   // and ResourceType
                   // and HostResource
                   // and not needed parent
                   .addressOnParent("" + addressOnParent)
                   .instanceID("" + instanceId)
    //               .hostResource(hostResource)
                   .elementName("Hard Disk " + (addressOnParent + 1))
                   .build();
       }
   }
}
