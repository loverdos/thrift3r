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

package com.ckkloverdos.thrift3r.protocol.json

import com.ckkloverdos.thrift3r.codec.Codec
import com.ckkloverdos.thrift3r.protocol.{OptionProtocol, StringEnumProtocol, FieldsByNameStructProtocol, UnsizedMapProtocol, UnsizedSetProtocol, UnsizedListProtocol, Protocol}
import java.io.{StringReader, StringWriter}
import com.ckkloverdos.thrift3r.protocol.helper.ProtocolHelpers

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
final class JSONProtocol(
  jsonReader: JSONReader,
  jsonWriter: JSONWriter
) extends Protocol
  with    StringEnumProtocol
  with    UnsizedListProtocol // don't know the list size before reading it all
  with    UnsizedSetProtocol
  with    UnsizedMapProtocol
  with    OptionProtocol
  with    FieldsByNameStructProtocol {

  def flush() {
    if(jsonWriter ne null) { jsonWriter.flush() }
  }

  def writeEnum(value: Enum[_]) = jsonWriter.writeString(value.toString)

  def writeBool(value: Boolean) = jsonWriter.writeBool(value)

  def writeInt8(value: Byte) = jsonWriter.writeInt8(value)

  def writeInt16(value: Short) = jsonWriter.writeInt16(value)

  def writeInt32(value: Int) = jsonWriter.writeInt32(value)

  def writeInt64(value: Long) = jsonWriter.writeInt64(value)

  def writeFloat32(value: Float) = jsonWriter.writeFloat32(value)

  def writeFloat64(value: Double) = jsonWriter.writeFloat64(value)

  def writeString(value: String) = jsonWriter.writeString(value)

  def readEnum() = jsonReader.readString()

  def readBool() = jsonReader.readBool()

  def readInt8() = jsonReader.readInt8()

  def readInt16() = jsonReader.readInt16()

  def readInt32() = jsonReader.readInt32()

  def readInt64() = jsonReader.readInt64()

  def readFloat32() = jsonReader.readFloat32()

  def readFloat64() = jsonReader.readFloat64()

  def readString() = jsonReader.readString()

  // LIST
  def writeListBegin[T](elementCodec: Codec[T]) = jsonWriter.writeArrayBegin()

  def writeListElement[T](element: T, elementCodec: Codec[T]) =
    elementCodec.encode(this, element)

  def writeListEnd() = jsonWriter.writeArrayEnd()

  protected def requireToken(expected: JSONToken, token: JSONToken, tokenText: String, location: TextLocation) {
    require(
      token == expected,
      "Expected %s but found %s(%s). %s".format(
        expected,
        token,
        tokenText,
        location
      )
    )
  }

  def readListBegin()  {
    val token = jsonReader.nextToken()

    val tokenText = jsonReader.currentText()
    val location = jsonReader.currentLocation()
    requireToken(JSONToken.ARRAY_START, token, tokenText, location)
  }

  def readListElement[T](elementCodec: Codec[T]) =
    elementCodec.decode(this)

  def readListEnd() = {
    jsonReader.peepNextToken() match {
      case JSONToken.ARRAY_END ⇒
        jsonReader.nextToken()
        true

      case token ⇒
        false
    }
  }

  // SET (delegates to the LIST implementation)
  def writeSetBegin[T](elementCodec: Codec[T]) =
    jsonWriter.writeArrayBegin()

  def writeSetElement[T](element: T, elementCodec: Codec[T]) =
    elementCodec.encode(this, element)

  def writeSetEnd() =
    jsonWriter.writeArrayEnd()

  def readSetBegin() = {
    val token = jsonReader.nextToken()

    val tokenText = jsonReader.currentText()
    val location = jsonReader.currentLocation()
    requireToken(JSONToken.ARRAY_START, token, tokenText, location)
  }

  def readSetElement[T](elementCodec: Codec[T]) =
    elementCodec.decode(this)

  def readSetEnd() = {
    jsonReader.peepNextToken() match {
      case JSONToken.ARRAY_END ⇒
        jsonReader.nextToken()
        true

      case token ⇒
        false
    }
  }

  // MAP
  def writeMapBegin[K, V](keyCodec: Codec[K], valueCodec: Codec[V]) = jsonWriter.writeObjectBegin()

  def writeMapElement[K, V](key: K, keyCodec: Codec[K], value: V, valueCodec: Codec[V]) {
    // generate a string representation of the key
    val keyString = keyCodec.hasDirectStringRepresentation match {
      case true ⇒
        // Simple cases get optimized
        keyCodec.toDirectString(key)

      case false ⇒
        // The key may be a full blown struct
        val keyWriter = new StringWriter()
        val keyJsonWriter = jsonWriter.newJSONWriter(keyWriter)
        val keyJsonProtocol = new JSONProtocol(jsonReader, keyJsonWriter)
        keyCodec.encode(keyJsonProtocol, key)
        keyJsonWriter.flush()
        keyWriter.toString
    }
    
    jsonWriter.writeFieldName(keyString)
    valueCodec.encode(this, value)
  }

  def writeMapEnd() = jsonWriter.writeObjectEnd()

  def readMapBegin() {
    val token = jsonReader.nextToken()

    val tokenText = jsonReader.currentText()
    val location = jsonReader.currentLocation()
    requireToken(JSONToken.OBJECT_START, token, tokenText, location)
  }


  def readMapElement[K, V](keyCodec: Codec[K], valueCodec: Codec[V]): (K, V) = {
    val keyString = jsonReader.readString()
    val key = keyCodec.hasDirectStringRepresentation match {
      case true ⇒
        // Optimize via direct string support
        keyCodec.fromDirectString(keyString)

      case false ⇒
        // Full-blown decoding
        val keyJsonReader = jsonReader.newJSONReader(new StringReader(keyString))
        val keyJsonProtocol = new JSONProtocol(keyJsonReader, jsonWriter)
        keyCodec.decode(keyJsonProtocol)
    }

    val value = valueCodec.decode(this)

    (key, value)
  }

  /**
   * Returns `true` if it reaches the map end.
   */
  def readMapEnd() =
    jsonReader.peepNextToken() match {
      case JSONToken.OBJECT_END ⇒
        jsonReader.nextToken()
        true

      case _ ⇒
        false
    }

  // OPTION
  def writeOption[T](elementCodec: Codec[T], option: Option[T]) =
    ProtocolHelpers.writeSetBasedOption(this, elementCodec, option)

  def readOption[T](elementCodec: Codec[T]) =
    ProtocolHelpers.readSetBasedOption(this, elementCodec)

  // STRUCT
  def writeFieldBegin[T](fieldCodec: Codec[T], id: Short, name: String) {
    jsonWriter.writeFieldName(name)
  }

  def writeFieldEnd() {}

  def writeStructBegin[T](name: String) = jsonWriter.writeObjectBegin()

  def writeStructEnd() = jsonWriter.writeObjectEnd()

  def readFieldBegin() = jsonReader.readString()

  def readField[T](fieldCodec: Codec[T], name: String) =
    fieldCodec.decode(this)

  def readFieldEnd() {}

  def readStructBegin() {
    val token = jsonReader.nextToken()

    val text = jsonReader.currentText()
    val location = jsonReader.currentLocation()
    requireToken(JSONToken.OBJECT_START, token, text, location)
  }

  def readStructEnd() {
    val token = jsonReader.nextToken()

    val tokenText = jsonReader.currentText()
    val location = jsonReader.currentLocation()
    requireToken(JSONToken.OBJECT_END, token, tokenText, location)
  }
}