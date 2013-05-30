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

import com.fasterxml.jackson.core.util.JsonParserDelegate
import com.fasterxml.jackson.core.{JsonStreamContext, JsonToken, JsonParser}

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
class JSONParser(underlying: JsonParser) extends JsonParserDelegate(underlying) {
  private[this] var _peepToken: JsonToken = null
  private[this] var _peepText: String = null
  private[this] var _peepCtx: JsonStreamContext = null
  private[this] var _lastToken: JsonToken = null
  private[this] var _lastText: String = null
  private[this] var _lastCtx: JsonStreamContext = null

  def superNextToken() = {
    _lastToken = super.nextToken()
    _lastText = super.getText
    _lastCtx = super.getParsingContext

    _lastToken
  }

  override def nextToken() = {
    _peepToken match {
      case null ⇒
        superNextToken()

      case _ ⇒
        _lastToken = _peepToken
        _lastText = _peepText
        _lastCtx = _peepCtx
        _peepToken = null; _peepText = null; _peepCtx = null
    }

//    println("!! token=%s, text=%s, context=%s".format(_lastToken, _lastText, _lastCtx))
    _lastToken
  }

  def peepNextToken() =
    _peepToken match {
      case null ⇒
        _peepToken = superNextToken()
        _peepText = _lastText
        _peepCtx = _lastCtx
        _peepToken

      case _ ⇒
        _peepToken
    }

  def nextString = {
    nextToken()
    _lastText
  }

  def nextBool = {
    nextToken()
    _lastText.toBoolean
  }

  def nextInt8 = {
    nextToken()
    _lastText.toByte
  }

  def nextInt16 = {
    nextToken()
    _lastText.toShort
  }

  def nextInt32 = {
    nextToken()
    _lastText.toInt
  }

  def nextInt64 = {
    nextToken()
    _lastText.toLong
  }

  def nextFloat64 = {
    nextToken()
    _lastText.toDouble
  }
}
