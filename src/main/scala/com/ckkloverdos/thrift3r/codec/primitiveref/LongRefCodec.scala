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
object LongRefCodec extends Codec[java.lang.Long] with CodecToString {
  final def binReprType = BinReprType.INT64

  final def typeToken = typeTokenOfClass(LongRefClass)

  @inline final def getValue(value: java.lang.Long): Long = {
    value match {
      case null ⇒ 0L
      case _    ⇒ value.longValue()
    }
  }

  final def encode(protocol: Protocol, value: java.lang.Long) {
    val longValue = getValue(value)

    protocol.writeInt64(longValue)
  }

  final  def decode(protocol: Protocol) = java.lang.Long.valueOf(protocol.readInt64())

  final def toDirectString(value: java.lang.Long) = String.valueOf(getValue(value))

  final def fromDirectString(value: String) = java.lang.Long.valueOf(value)
}
