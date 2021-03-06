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
package misc

import com.ckkloverdos.thrift3r.BinReprType
import com.ckkloverdos.thrift3r.protocol.{UnsizedSetProtocol, SizedSetProtocol, Protocol}
import com.google.common.reflect.TypeToken
import com.ckkloverdos.thrift3r.protocol.helper.ProtocolHelpers

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
final case class OptionCodec[T](
  typeToken: TypeToken[Option[T]],
  elementCodec: Codec[T]
) extends Codec[Option[T]] with UnsupportedDirectStringTransformations[Option[T]] {

  def binReprType = BinReprType.OPTION

  def encode(protocol: Protocol, value: Option[T]) =
    protocol.getOptionProtocol.writeOption(elementCodec, value)

  def decode(protocol: Protocol) =
    protocol.getOptionProtocol.readOption(elementCodec)
}
