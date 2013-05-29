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
import com.fasterxml.jackson.core.{JsonToken, JsonParser}

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
class PeepingJSONParser(underlying: JsonParser) extends JsonParserDelegate(underlying) {
  private[this] var _peepNextToken: JsonToken = null

  override def nextToken() =
    _peepNextToken match {
      case null ⇒
        super.nextToken()

      case peepNextToken ⇒
        _peepNextToken = null
        peepNextToken

    }

  def peepNextToken() =
    _peepNextToken match {
      case null ⇒
        _peepNextToken = super.nextToken()
        _peepNextToken

      case peepNextToken ⇒
        peepNextToken
    }

  def nextString = {
    super.nextToken()
    super.getText
  }

  def nextBool = {
    super.nextToken()
    super.getValueAsBoolean
  }

  def nextInt8 = {
    super.nextToken()
    super.getValueAsInt.toByte
  }

  def nextInt16 = {
    super.nextToken()
    super.getValueAsInt.toShort
  }

  def nextInt32 = {
    super.nextToken()
    super.getValueAsInt
  }

  def nextInt64 = {
    super.nextToken()
    super.getValueAsLong
  }

  def nextFloat64 = {
    super.nextToken()
    super.getValueAsDouble
  }
}
