/*
 * Copyright 2015-2016 Dark Phoenixs (Open-Source Organization).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.json.bvt.support.spring.mock.entity;

/**
 * <p>FastJsonEnumTestVO</p>
 * <p></p>
 *
 * @author Victor.Zxy
 * @version 1.0
 * @since 2016年8月8日
 */
public class FastJsonEnumTestVO {

    public enum SmsType {

        USER_REGISTER, USER_LOGIN, USER_LOGOUT;
    }

    public class Packet {

        private SmsType smsType;

        /**
         * @return the smsType
         */
        public SmsType getSmsType() {
            return smsType;
        }

        /**
         * @param smsType the smsType to set
         */
        public void setSmsType(SmsType smsType) {
            this.smsType = smsType;
        }
    }

    private Packet packet;

    /**
     * @return the packet
     */
    public Packet getPacket() {
        return packet;
    }

    /**
     * @param packet the packet to set
     */
    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
