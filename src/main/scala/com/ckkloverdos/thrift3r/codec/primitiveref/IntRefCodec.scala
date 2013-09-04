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

package com.ckkloverdos.thrift3r
package codec
package primitiveref

import com.ckkloverdos.thrift3r.BinReprType
import com.ckkloverdos.thrift3r.protocol.Protocol

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case object IntRefCodec extends Codec[java.lang.Integer] with CodecToString {

  final def binReprType = BinReprType.INT32

  final def typeToken = typeTokenOfClass(IntRefClass)

  @inline final def getValue(value: java.lang.Integer): Int = {
    value match {
      case null ⇒ 0
      case _ ⇒ value.intValue()
    }
  }

  final def encode(protocol: Protocol, value: java.lang.Integer) {
    val intValue = getValue(value)

    protocol.writeInt32(intValue)
  }

  final def decode(protocol: Protocol) = java.lang.Integer.valueOf(protocol.readInt32())

  final def toDirectString(value: java.lang.Integer) = String.valueOf(getValue(value))

  final def fromDirectString(value: String) = java.lang.Integer.valueOf(value)
}
