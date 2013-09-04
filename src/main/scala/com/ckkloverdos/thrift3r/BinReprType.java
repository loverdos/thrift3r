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

/**
 * Binary representation types: these are the types supported at the binary codec level.
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
public enum BinReprType {
    VOID   ((byte)  0),
    BOOL   ((byte)  1), // Java: boolean
    INT8   ((byte)  2), // Java: byte
    INT16  ((byte)  3), // Java: short
    INT32  ((byte)  4), // Java: int
    INT64  ((byte)  5), // Java: long
    FLOAT32((byte)  6), // Java: float
    FLOAT64((byte)  7), // Java: double
    STRING ((byte)  10),
    SET    ((byte)  20),
    LIST   ((byte)  21),
    MAP    ((byte)  22),
    OPTION ((byte)  23), // Yes, we support options natively
    ENUM   ((byte)  30), // Simple, Java-like enums
    STRUCT ((byte)  40);

    public final byte brType;

    private BinReprType(byte brType) {
        this.brType = brType;
    }

    public boolean hasDirectStringRepresentation() {
        switch(this) {
            case BOOL:
            case INT8:
            case INT16:
            case INT32:
            case INT64:
            case FLOAT32:
            case FLOAT64:
            case STRING:
            case ENUM:
                return true;
            default:
                return false;
        }
    }
}
