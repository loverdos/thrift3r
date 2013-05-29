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
trait SetProtocol { this: Protocol ⇒
  def writeSetBegin[T](elementCodec: Codec[T], size: Int)

  def writeSetElement[T](element: T, elementCodec: Codec[T])

  def writeSetEnd()

  def writeEmptySet[T](elementCodec: Codec[T]) {
    writeSetBegin(elementCodec, 0)
    writeSetEnd()
  }

  def readSetElement[T](elementCodec: Codec[T]): T

  final def isSizedSetProtocol = this.isInstanceOf[SizedSetProtocol]
  final def asSizedSetProtocol = this.asInstanceOf[SizedSetProtocol]
  final def asUnsizedSetProtocol = this.asInstanceOf[UnsizedSetProtocol]
}

/**
 * Protocol for sets of known size
 */
trait SizedSetProtocol extends SetProtocol { this: Protocol ⇒
  /**
   * Returns the Set size.
   */
  def readSetBegin(): Int

  def readSetEnd()
}

trait UnsizedSetProtocol extends SetProtocol { this: Protocol ⇒
  final def writeSetBegin[T](elementCodec: Codec[T], size: Int) = writeSetBegin(elementCodec)

  def writeSetBegin[T](elementCodec: Codec[T])

  def readSetBegin()

  /**
   * Returns `true` if it reaches the set end.
   */
  def readSetEnd(): Boolean
}


