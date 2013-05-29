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

import com.ckkloverdos.thrift3r.codec.Codec

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
trait StructProtocol { this: Protocol ⇒
  def writeFieldBegin[T](fieldCodec: Codec[T], id: Short, name: String)

  def writeFieldValue[T](fieldCodec: Codec[T], value: T) = fieldCodec.encode(this, value)

  def writeField[T](fieldCodec: Codec[T], id: Short, name: String, value: T) {
    writeFieldBegin(fieldCodec, id, name)
    writeFieldValue(fieldCodec, value)
    writeFieldEnd()
  }

  def writeFieldEnd()
  def writeStructBegin[T](name: String)
  def writeStructEnd()

  def readFieldEnd()

  def readStructBegin()
  def readStructEnd()
}

/**
 * Field are encoded in their strict id order. The id itself is not encoded.
 */
trait StrictFieldsStructProtocol extends StructProtocol { this: Protocol ⇒
  def readFieldBegin()
  def readField[T](fieldCodec: Codec[T]): T
}

trait FieldsByIDStructProtocol extends StructProtocol { this: Protocol ⇒
  def readFieldBegin(): Short // returns the ID of the field
  def readField[T](fieldCodec: Codec[T], id: Short): T
}

trait FieldsByNameStructProtocol extends StructProtocol { this: Protocol ⇒
  def readFieldBegin(): String // returns the name of the field
  def readField[T](fieldCodec: Codec[T], name: String): T
}
