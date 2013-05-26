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
package codec.primitiveref

import com.ckkloverdos.thrift3r.TTypeEnum
import com.ckkloverdos.thrift3r.codec.{CodecToString, Codec}
import org.apache.thrift.protocol.TProtocol

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case object ByteRefCodec extends Codec[java.lang.Byte] with CodecToString {
  final def tTypeEnum = TTypeEnum.INT8

  final def typeToken = typeTokenOfClass(ByteRefClass)

  final def encode(protocol: TProtocol, value: java.lang.Byte) {
    val byteValue = value match {
      case null ⇒ 0.toByte
      case _    ⇒ value.byteValue()
    }

    protocol.writeByte(byteValue)
  }

  final def decode(protocol: TProtocol) = java.lang.Byte.valueOf(protocol.readByte())
}
