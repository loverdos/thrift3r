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
package primitive

import com.ckkloverdos.thrift3r.TTypeEnum
import com.ckkloverdos.thrift3r.protocol.Protocol

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case object ShortCodec extends Codec[Short] with CodecToString {
  final def tTypeEnum = TTypeEnum.INT16

  final def typeToken = typeTokenOfClass(ShortClass)

  final def encode(protocol: Protocol, value: Short) { protocol.writeInt16(value) }

  final def decode(protocol: Protocol) = protocol.readInt16()

  final def toDirectString(value: Short) =  String.valueOf(value)

  final def fromDirectString(value: String) = value.toShort
}
