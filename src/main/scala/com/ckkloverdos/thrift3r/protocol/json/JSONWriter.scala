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

package com.ckkloverdos.thrift3r
package protocol.json

import java.io.Writer

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
trait JSONWriter {
  def flush()

  def writeString(value: String)

  def writeBool(value: Bool)

  def writeInt8(value: Int8)

  def writeInt16(value: Int16)

  def writeInt32(value: Int32)

  def writeInt64(value: Int64)

  def writeFloat32(value: Float32)

  def writeFloat64(value: Float64)

  def writeArrayBegin()

  def writeArrayEnd()

  def writeObjectBegin()

  def writeObjectEnd()

  def writeFieldName(value: String)

  def newJSONWriter(writer: Writer): JSONWriter
}
