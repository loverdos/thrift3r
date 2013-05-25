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

import com.ckkloverdos.thrift3r.TTypeEnum
import com.google.common.reflect.TypeToken
import org.apache.thrift.protocol.{TSet, TProtocol}

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
final case class OptionCodec[T](
  typeToken: TypeToken[Option[T]],
  elementCodec: Codec[T]
) extends Codec[Option[T]] {

  def tTypeEnum = TTypeEnum.SET

  def encode(protocol: TProtocol, value: Option[T]) {
    value match {
      case null | None ⇒
        val tSet = new TSet(elementCodec.tType, 0)
        protocol.writeSetBegin(tSet)
        protocol.writeSetEnd()

      case Some(element) ⇒
        val tSet = new TSet(elementCodec.tType, 1)
        protocol.writeSetBegin(tSet)
        elementCodec.encode(protocol, element)
        protocol.writeSetEnd()
    }
  }

  def decode(protocol: TProtocol) = {
    val tSet = protocol.readSetBegin()
    val option = if(tSet.size == 0) None else Some(elementCodec.decode(protocol))
    protocol.readSetEnd()
    option
  }
}
