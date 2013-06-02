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


/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
trait Protocol {
  this: EnumProtocol
  with  ListProtocol
  with  SetProtocol
  with  StructProtocol
  with  MapProtocol
  with  OptionProtocol ⇒

  def flush()

  def writeBool(value: Boolean)
  def readBool(): Boolean

  def writeInt8(value: Byte)
  def readInt8(): Byte

  def writeInt16(value: Short)
  def readInt16(): Short

  def writeInt32(value: Int)
  def readInt32(): Int

  def writeInt64(value: Long)
  def readInt64(): Long

  def writeFloat32(value: Float)
  def readFloat32(): Float

  def writeFloat64(value: Double)
  def readFloat64(): Double

  def writeString(value: String)
  def readString(): String

//  def writeBinary(value: ByteBuffer)

  def getEnumProtocol: EnumProtocol = this

  def getListProtocol: ListProtocol = this

  def getSetProtocol: SetProtocol = this

  def getMapProtocol: MapProtocol = this

  def getOptionProtocol: OptionProtocol = this

  def getStructProtocol: StructProtocol = this
}

