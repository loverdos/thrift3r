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
trait ListProtocol { this: Protocol ⇒
  def writeListBegin[T](elementCodec: Codec[T], size: Int)

  def writeListElement[T](element: T, elementCodec: Codec[T])

  def writeListEnd()

  def writeEmptyList[T](elementCodec: Codec[T]) {
    writeListBegin(elementCodec, 0)
    writeListEnd()
  }

  def readListElement[T](elementCodec: Codec[T]): T

  final def isSizedListProtocol = this.isInstanceOf[SizedListProtocol]
  final def asSizedListProtocol = this.asInstanceOf[SizedListProtocol]
  final def asUnsizedListProtocol = this.asInstanceOf[UnsizedListProtocol]
}

/**
 * Protocol for lists of known size
 */
trait SizedListProtocol extends ListProtocol { this: Protocol ⇒
  /**
   * Returns the list size.
   */
  def readListBegin(): Int

  def readListEnd()
}

trait UnsizedListProtocol extends ListProtocol { this: Protocol ⇒
  final def writeListBegin[T](elementCodec: Codec[T], size: Int) = writeListBegin(elementCodec)

  def writeListBegin[T](elementCodec: Codec[T])

  def readListBegin()

  /**
   * Returns `true` if it reaches the list end.
   */
  def readListEnd(): Boolean
}

