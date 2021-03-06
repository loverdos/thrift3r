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

import com.ckkloverdos.thrift3r.BinReprType
import com.ckkloverdos.thrift3r.protocol.Protocol
import java.util.Locale
import com.ckkloverdos.thrift3r.protocol.helper.ProtocolHelpers

/**
 * Simple Java enumerations are encoded as integers.
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
final case class EnumCodec[E <: Enum[_]](enumClass: Class[E]) extends Codec[E] with CodecToString {
  private[this] final val enumConstants = enumClass.getEnumConstants
  private[this] final val enumConstantsByName = enumConstants.map(e ⇒ (e.name(), e)).toMap
  private[this] final val enumConstantsByNameLowerCase = enumConstants.map(e ⇒ (e.name().toLowerCase(Locale.US), e)).toMap

  // NOTE Not ENUM because TCompactProtocol/v0.9 throws:
  // NOTE java.lang.ArrayIndexOutOfBoundsException: 16
  // NOTE at org.apache.thrift.protocol.TCompactProtocol.getCompactType(TCompactProtocol.java:852)
  def binReprType = BinReprType.INT32

  def typeToken = typeTokenOfClass(enumClass)

  def encode(protocol: Protocol, value: E) =
    ProtocolHelpers.writeEnum(protocol, value)

  def decode(protocol: Protocol) =
    ProtocolHelpers.readEnum(protocol, this)

  def fromOrdinal(ordinal: Int) = enumConstants(ordinal)

  def fromName(name: String) = enumConstantsByName(name)

  def fromNameIgnoringCase(name: String) = enumConstantsByNameLowerCase(name.toLowerCase(Locale.US))

  def toDirectString(value: E) = value.toString

  @inline def fromDirectString(value: String) = fromNameIgnoringCase(value)
}
