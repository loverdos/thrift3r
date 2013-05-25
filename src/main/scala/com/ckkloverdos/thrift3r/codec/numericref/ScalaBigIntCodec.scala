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

package com.ckkloverdos.thrift3r.codec.numericref

import com.ckkloverdos.thrift3r.TTypeEnum
import com.ckkloverdos.thrift3r.codec.{CodecToString, Codec}
import java.math.BigInteger
import org.apache.thrift.protocol.TProtocol
import com.google.common.reflect.TypeToken

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case class ScalaBigIntCodec(radix: Int) extends Codec[BigInt] with CodecToString {
  final def tTypeEnum = TTypeEnum.STRING

  final def typeToken = new TypeToken[BigInt]{}

  final def encode(protocol: TProtocol, value: BigInt) {
    val stringValue = value match {
      case null ⇒ "0"
      case _    ⇒ value.toString(radix)
    }

    protocol.writeString(stringValue)
  }

  final def decode(protocol: TProtocol) = {
    val stringValue = protocol.readString()
    val javaBigInt = new BigInteger(stringValue, radix)
    new BigInt(javaBigInt)
  }

  override protected def extraToStringElements = List(radix)
}
