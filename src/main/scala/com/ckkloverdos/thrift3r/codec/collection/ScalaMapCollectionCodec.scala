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

package com.ckkloverdos.thrift3r.codec.collection

import com.ckkloverdos.thrift3r.TTypeEnum
import com.ckkloverdos.thrift3r.codec.Codec
import com.ckkloverdos.thrift3r.collection.builder.CollectionBuilderFactory
import com.google.common.reflect.TypeToken
import scala.collection.GenMap
import org.apache.thrift.protocol.{TMap, TProtocol}
import scala.annotation.tailrec

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case class ScalaMapCollectionCodec[A, B, M](
  typeToken: TypeToken[GenMap[A, B]],
  keyCodec: Codec[A],
  valueCodec: Codec[B],
  meta: M,
  builderFactory: CollectionBuilderFactory[(A, B), GenMap[A, B], M]
) extends Codec[GenMap[A, B]] {

  def tTypeEnum = TTypeEnum.MAP

  def encode(protocol: TProtocol, map: GenMap[A, B]) {
    val isNull = map eq null
    val tMap = new TMap(
      keyCodec.tType,
      valueCodec.tType,
      if(isNull) 0 else map.size
    )

    protocol.writeMapBegin(tMap)
    if(!isNull) {
      for((k, v) ← map) {
        keyCodec.encode(protocol, k)
        valueCodec.encode(protocol, v)
      }
    }
    protocol.writeMapEnd()
  }

  def decode(protocol: TProtocol) = {
    val builder = builderFactory.newBuilder(meta)

    @tailrec def decodeElement(index: Int, size: Int) {
      if(index < size) {
        val key = keyCodec.decode(protocol)
        val value = valueCodec.decode(protocol)
        builder.add(key → value)

        decodeElement(index + 1, size)
      }
    }

    val tMap = protocol.readMapBegin()
    val size = tMap.size
    decodeElement(0, size)
    protocol.readMapEnd()

    builder.build
  }

}
