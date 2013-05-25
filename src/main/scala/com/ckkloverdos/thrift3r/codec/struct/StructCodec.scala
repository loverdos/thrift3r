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

package com.ckkloverdos.thrift3r.codec.struct

import com.ckkloverdos.thrift3r.Thrift3rHelpers.valueAndTypeStr
import com.ckkloverdos.thrift3r.codec.{CodecToString, Codec}
import com.ckkloverdos.thrift3r.descriptor.StructDescriptor
import com.ckkloverdos.thrift3r.{Thrift3r, TTypeEnum}
import com.google.common.reflect.TypeToken
import org.apache.thrift.protocol.{TField, TProtocol}
import scala.annotation.tailrec

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case class StructCodec[T](
  thrifter: Thrift3r,
  descriptor: StructDescriptor
) extends Codec[T] with CodecToString {

  final val codecAndFieldById = for((fieldId, fieldDescr) ← descriptor.fields) yield {
    val fieldType = fieldDescr.jvmType
    val fieldCodec = thrifter.codecOfType(fieldType).asInstanceOf[Codec[Any]]
    val tfield = new TField(fieldDescr.name, fieldCodec.tType, fieldId)
    (fieldId, (fieldCodec, tfield))
  }

  /**
   * The supported [[com.ckkloverdos.thrift3r.TTypeEnum]].
   */
  final def tTypeEnum = TTypeEnum.STRUCT

  final def typeToken = descriptor.typeToken.asInstanceOf[TypeToken[T]]

  final def encode(protocol: TProtocol, value: T) {
    println("let encode %s (%s) =".format(protocol, valueAndTypeStr(value)))
    val arity = descriptor.arity

    @tailrec def encodeFields(id: Short) {
      if(id >= arity) {return}

      val fieldDescr = descriptor.fields(id)
      val fieldValue = fieldDescr.getter(value.asInstanceOf[AnyRef])
      val (fieldCodec, tfield) = codecAndFieldById(id)

      println("  encode (%s) %s::%s, value=(%s), codec=%s".format(
        id,
        descriptor.jvmClass.getSimpleName,
        fieldDescr.name,
        valueAndTypeStr(fieldValue),
        fieldCodec
      ))

      try {
        protocol.writeFieldBegin(tfield)
        fieldCodec.encode(protocol, fieldValue)
        protocol.writeFieldEnd()
      }
      catch {
        case e: Throwable ⇒
          throw new Exception(
            "Error encoding %s::%s of type %s".format(
              descriptor.jvmClass.getName,
              fieldDescr.name,
              fieldDescr.jvmClass.getName
            ),
            e
          )
      }

      encodeFields((id + 1).toShort)
    }

    protocol.writeStructBegin(descriptor.thriftStruct)
    encodeFields(0.toShort)
    protocol.writeStructEnd()
  }

  final def decode(protocol: TProtocol) = {
    val arity = descriptor.arity
    println("let decode %s = (->%s/%s)".format(protocol, descriptor.jvmType, arity))
    val params = new Array[AnyRef](arity)

    @tailrec def decodeFields(i: Short) {
      if(i >= arity) {return}

      val (fieldCodec, tfield) = codecAndFieldById(i)

      val fieldDescr = descriptor.fields(i)
      println("  let decode (%s) %s::%s, codec=%s = ".format(
        i,
        descriptor.jvmClass.getSimpleName,
        fieldDescr.name,
        fieldCodec
      ))

      try {
        protocol.readFieldBegin()
        val fieldValue = fieldCodec.decode(protocol)
        protocol.readFieldEnd()
        params(i) = fieldValue.asInstanceOf[AnyRef]

        println("    (%s)".format(valueAndTypeStr(fieldValue)))
      }
      catch {
        case e: Throwable ⇒
          throw new Exception(
            "Error decoding %s::%s of type %s".format(
              descriptor.jvmType,
              fieldDescr.name,
              fieldDescr.jvmType
            ),
            e
          )
      }

      decodeFields((i + 1).toShort)
    }

    protocol.readStructBegin()
    decodeFields(0.toShort)
    protocol.readStructEnd()

    val struct = descriptor.construct(params)
    println("  (%s)".format(valueAndTypeStr(struct)))
    struct.asInstanceOf[T]
  }

  override protected def extraToStringElements = List(descriptor)
}
