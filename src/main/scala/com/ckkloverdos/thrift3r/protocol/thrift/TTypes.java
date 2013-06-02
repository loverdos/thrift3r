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

import java.util.EnumMap;

/**
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
public final class TTypes {
    private TTypes() {}

    public static final EnumMap<BinReprType, Byte> BinReprTypeMap = new EnumMap<BinReprType, Byte>(BinReprType.class);
    static {
        BinReprTypeMap.put(BinReprType.VOID, TType.VOID);
        BinReprTypeMap.put(BinReprType.BOOL, TType.BOOL);
        BinReprTypeMap.put(BinReprType.INT8, TType.BYTE);
        BinReprTypeMap.put(BinReprType.INT16, TType.I16);
        BinReprTypeMap.put(BinReprType.INT32, TType.I32);
        BinReprTypeMap.put(BinReprType.INT64, TType.I64);
        BinReprTypeMap.put(BinReprType.FLOAT32, TType.DOUBLE);
        BinReprTypeMap.put(BinReprType.FLOAT64, TType.DOUBLE);
        BinReprTypeMap.put(BinReprType.STRING, TType.STRING);
        BinReprTypeMap.put(BinReprType.SET, TType.SET);
        BinReprTypeMap.put(BinReprType.LIST, TType.LIST);
        BinReprTypeMap.put(BinReprType.MAP, TType.MAP);
        BinReprTypeMap.put(BinReprType.OPTION, TType.SET);
        BinReprTypeMap.put(BinReprType.ENUM, TType.ENUM);
        BinReprTypeMap.put(BinReprType.STRUCT, TType.STRUCT);
    }

    public static byte toTType(BinReprType brType) {
        final Byte value = BinReprTypeMap.get(brType);
        if(value == null) {
            throw new RuntimeException("Unknown " + brType);
        }
        return value;
    }
}
