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

package com.ckkloverdos.thrift3r.helper

import com.ckkloverdos.thrift3r.codec.{UnsupportedDirectStringTransformations, Codec}
import com.google.common.reflect.TypeToken
import com.ckkloverdos.thrift3r.protocol.Protocol
import com.ckkloverdos.thrift3r.BinReprType

/**
 * Struct codec for string-based objects.
 *
 * This is useful for classes whose only attribute is a string. In such a case,
 * instead of encoding the `STRUCT` and then the `STRING`, we only encode the latter.
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
final case class StringBasedStructCodec[T](
  typeToken: TypeToken[T],
  toStringRepr: (T) ⇒ String,
  fromStringRepr: (String) ⇒ T
) extends Codec[T] {

  def binReprType = BinReprType.STRUCT

  def encode(protocol: Protocol, value: T) = protocol.writeString(toStringRepr(value))

  def decode(protocol: Protocol) = fromStringRepr(protocol.readString())

  def toDirectString(value: T) = toStringRepr(value)

  def fromDirectString(value: String) = fromStringRepr(value)

  override def hasDirectStringRepresentation = true // by design
}
