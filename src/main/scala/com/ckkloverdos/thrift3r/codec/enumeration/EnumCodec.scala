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
package enumeration

import com.ckkloverdos.thrift3r.TTypeEnum
import com.ckkloverdos.thrift3r.protocol.Protocol
import java.util.Locale

/**
 * Simple Java enumerations are encoded as integers.
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case class EnumCodec[E <: Enum[_]](enumClass: Class[E]) extends Codec[E] with CodecToString {
  private[this] final val enumConstants = enumClass.getEnumConstants
  private[this] final val enumConstantsByName = enumConstants.map(e ⇒ (e.name().toLowerCase(Locale.US), e)).toMap

  // NOTE Not ENUM because TCompactProtocol/v0.9 throws:
  // NOTE java.lang.ArrayIndexOutOfBoundsException: 16
  // NOTE at org.apache.thrift.protocol.TCompactProtocol.getCompactType(TCompactProtocol.java:852)
  def tTypeEnum = TTypeEnum.INT32

  def typeToken = typeTokenOfClass(enumClass)

  def encode(protocol: Protocol, value: E) {
    val ordinal = value.ordinal()
    protocol.writeInt32(ordinal)
  }

  def decode(protocol: Protocol) = {
    val ordinal = protocol.readInt32()
    enumConstants(ordinal)
  }

  def toDirectString(value: E) = value.toString

  def fromDirectString(value: String) = enumConstantsByName(value.toLowerCase(Locale.US))
}
