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

import com.ckkloverdos.thrift3r.BinReprType.{LIST, SET}
import com.ckkloverdos.thrift3r.collection.builder.CollectionBuilderFactory
import com.ckkloverdos.thrift3r.protocol.Protocol
import com.ckkloverdos.thrift3r.protocol.helper.ProtocolHelpers
import com.google.common.reflect.TypeToken
import scala.collection.GenTraversable
import com.ckkloverdos.thrift3r.BinReprType

/**
 * Codec for Scala Seqs and Sets.
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case class ScalaSeqSetCodec[A, M](
  binReprType: BinReprType,
  typeToken: TypeToken[GenTraversable[A]],
  elementCodec: Codec[A],
  meta: M,
  builderFactory: CollectionBuilderFactory[A, GenTraversable[A], M]
) extends Codec[GenTraversable[A]] with CodecToString with UnsupportedDirectStringTransformations[GenTraversable[A]] {

  require(
    binReprType == LIST || binReprType == SET,
    "tTypeENum = %s, should have been one of %s, %s".format(binReprType, LIST, SET)
  )

  override protected def extraToStringElements = List(elementCodec, meta, builderFactory)

  def encode(protocol: Protocol, value: GenTraversable[A]) {
    binReprType match {
      case LIST ⇒
        ProtocolHelpers.writeList(protocol, elementCodec, value)

      case SET ⇒
        ProtocolHelpers.writeSet(protocol, elementCodec, value)
    }
  }

  def decode(protocol: Protocol) = {
    val builder = builderFactory.newBuilder(meta)

    binReprType match {
      case LIST ⇒
        ProtocolHelpers.readList[A](protocol, elementCodec, builder.add(_))

      case SET ⇒
        ProtocolHelpers.readSet[A](protocol, elementCodec, builder.add(_))
    }

    builder.build
  }
}
