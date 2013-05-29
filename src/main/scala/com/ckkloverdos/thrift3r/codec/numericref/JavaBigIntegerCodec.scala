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

package com.ckkloverdos.thrift3r.codec
package numericref

import com.ckkloverdos.thrift3r.TTypeEnum
import com.ckkloverdos.thrift3r.protocol.Protocol
import com.google.common.reflect.TypeToken
import java.math.BigInteger

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
final case class JavaBigIntegerCodec(radix: Int) extends Codec[BigInteger] with CodecToString {
  def tTypeEnum = TTypeEnum.STRING

  def typeToken = new TypeToken[BigInteger]{}

  @inline def getValue(value: BigInteger) =
    value match {
      case null ⇒ "0"
      case _    ⇒ value.toString(radix)
    }

  def encode(protocol: Protocol, value: BigInteger) {
    val stringValue = getValue(value)

    protocol.writeString(stringValue)
  }

  def decode(protocol: Protocol) = {
    val stringValue = protocol.readString()
    new BigInteger(stringValue, radix)
  }

  override protected def extraToStringElements = List(radix)

  def toDirectString(value: BigInteger) = getValue(value)

  def fromDirectString(value: String) = new BigInteger(value, radix)
}
