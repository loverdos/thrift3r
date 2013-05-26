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
package codec.enumeration

import com.ckkloverdos.thrift3r.TTypeEnum
import com.ckkloverdos.thrift3r.codec.{CodecToString, Codec}
import org.apache.thrift.protocol.TProtocol

/**
 * Simple Java enumerations are encoded as integers.
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case class EnumCodec[E <: Enum[_]](enumClass: Class[E]) extends Codec[E] with CodecToString {
  private[this] final val enumConstants = enumClass.getEnumConstants

  // NOTE Not ENUM because TCompactProtocol/v0.9 throws:
  // NOTE java.lang.ArrayIndexOutOfBoundsException: 16
  // NOTE at org.apache.thrift.protocol.TCompactProtocol.getCompactType(TCompactProtocol.java:852)
  def tTypeEnum = TTypeEnum.INT32

  def typeToken = typeTokenOfClass(enumClass)

  def encode(protocol: TProtocol, value: E) {
    val ordinal = value.ordinal()
    protocol.writeI32(ordinal)
  }

  def decode(protocol: TProtocol) = {
    val ordinal = protocol.readI32()
    enumConstants(ordinal)
  }
}
