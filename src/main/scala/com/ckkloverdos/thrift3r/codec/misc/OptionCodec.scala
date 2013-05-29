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
import com.ckkloverdos.thrift3r.protocol.{UnsizedSetProtocol, SizedSetProtocol, Protocol}
import com.google.common.reflect.TypeToken

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
final case class OptionCodec[T](
  typeToken: TypeToken[Option[T]],
  elementCodec: Codec[T]
) extends Codec[Option[T]] with UnsupportedDirectStringTransformations[Option[T]] {

  def tTypeEnum = TTypeEnum.SET

  def encode(protocol: Protocol, value: Option[T]) {
    val setProtocol = protocol.getSetProtocol
    value match {
      case null | None ⇒
        setProtocol.writeSetBegin(elementCodec, 0)
        setProtocol.writeSetEnd()

      case Some(element) ⇒
        setProtocol.writeSetBegin(elementCodec, 1)
        setProtocol.writeSetElement(element, elementCodec)
        setProtocol.writeSetEnd()
    }
  }

  def decode(protocol: Protocol) = {
    protocol.getSetProtocol match {
      case setProtocol: SizedSetProtocol ⇒
        val isEmpty = setProtocol.readSetBegin() == 0
        val option = if(isEmpty) None else {
          val element = setProtocol.readSetElement(elementCodec)
          Some(element)
        }
        setProtocol.readSetEnd()
        option

      case setProtocol: UnsizedSetProtocol ⇒
        setProtocol.readSetBegin()
        if(setProtocol.readSetEnd()) {
          None
        }
        else {
          val element = setProtocol.readSetElement(elementCodec)
          setProtocol.readSetEnd()
          Some(element)
        }
    }
  }
}
