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

import com.ckkloverdos.thrift3r.TTypeEnum
import com.ckkloverdos.thrift3r.protocol.Protocol

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case object BooleanRefCodec extends Codec[java.lang.Boolean] with CodecToString {
  final def tTypeEnum = TTypeEnum.BOOL

  final def typeToken = typeTokenOfClass(BooleanRefClass)

  @inline final def getValue(value: java.lang.Boolean) =
    value match {
      case null ⇒ false
      case _    ⇒ value.booleanValue()
    }

  final def encode(protocol: Protocol, value: java.lang.Boolean) {
    protocol.writeBool(getValue(value))
  }

  final def decode(protocol: Protocol) = java.lang.Boolean.valueOf(protocol.readBool())

  final def toDirectString(value: java.lang.Boolean) = String.valueOf(getValue(value))

  final def fromDirectString(value: String) = java.lang.Boolean.valueOf(value)
}
