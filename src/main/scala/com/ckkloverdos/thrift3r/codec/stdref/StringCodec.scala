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
package stdref

import com.ckkloverdos.thrift3r.BinReprType
import com.ckkloverdos.thrift3r.protocol.Protocol
import com.google.common.reflect.TypeToken

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case object StringCodec extends Codec[String] with CodecToString {

  /**
   * The supported [[com.ckkloverdos.thrift3r.BinReprType]],
   * which is [[com.ckkloverdos.thrift3r.BinReprType#STRING]].
   */
  final def binReprType = BinReprType.STRING

  final def typeToken = new TypeToken[String]{}

  final def encode(protocol: Protocol, value: String) { protocol.writeString(value) }

  final def decode(protocol: Protocol) = protocol.readString()

  final def toDirectString(value: String) = value

  final def fromDirectString(value: String) = value
}
