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
final case class ScalaBigIntCodec(radix: Int) extends Codec[BigInt] with CodecToString {
  def tTypeEnum = TTypeEnum.STRING

  def typeToken = new TypeToken[BigInt]{}

  @inline def getValue(value: BigInt): String = {
    value match {
      case null ⇒ "0"
      case _    ⇒ value.toString(radix)
    }
  }

  def encode(protocol: Protocol, value: BigInt) {
    val stringValue = getValue(value)

    protocol.writeString(stringValue)
  }

  def decode(protocol: Protocol) = {
    val stringValue = protocol.readString()
    val javaBigInt = new BigInteger(stringValue, radix)
    new BigInt(javaBigInt)
  }

  override protected def extraToStringElements = List(radix)

  def toDirectString(value: BigInt) = getValue(value)

  def fromDirectString(value: String) = new BigInt(new BigInteger(value, radix))
}
