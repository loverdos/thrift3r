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
package collection

import com.ckkloverdos.thrift3r.TTypeEnum
import com.ckkloverdos.thrift3r.collection.builder.CollectionBuilderFactory
import com.ckkloverdos.thrift3r.protocol.Protocol
import com.ckkloverdos.thrift3r.protocol.helper.ProtocolHelpers
import com.google.common.reflect.TypeToken
import scala.collection.GenMap

/**
 * Codec for Scala Maps.
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case class ScalaMapCodec[A, B, M](
  typeToken: TypeToken[GenMap[A, B]],
  keyCodec: Codec[A],
  valueCodec: Codec[B],
  meta: M,
  builderFactory: CollectionBuilderFactory[(A, B), GenMap[A, B], M]
) extends Codec[GenMap[A, B]] with UnsupportedDirectStringTransformations[GenMap[A, B]] {

  def tTypeEnum = TTypeEnum.MAP

  def encode(protocol: Protocol, map: GenMap[A, B]) {
    ProtocolHelpers.writeMap(protocol, keyCodec, valueCodec, map)
  }

  def decode(protocol: Protocol) = {
    val builder = builderFactory.newBuilder(meta)
    ProtocolHelpers.readMap[A, B](protocol, keyCodec, valueCodec, builder.add(_))
    builder.build
  }
}
