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

package com.ckkloverdos.thrift3r.protocol.thrift;

import com.ckkloverdos.thrift3r.BinReprType;
import org.apache.thrift.protocol.TType;

/**
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
public final class TTypes {
    private TTypes() {}

    public static byte toTType(BinReprType brType) {
        switch(brType) {
            case VOID: return TType.VOID;
            case BOOL: return TType.BOOL;
            case INT8: return TType.BYTE;
            case INT16: return TType.I16;
            case INT32: return TType.I32;
            case INT64: return TType.I64;
            case FLOAT64: return TType.DOUBLE;
            case STRING: return TType.STRING;
            case SET: return TType.SET;
            case LIST: return TType.LIST;
            case MAP: return TType.MAP;
            case ENUM: return TType.ENUM;
            case STRUCT: return TType.STRUCT;
            default:
                throw new RuntimeException("Unknown " + brType);
        }
    }
}
