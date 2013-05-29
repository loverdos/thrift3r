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
package codec
package primitiveref

import com.ckkloverdos.thrift3r.TTypeEnum
import com.ckkloverdos.thrift3r.protocol.Protocol

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case object DoubleRefCodec extends Codec[java.lang.Double] with CodecToString {

  /**
   * The supported [[com.ckkloverdos.thrift3r.TTypeEnum]],
   * which is [[com.ckkloverdos.thrift3r.TTypeEnum#FLOAT64]].
   */
  final def tTypeEnum = TTypeEnum.FLOAT64

  final def typeToken = typeTokenOfClass(DoubleRefClass)

  @inline final def getValue(value: java.lang.Double): Double = {
    value match {
      case null ⇒ 0.0
      case _ ⇒ value.doubleValue()
    }
  }

  final def encode(protocol: Protocol, value: java.lang.Double) {
    val doubleValue = getValue(value)

    protocol.writeFloat64(doubleValue)
  }

  final def decode(protocol: Protocol) = java.lang.Double.valueOf(protocol.readFloat64())

  final def toDirectString(value: java.lang.Double) = String.valueOf(getValue(value))

  final def fromDirectString(value: String) = java.lang.Double.valueOf(value)
}
