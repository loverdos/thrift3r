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
package collection

import com.ckkloverdos.thrift3r.TTypeEnum
import com.ckkloverdos.thrift3r.TTypeEnum.{LIST, SET}
import com.ckkloverdos.thrift3r.codec.Codec
import com.ckkloverdos.thrift3r.collection.builder.CollectionBuilderFactory
import org.apache.thrift.protocol.{TSet, TList, TProtocol}
import scala.annotation.tailrec
import scala.collection.GenTraversable
import com.google.common.reflect.TypeToken

/**
 * Codec for Scala Traversables that are not Maps.
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case class ScalaNonMapCollectionCodec[A, M](
  tTypeEnum: TTypeEnum,
  typeToken: TypeToken[GenTraversable[A]],
  elementCodec: Codec[A],
  meta: M,
  builderFactory: CollectionBuilderFactory[A, GenTraversable[A], M]
) extends Codec[GenTraversable[A]] with CodecToString {

  require(
    tTypeEnum == LIST || tTypeEnum == SET,
    "tTypeEnum must be one of (%s, %s) but was %s".format(
      LIST,
      SET,
      tTypeEnum
    )
  )

  override protected def extraToStringElements = List(elementCodec, meta, builderFactory)

  def encode(protocol: TProtocol, value: GenTraversable[A]) {
    if(tTypeEnum == LIST) {
      if(value eq null) {
        protocol.writeListBegin(new TList(elementCodec.tType, 0))
        protocol.writeListEnd()
        return
      }

      val tList = new TList(elementCodec.tType, value.size)
      protocol.writeListBegin(tList)
      for(element ← value) {
        elementCodec.encode(protocol, element)
      }
      protocol.writeListEnd()
    }
    else {
      if(value eq null) {
        protocol.writeSetBegin(new TSet(elementCodec.tType, 0))
        protocol.writeSetEnd()
        return
      }

      val tSet = new TSet(elementCodec.tType, value.size)
      protocol.writeSetBegin(tSet)
      for(element ← value) {
        elementCodec.encode(protocol, element)
      }
      protocol.writeSetEnd()
    }
  }

  def decode(protocol: TProtocol) = {
    val builder = builderFactory.newBuilder(meta)

    @tailrec def decodeElement(index: Int, size: Int) {
      if(index < size) {
        val element = elementCodec.decode(protocol)
        builder.add(element)
        decodeElement(index + 1, size)
      }
    }

    if(tTypeEnum == LIST) {
      val tList = protocol.readListBegin()
      val size = tList.size
      decodeElement(0, size)
      protocol.readListEnd()
    }
    else {
      val tSet = protocol.readSetBegin()
      val size = tSet.size
      decodeElement(0, size)
      protocol.readSetEnd()
    }

    builder.build
  }
}
