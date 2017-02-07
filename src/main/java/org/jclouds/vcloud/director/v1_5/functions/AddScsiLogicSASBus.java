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

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import org.jclouds.vcloud.director.v1_5.domain.RasdItemsList;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.RasdItem;
import org.jclouds.vcloud.director.v1_5.domain.dmtf.cim.ResourceAllocationSettingData;

public class AddScsiLogicSASBus {
    static final Ordering<RasdItem> BY_ADDRESS_ORDERING = new Ordering<RasdItem>() {
        @Override
        public int compare(RasdItem left, RasdItem right) {
            if (left.getAddress() == null && right.getAddress() == null) {
                return 0;
            } else if (left.getAddress() == null) {
                return -1;
            } else if (right.getAddress() == null) {
                return 1;
            }
            Integer leftParent = Integer.parseInt(left.getAddress());
            Integer rightParent = Integer.parseInt(right.getAddress());
            return leftParent.compareTo(rightParent);
        }
    };
    public RasdItemsList addScsiLogicSASBus(RasdItemsList virtualHardwareSectionDisks) {
        RasdItemsList result = RasdItemsList.builder().fromRasdItemsList(virtualHardwareSectionDisks)
                .items(Lists.<RasdItem>newLinkedList())
                .build();
        for (RasdItem item : virtualHardwareSectionDisks) {
            result.add(item);
        }

        Iterable<RasdItem> existingScsiBus = Iterables.filter(result, new Predicate<RasdItem>() {
            @Override public boolean apply(RasdItem rasdItem) {
                return ResourceAllocationSettingData.ResourceType.PARALLEL_SCSI_HBA.equals(rasdItem.getResourceType());
            }
        });
        int size = Iterables.size(existingScsiBus);
        Preconditions.checkArgument(size == 1, "Adding new bus is implemented only for machines which have at least one scsi bus.");
        RasdItem maxBus = BY_ADDRESS_ORDERING.max(existingScsiBus);
        Integer address = Integer.parseInt(maxBus.getAddress()) + 1;
        Integer instanceId = Integer.parseInt(maxBus.getInstanceID()) + 1;
        RasdItem newBus = RasdItem.builder()
                .fromRasdItem(maxBus) // Copy fields from max Bus:
                // and Description
                // and ResourceType
                // and not needed parent
                .resourceSubType("lsilogicsas")
                .address("" + address)
                .instanceID("" + instanceId)
                .elementName("SCSI Controller " + address)
                .build();

        result.add(result.size() - 1, newBus);

        int scsiControllerIdx = result.indexOf(
                Iterables.getLast(Iterables.filter(result, NewScsiLogicSASDisk.SCSI_LSILOGICSAS_PREDICATE)));
        int ideControllerIdx = Iterables.indexOf(result, new Predicate<RasdItem>() {
                    @Override
                    public boolean apply(RasdItem input) {
                        return input.getResourceType().equals(ResourceAllocationSettingData.ResourceType.IDE_CONTROLLER);
                    }
                });
        Preconditions.checkArgument(scsiControllerIdx < ideControllerIdx);
        Preconditions.checkArgument(ideControllerIdx == result.size() - 1, "IDE Controller should be at last place.");
        RasdItem ideController = result.get(ideControllerIdx);
        ideController = RasdItem.builder().fromRasdItem(ideController).instanceID("" + (Integer.parseInt(ideController.getInstanceID()) + 1)).build();
        result.remove(ideControllerIdx);
        result.add(ideController);
        return result;
    }
}
