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
package struct

import com.ckkloverdos.thrift3r.descriptor.{FieldInfo, StructDescriptor}
import com.ckkloverdos.thrift3r.protocol.Protocol
import com.ckkloverdos.thrift3r.protocol.helper.ProtocolHelpers
import com.google.common.reflect.TypeToken

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case class StructCodec[T](
  thrifter: Thrift3r,
  descriptor: StructDescriptor
) extends Codec[T] with CodecToString with UnsupportedDirectStringTransformations[T] {

  final val fieldInfoByID = for((fieldId, fieldDescr) ← descriptor.fields) yield {
    val fieldType = fieldDescr.jvmType
    val fieldCodec = thrifter.codecOfType(fieldType).asInstanceOf[Codec[Any]]

    (fieldId, FieldInfo(fieldDescr, fieldCodec))
  }

  final val fieldInfoByName = fieldInfoByID.map { case (id, fi) ⇒ (fi.name, fi) }

  final val arity: Short = descriptor.arity

  /**
   * The supported [[com.ckkloverdos.thrift3r.BinReprType]].
   */
  final def binReprType = BinReprType.STRUCT

  final def typeToken = descriptor.typeToken.asInstanceOf[TypeToken[T]]

  final def encode(protocol: Protocol, value: T) {
    ProtocolHelpers.writeStruct(protocol, descriptor, fieldInfoByID, value)
  }

  final def decode(protocol: Protocol) = {
    val params = new Array[AnyRef](arity)
    ProtocolHelpers.readStruct(protocol, fieldInfoByID, fieldInfoByName, params)
    val struct = descriptor.construct(params)
    struct.asInstanceOf[T]
  }

  override protected def extraToStringElements = List(descriptor)
}
