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

import com.ckkloverdos.thrift3r.BinReprType
import com.ckkloverdos.thrift3r.protocol.Protocol

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case object FloatCodec extends Codec[Float] with CodecToString {
  final def binReprType = BinReprType.FLOAT32

  final def typeToken = typeTokenOfClass(FloatClass)

  final def encode(protocol: Protocol, value: Float) { protocol.writeFloat32(value) }

  final def decode(protocol: Protocol) = protocol.readFloat32()

  final def toDirectString(value: Float) = String.valueOf(value)

  final def fromDirectString(value: String) = value.toFloat
}
