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

package com.ckkloverdos.thrift3r.codec.primitiveref

import com.ckkloverdos.thrift3r.TTypeEnum
import com.ckkloverdos.thrift3r.codec.{CodecToString, Codec}
import org.apache.thrift.protocol.TProtocol
import com.google.common.reflect.TypeToken

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
object LongRefCodec extends Codec[java.lang.Long] with CodecToString {
  final def tTypeEnum = TTypeEnum.INT64

  final def typeToken = new TypeToken[java.lang.Long]{}

  final def encode(protocol: TProtocol, value: java.lang.Long) {
    val longValue = value match {
      case null ⇒ 0L
      case _    ⇒ value.longValue()
    }

    protocol.writeI64(longValue)
  }

  final  def decode(protocol: TProtocol) = java.lang.Long.valueOf(protocol.readI64())
}
