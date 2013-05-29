/*
 * Copyright (c) 2013 Christos KK Loverdos
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

package com.ckkloverdos.thrift3r;

import org.apache.thrift.protocol.TType;

/**
 * These are the types supported at the binary codec level.
 *
 * @see org.apache.thrift.protocol.TType
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
public enum TTypeEnum {
    VOID(TType.VOID),
    BOOL(TType.BOOL),
    INT8(TType.BYTE),
    INT16(TType.I16),
    INT32(TType.I32),
    INT64(TType.I64),
    FLOAT64(TType.DOUBLE),
    STRING(TType.STRING),
    SET(TType.SET),
    LIST(TType.LIST),
    MAP(TType.MAP),
    ENUM(TType.ENUM),
    STRUCT(TType.STRUCT);

    public final byte ttype;

    private TTypeEnum(byte ttype) {
        this.ttype = ttype;
    }

    public boolean hasDirectStringRepresentation() {
        switch(this) {
            case BOOL:
            case INT8:
            case INT16:
            case INT32:
            case INT64:
            case FLOAT64:
            case STRING:
            case ENUM:
                return true;
            default:
                return false;
        }
    }
}
