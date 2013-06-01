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
package jackson

import com.fasterxml.jackson.core.{JsonFactory, JsonLocation, JsonStreamContext, JsonToken, JsonParser}
import java.io.Reader

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
final class JacksonJSONReader(
  parser: JsonParser,
  factory: JsonFactory
) extends JSONReader {

  private[this] var _peepToken: JSONToken = null
  private[this] var _peepText: String = null
  private[this] var _peepLocation: TextLocation = null
  private[this] var _peepCtx: JsonStreamContext = null
  private[this] var _currentToken: JSONToken = null
  private[this] var _currentText: String = null
  private[this] var _currentLocation: TextLocation = null
  private[this] var _currentCtx: JsonStreamContext = null

  private[this] def superNextToken() = {
    val jacksonToken = parser.nextToken()
    _currentToken = translateToken(jacksonToken)
    _currentText = parser.getText
    _currentLocation = translateLocation(parser.getCurrentLocation)
    _currentCtx = parser.getParsingContext

    _currentToken
  }

  @inline def translateLocation(jacksonLocation: JsonLocation): TextLocation =
    TextLocation(
      jacksonLocation.getSourceRef,
      jacksonLocation.getLineNr,
      jacksonLocation.getColumnNr,
      jacksonLocation.getCharOffset
    )

  @inline def translateToken(jacksonToken: JsonToken): JSONToken =
    jacksonToken match {
      case JsonToken.START_ARRAY   ⇒ JSONToken.ARRAY_START
      case JsonToken.END_ARRAY     ⇒ JSONToken.ARRAY_END
      case JsonToken.START_OBJECT  ⇒ JSONToken.OBJECT_START
      case JsonToken.END_OBJECT    ⇒ JSONToken.OBJECT_END
      case JsonToken.NOT_AVAILABLE ⇒ JSONToken.NOTHING
      case _ ⇒ JSONToken.VALUE
    }

  def currentToken() = _currentToken

  def currentText() = _currentText

  def currentLocation() = _currentLocation

  def nextToken() = {
    _peepToken match {
      case null ⇒
        superNextToken()

      case _ ⇒
        _currentToken = _peepToken
        _currentText = _peepText
        _currentLocation = _peepLocation
        _currentCtx = _peepCtx
        _peepToken = null; _peepText = null; _peepCtx = null
    }

//    println("!! nextToken(): token=%s, text=%s, location=%s".format(_currentToken, _currentText, _currentLocation))
    _currentToken
  }

  def peepNextToken() = {
    val token = _peepToken match {
      case null ⇒
        _peepToken = superNextToken()
        _peepText = _currentText
        _peepLocation = _currentLocation
        _peepCtx = _currentCtx
        _peepToken

      case _ ⇒
        _peepToken
    }

//    println("!! peepNextToken(): token=%s, text=%s, location=%s".format(_peepToken, _peepText, _peepLocation))
    token
  }

  def readString() = {
    nextToken()
    _currentText
  }

  def readBool() = {
    nextToken()
    _currentText.toBoolean
  }

  def readInt8() = {
    nextToken()
    _currentText.toByte
  }

  def readInt16() = {
    nextToken()
    _currentText.toShort
  }

  def readInt32() = {
    nextToken()
    _currentText.toInt
  }

  def readInt64() = {
    nextToken()
    _currentText.toLong
  }

  def readFloat64() = {
    nextToken()
    _currentText.toDouble
  }

  def newJSONReader(reader: Reader) = new JacksonJSONReader(factory.createParser(reader), factory)
}
