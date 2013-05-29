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
import com.ckkloverdos.thrift3r.protocol.{FieldsByNameStructProtocol, UnsizedMapProtocol, StructProtocol, UnsizedSetProtocol, UnsizedListProtocol, Protocol}
import com.fasterxml.jackson.core.{JsonFactory, JsonLocation, JsonToken, JsonParser, JsonGenerator}
import java.io.StringWriter

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
class JSONProtocol(
  jsonFactory: JsonFactory,
  jsonGen: JsonGenerator,
  _jsonParser: JsonParser
) extends Protocol
  with    UnsizedListProtocol // don't know the list size before reading it all
  with    UnsizedSetProtocol
  with    UnsizedMapProtocol
  with    FieldsByNameStructProtocol {

  protected val jsonParser = new PeepingJSONParser(_jsonParser)

  def flush() {
    if(jsonGen ne null) { jsonGen.flush() }
  }

  def writeBool(value: Boolean) = jsonGen.writeBoolean(value)

  def writeInt8(value: Byte) = jsonGen.writeNumber(value.toShort)

  def writeInt16(value: Short) = jsonGen.writeNumber(value)

  def writeInt32(value: Int) = jsonGen.writeNumber(value)

  def writeInt64(value: Long) = jsonGen.writeNumber(value)

  def writeFloat64(value: Double) = jsonGen.writeNumber(value)

  def writeString(value: String) = jsonGen.writeString(value)

  def readBool() = jsonParser.nextBool

  def readInt8() = jsonParser.nextInt8

  def readInt16() = jsonParser.nextInt16

  def readInt32() = jsonParser.nextInt32

  def readInt64() = jsonParser.nextInt64

  def readFloat64() = jsonParser.nextFloat64

  def readString() = jsonParser.nextString

  // LIST
  def writeListBegin[T](elementCodec: Codec[T]) = jsonGen.writeStartArray()

  def writeListElement[T](element: T, elementCodec: Codec[T]) =
    elementCodec.encode(this, element)

  def writeListEnd() = jsonGen.writeEndArray()

  protected def requireToken(expected: JsonToken, token: JsonToken, location: JsonLocation) {
    require(
      token == expected,
      "Expected %s but found %s. %s".format(
        expected,
        token,
        location
      )
    )
  }

  def readListBegin()  {
    val token = jsonParser.nextToken()
    requireToken(JsonToken.START_ARRAY, token, jsonParser.getCurrentLocation)
  }

  def readListElement[T](elementCodec: Codec[T]) =
    elementCodec.decode(this)

  def readListEnd() = {
    jsonParser.peepNextToken() match {
      case JsonToken.END_ARRAY ⇒
        jsonParser.nextToken()
        true

      case _ ⇒
        false
    }
  }

  // SET (delegates to the LIST implementation)
  def writeSetBegin[T](elementCodec: Codec[T]) = writeListBegin(elementCodec)

  def writeSetElement[T](element: T, elementCodec: Codec[T]) =
    elementCodec.encode(this, element)

  def writeSetEnd() = writeListEnd()

  def readSetBegin() = readListBegin()

  def readSetElement[T](elementCodec: Codec[T]) =
    elementCodec.decode(this)

  def readSetEnd() = readListEnd()

  // MAP
  def writeMapBegin[K, V](keyCodec: Codec[K], valueCodec: Codec[V]) = jsonGen.writeStartObject()

  def writeMapElement[K, V](key: K, keyCodec: Codec[K], value: V, valueCodec: Codec[V]) {
    // generate a string representation of the key
    val keyString = keyCodec.hasDirectStringRepresentation match {
      case true ⇒
        // Simple cases get optimized
        keyCodec.toDirectString(key)

      case false ⇒
        // The key may be a full blown struct
        val keyWriter = new StringWriter()
        val keyJsonGen = jsonFactory.createGenerator(keyWriter)
        val keyJsonProtocol = new JSONProtocol(jsonFactory, keyJsonGen, jsonParser)
        keyCodec.encode(keyJsonProtocol, key)
        keyJsonGen.flush()
        keyJsonGen.close()
        keyWriter.toString
    }
    
    jsonGen.writeFieldName(keyString)
    jsonGen.writeRaw(':')
    valueCodec.encode(this, value)
  }

  def writeMapEnd() = jsonGen.writeEndObject()

  def readMapBegin() {
    val token = jsonParser.nextToken()
    requireToken(JsonToken.START_OBJECT, token, jsonParser.getCurrentLocation)
  }


  def readMapElement[K, V](keyCodec: Codec[K], valueCodec: Codec[V]): (K, V) = {
    val keyString = jsonParser.getValueAsString
    val key = keyCodec.hasDirectStringRepresentation match {
      case true ⇒
        // Optimize via direct string support
        keyCodec.fromDirectString(keyString)

      case false ⇒
        // Full-blown decoding
        val keyJsonParser = jsonFactory.createParser(keyString)
        val keyJsonProtocol = new JSONProtocol(jsonFactory, jsonGen, keyJsonParser)
        keyCodec.decode(keyJsonProtocol)
    }

    val value = valueCodec.decode(this)

    (key, value)
  }

  /**
   * Returns `true` if it reaches the map end.
   */
  def readMapEnd() =
    jsonParser.peepNextToken() match {
      case JsonToken.END_OBJECT ⇒
        jsonParser.nextToken()
        true

      case _ ⇒
        false
    }

  // STRUCT
  def writeFieldBegin[T](fieldCodec: Codec[T], id: Short, name: String) {
    jsonGen.writeFieldName(name)
//    jsonGen.writeRaw(':')
  }

  def writeFieldEnd() {}

  def writeStructBegin[T](name: String) = jsonGen.writeStartObject()

  def writeStructEnd() = jsonGen.writeEndObject()

  def readFieldBegin() = jsonParser.nextString

  def readField[T](fieldCodec: Codec[T], name: String) = fieldCodec.decode(this)

  def readFieldEnd() {}

  def readStructBegin() {
    val token = jsonParser.nextToken()
    require(
      token == JsonToken.START_OBJECT,
      "Expected %s but found %s. %s".format(
        JsonToken.START_OBJECT,
        token,
        jsonParser.getCurrentLocation
      )
    )
  }

  def readStructEnd() {
    val token = jsonParser.nextToken()
    require(
      token == JsonToken.END_OBJECT,
      "Expected %s but found %s. %s".format(
        JsonToken.END_OBJECT,
        token,
        jsonParser.getCurrentLocation
      )
    )
  }
}