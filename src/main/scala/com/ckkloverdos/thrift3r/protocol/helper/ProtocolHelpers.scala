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

package com.ckkloverdos.thrift3r.protocol
package helper

import com.ckkloverdos.thrift3r.codec.Codec
import com.ckkloverdos.thrift3r.codec.enumeration.EnumCodec
import com.ckkloverdos.thrift3r.descriptor.{StructDescriptor, FieldInfo}
import scala.annotation.tailrec
import scala.collection.{SortedMap, GenMap, GenTraversable}


/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
object ProtocolHelpers {
  final def encodeAndFlush[T <:AnyRef](protocol: Protocol, bean: T, codec: Codec[T]) {
    codec.encode(protocol, bean)
    protocol.flush()
  }

  final def writeList[T](
    protocol: Protocol,
    elementCodec: Codec[T],
    elements: GenTraversable[T]
  ) {
    val listProtocol = protocol.getListProtocol

    if(elements eq null) {
      listProtocol.writeEmptyList(elementCodec)
      return
    }

    val size = elements.size
    listProtocol.writeListBegin(elementCodec, size)
    for(element ← elements) {
      listProtocol.writeListElement(element, elementCodec)
    }
    listProtocol.writeListEnd()
  }

  final def readList[T](
    protocol: Protocol,
    elementCodec: Codec[T],
    addElement: (T) ⇒ Unit
  ) {

    def readSized(listProtocol: SizedListProtocol) {
      @tailrec def readElement(index: Int, size: Int) {
        if(index < size) {
          val element = listProtocol.readListElement(elementCodec)
          addElement(element)
          readElement(index + 1, size)
        }
      }

      val size = listProtocol.readListBegin()
      readElement(0, size)
      listProtocol.readListEnd()
    }

    def readUnsized(listProtocol: UnsizedListProtocol) {
      listProtocol.readListBegin()

      while(!listProtocol.readListEnd()) {
        val element = listProtocol.readListElement(elementCodec)
        addElement(element)
      }
    }

    protocol.getListProtocol match {
      case listProtocol: SizedListProtocol ⇒
        readSized(listProtocol)

      case listProtocol: UnsizedListProtocol ⇒
        readUnsized(listProtocol)
    }
  }

  final def writeSet[T](
    protocol: Protocol,
    elementCodec: Codec[T],
    elements: GenTraversable[T]
    ) {
    val setProtocol = protocol.getSetProtocol

    if(elements eq null) {
      setProtocol.writeEmptySet(elementCodec)
      return
    }

    val size = elements.size
    setProtocol.writeSetBegin(elementCodec, size)
    for(element ← elements) {
      setProtocol.writeSetElement(element, elementCodec)
    }
    setProtocol.writeSetEnd()
  }

  final def readSet[T](
    protocol: Protocol,
    elementCodec: Codec[T],
    addElement: (T) ⇒ Unit
  ) {
    def readSized(setProtocol: SizedSetProtocol) {
      @tailrec def readElement(index: Int, size: Int) {
        if(index < size) {
          val element = setProtocol.readSetElement(elementCodec)
          addElement(element)
          readElement(index + 1, size)
        }
      }

      val size = setProtocol.readSetBegin()
      readElement(0, size)
      setProtocol.readSetEnd()
    }

    def readUnsized(setProtocol: UnsizedSetProtocol) {
      setProtocol.readSetBegin()

      while(!setProtocol.readSetEnd()) {
        val element = setProtocol.readSetElement(elementCodec)
        addElement(element)
      }
    }

    protocol.getSetProtocol match {
      case setProtocol: SizedSetProtocol ⇒
        readSized(setProtocol)

      case setProtocol: UnsizedSetProtocol ⇒
        readUnsized(setProtocol)
    }
  }

  final def writeMap[K, V](
    protocol: Protocol,
    keyCodec: Codec[K],
    valueCodec: Codec[V],
    elements: GenMap[K, V]
  ) {
    val mapProtocol = protocol.getMapProtocol

    if(elements eq null) {
      mapProtocol.writeEmptyMap(keyCodec, valueCodec)
      return
    }

    val size = elements.size
    mapProtocol.writeMapBegin(keyCodec, valueCodec, size)
    elements.foreach {
      case (k, v) ⇒
        mapProtocol.writeMapElement(k, keyCodec, v, valueCodec)
    }
    mapProtocol.writeMapEnd()
  }

  final def readMap[K, V](
    protocol: Protocol,
    keyCodec: Codec[K],
    valueCodec: Codec[V],
    addElement: ((K, V)) ⇒ Unit
  ) {
    def decodeSized(mapProtocol: SizedMapProtocol) {
      @tailrec def decodeElement(index: Int, size: Int) {
        if(index < size) {
          val kv = mapProtocol.readMapElement(keyCodec, valueCodec)
          addElement(kv)
          decodeElement(index + 1, size)
        }
      }

      val size = mapProtocol.readMapBegin()
      decodeElement(0, size)
      mapProtocol.readMapEnd()
    }

    def decodeUnsized(mapProtocol: UnsizedMapProtocol) {
      mapProtocol.readMapBegin()
      while(!mapProtocol.readMapEnd()) {
        val kv = mapProtocol.readMapElement(keyCodec, valueCodec)
        addElement(kv)
      }
    }

    protocol.getMapProtocol match {
      case mapProtocol: SizedMapProtocol ⇒
        decodeSized(mapProtocol)

      case mapProtocol: UnsizedMapProtocol ⇒
        decodeUnsized(mapProtocol)
    }
  }

