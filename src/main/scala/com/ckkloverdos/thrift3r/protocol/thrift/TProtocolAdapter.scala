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

package com.ckkloverdos.thrift3r.protocol.thrift

import com.ckkloverdos.thrift3r.codec.Codec
import com.ckkloverdos.thrift3r.protocol.{StrictFieldsStructProtocol, SizedMapProtocol, SizedSetProtocol, SizedListProtocol, Protocol}
import org.apache.thrift.protocol.{TMap, TSet, TStruct, TField, TList, TProtocol}

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
class TProtocolAdapter(tprotocol: TProtocol)
  extends Protocol
  with    SizedListProtocol
  with    SizedSetProtocol
  with    SizedMapProtocol
  with    StrictFieldsStructProtocol {

  def flush() {
    tprotocol.getTransport.flush()
  }

  def writeBool(value: Boolean) { tprotocol.writeBool(value) }

  def writeInt8(value: Byte) {tprotocol.writeByte(value) }

  def writeInt16(value: Short) { tprotocol.writeI16(value) }

  def writeInt32(value: Int) { tprotocol.writeI32(value) }

  def writeInt64(value: Long) { tprotocol.writeI64(value) }

  def writeFloat64(value: Double) { tprotocol.writeDouble(value) }

  def writeString(value: String) { tprotocol.writeString(value) }

  //  def writeBinary(value: ByteBuffer) { tprotocol.writeBinary(value) }

  def readBool() = tprotocol.readBool()

  def readInt8() = tprotocol.readByte()

  def readInt16() = tprotocol.readI16()

  def readInt32() = tprotocol.readI32()

  def readInt64() = tprotocol.readI64()

  def readFloat64() = tprotocol.readDouble()

  def readString() = tprotocol.readString()

  // LIST
  def writeListBegin[T](elementCodec: Codec[T], size: Int) =
    tprotocol.writeListBegin(new TList(elementCodec.tType, size))

  def writeListElement[T](element: T, elementCodec: Codec[T]) =
    elementCodec.encode(this, element)

  def writeListEnd() = tprotocol.writeListEnd()

  def readListBegin() = tprotocol.readListBegin().size

  def readListElement[T](elementCodec: Codec[T]): T =
    elementCodec.decode(this)

  def readListEnd() = tprotocol.readListEnd()

  // SET
  def writeSetBegin[T](elementCodec: Codec[T], size: Int) =
    tprotocol.writeSetBegin(new TSet(elementCodec.tType, size))

  def writeSetElement[T](element: T, elementCodec: Codec[T]) =
    elementCodec.encode(this, element)

  def writeSetEnd() = tprotocol.writeSetEnd()

  def readSetBegin() = tprotocol.readSetBegin().size

  def readSetElement[T](elementCodec: Codec[T]): T =
    elementCodec.decode(this)

  def readSetEnd() = tprotocol.readSetEnd()

  // MAP
  def writeMapBegin[K, V](keyCodec: Codec[K], valueCodec: Codec[V], size: Int) =
    tprotocol.writeMapBegin(new TMap(keyCodec.tType, valueCodec.tType, size))

  def writeMapElement[K, V](key: K, keyCodec: Codec[K], value: V, valueCodec: Codec[V]) {
    keyCodec.encode(this, key)
    valueCodec.encode(this, value)
  }

  def writeMapEnd() = tprotocol.writeMapEnd()

  def readMapBegin() = tprotocol.readMapBegin().size

  def readMapElement[K, V](keyCodec: Codec[K], valueCodec: Codec[V]): (K, V) = {
    val key = keyCodec.decode(this)
    val value = valueCodec.decode(this)
    (key, value)
  }

  def readMapEnd() = tprotocol.readMapEnd()

  // STRUCT
  def writeFieldBegin[T](fieldCodec: Codec[T], id: Short, name: String) =
    tprotocol.writeFieldBegin(new TField(name, fieldCodec.tType, id))

  def writeFieldEnd() = tprotocol.writeFieldEnd()

  def writeStructBegin[T](name: String) = tprotocol.writeStructBegin(new TStruct(name))

  def writeStructEnd() = tprotocol.writeStructEnd()

  def readFieldBegin() = tprotocol.readFieldBegin().name

  def readField[T](fieldCodec: Codec[T]) = fieldCodec.decode(this)

  def readFieldEnd() = tprotocol.readFieldEnd()

  def readStructBegin() = tprotocol.readStructBegin()

  def readStructEnd() = tprotocol.readStructEnd()
}
