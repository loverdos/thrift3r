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

import java.io.Reader


/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
trait JSONReader {
  def nextToken(): JSONToken

  def peepNextToken(): JSONToken

  def currentToken(): JSONToken

  def currentText(): String

  def currentLocation(): TextLocation

  def readString(): String

  def readBool(): Boolean

  def readInt8(): Byte

  def readInt16(): Short

  def readInt32(): Int

  def readInt64(): Long

  def readFloat64(): Double

  def newJSONReader(reader: Reader): JSONReader
}