  final def writeStruct[T <: AnyRef](
    protocol: Protocol,
    descriptor: StructDescriptor[T],
    fieldInfoByID: SortedMap[Short, FieldInfo],
    value: T
  ) {
    val arity = descriptor.arity
    val structProtocol = protocol.getStructProtocol

    @tailrec def encodeField(id: Short) {
      if(id >= arity) { return }

      val fieldInfo = fieldInfoByID(id)
      val fieldValue = fieldInfo.field.getter(value.asInstanceOf[AnyRef])
      val fieldName = fieldInfo.name
      val fieldCodec = fieldInfo.codec.asInstanceOf[Codec[Any]]

      structProtocol.writeField(fieldCodec, id, fieldName, fieldValue)

      encodeField((id + 1).toShort)
    }

    structProtocol.writeStructBegin(descriptor.name)
    encodeField(0.toShort)
    structProtocol.writeStructEnd()
  }

  final def readStruct(
    protocol: Protocol,
    fieldInfoByID: SortedMap[Short, FieldInfo],
    fieldInfoByName: SortedMap[String, FieldInfo],
    params: Array[AnyRef]
  ) {
    val arity = params.size

    def readStrictFields(structProtocol: StrictFieldsStructProtocol) {
      @tailrec def readField(fieldID: Short) {
        if(fieldID >= arity) {return}

        val fieldInfo = fieldInfoByID(fieldID)
        val fieldCodec = fieldInfo.codec

        structProtocol.readFieldBegin()
        val fieldValue = structProtocol.readField(fieldCodec)
        structProtocol.readFieldEnd()

        params(fieldID) = fieldValue.asInstanceOf[AnyRef]

        readField((fieldID + 1).toShort)
      }

      structProtocol.readStructBegin()
      readField(0.toShort)
      structProtocol.readStructEnd()
    }

    def readFieldsByName(structProtocol: FieldsByNameStructProtocol) {
      @tailrec def readField(counter: Short) {
        if(counter >= arity) {return}

        val fieldName =  structProtocol.readFieldBegin()
        val fieldInfo = fieldInfoByName(fieldName)
        val fieldCodec = fieldInfo.codec
        val fieldID = fieldInfo.id

        val fieldValue = structProtocol.readField(fieldCodec, fieldName)
        structProtocol.readFieldEnd()

        params(fieldID) = fieldValue.asInstanceOf[AnyRef]

        readField((counter + 1).toShort)
      }

      structProtocol.readStructBegin()
      readField(0.toShort)
      structProtocol.readStructEnd()
    }

    def readFieldsByID(structProtocol: FieldsByIDStructProtocol) {
      @tailrec def readField(counter: Short) {
        if(counter >= arity) {return}


        val fieldID = structProtocol.readFieldBegin()
        val fieldInfo = fieldInfoByID(fieldID)
        val fieldCodec = fieldInfo.codec
        val fieldValue = structProtocol.readField(fieldCodec, fieldID)
        structProtocol.readFieldEnd()

        params(fieldID) = fieldValue.asInstanceOf[AnyRef]

        readField((counter + 1).toShort)
      }

      structProtocol.readStructBegin()
      readField(0.toShort)
      structProtocol.readStructEnd()
    }

    protocol.getStructProtocol match {
      case structProtocol: StrictFieldsStructProtocol ⇒
        readStrictFields(structProtocol)

      case structProtocol: FieldsByNameStructProtocol ⇒
        readFieldsByName(structProtocol)

      case structProtocol: FieldsByIDStructProtocol ⇒
        readFieldsByID(structProtocol)
    }
  }

  final def writeEnum(protocol: Protocol, value: Enum[_]) =
    protocol.getEnumProtocol.writeEnum(value)

  final def readEnum[E <: Enum[_]](protocol: Protocol, codec: EnumCodec[E]) = {
    protocol.getEnumProtocol match {
      case enumProtocol: IntEnumProtocol ⇒
        val ordinal = enumProtocol.readEnum()
        codec.fromOrdinal(ordinal)

      case enumProtocol: StringEnumProtocol ⇒
        val name = enumProtocol.readEnum()
        codec.fromNameIgnoringCase(name)
    }
  }

  final def writeBoolBasedOption[T](protocol: Protocol, codec: Codec[T], value: Option[T]) {
    value match {
      case null | None ⇒
        protocol.writeBool(false) // not present

      case Some(element) ⇒
        protocol.writeBool(true) // present
        codec.encode(protocol, element)
    }
  }

  final def readBoolBasedOption[T](protocol: Protocol, codec: Codec[T]): Option[T] = {
    protocol.readBool() match {
      case false ⇒ None
      case true  ⇒ Some(codec.decode(protocol))
    }
  }

  final def writeSetBasedOption[T](protocol: Protocol, codec: Codec[T], value: Option[T]) {
    val setProtocol = protocol.getSetProtocol
    value match {
      case null | None ⇒
        setProtocol.writeEmptySet(codec)

      case Some(element) ⇒
        setProtocol.writeSetBegin(codec, 1)
        setProtocol.writeSetElement(element, codec)
        setProtocol.writeSetEnd()
    }
  }

  final def readSetBasedOption[T](protocol: Protocol, codec: Codec[T]): Option[T] = {
    protocol.getSetProtocol match {
      case setProtocol: SizedSetProtocol ⇒
        val size = setProtocol.readSetBegin()
        require(size == 1)
        val element = setProtocol.readSetElement(codec)
        setProtocol.readSetEnd()
        Some(element)

      case setProtocol: UnsizedSetProtocol ⇒
        setProtocol.readSetBegin()
        if(setProtocol.readSetEnd()) {
          None
        }
        else {
          val element = setProtocol.readSetElement(codec)
          require(setProtocol.readSetEnd())
          Some(element)
        }
    }
  }
}
