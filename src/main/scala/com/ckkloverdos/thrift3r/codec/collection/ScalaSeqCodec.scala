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

import com.ckkloverdos.thrift3r.BinReprType.LIST
import com.ckkloverdos.thrift3r.collection.builder.CollectionBuilderFactory
import com.ckkloverdos.thrift3r.protocol.Protocol
import com.ckkloverdos.thrift3r.protocol.helper.ProtocolHelpers
import com.google.common.reflect.TypeToken
import scala.collection.GenTraversable

/**
 * Codec for Scala Seqs.
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case class ScalaSeqCodec[A, M](
  typeToken: TypeToken[GenTraversable[A]],
  elementCodec: Codec[A],
  meta: M,
  builderFactory: CollectionBuilderFactory[A, GenTraversable[A], M]
) extends Codec[GenTraversable[A]] with CodecToString with UnsupportedDirectStringTransformations[GenTraversable[A]] {

  def binReprType = LIST

  override protected def extraToStringElements = List(elementCodec, meta, builderFactory)

  def encode(protocol: Protocol, value: GenTraversable[A]) {
    ProtocolHelpers.writeList(protocol, elementCodec, value)
  }

  def decode(protocol: Protocol) = {
    val builder = builderFactory.newBuilder(meta)
    ProtocolHelpers.readList[A](protocol, elementCodec, builder.add(_))
    builder.build
  }
}
