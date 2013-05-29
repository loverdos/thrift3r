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
trait MapProtocol { this: Protocol ⇒
  def writeMapBegin[K, V](keyCodec: Codec[K], valueCodec: Codec[V], size: Int)

  def writeMapElement[K, V](key: K, keyCodec: Codec[K], value: V, valueCodec: Codec[V])

  def writeMapEnd()

  def writeEmptyMap[K, V](keyCodec: Codec[K], valueCodec: Codec[V]) {
    writeMapBegin(keyCodec, valueCodec, 0)
    writeMapEnd()
  }

  def readMapElement[K, V](keyCodec: Codec[K], valueCodec: Codec[V]): (K, V)

  final def isSizedMapProtocol = this.isInstanceOf[SizedMapProtocol]
  final def asSizedMapProtocol = this.asInstanceOf[SizedMapProtocol]
  final def asUnsizedMapProtocol = this.asInstanceOf[UnsizedMapProtocol]
}

trait SizedMapProtocol extends MapProtocol { this: Protocol ⇒
  def readMapBegin(): Int

  def readMapEnd()
}

trait UnsizedMapProtocol extends MapProtocol { this: Protocol ⇒
  final def writeMapBegin[K, V](keyCodec: Codec[K], valueCodec: Codec[V], size: Int) =
    writeMapBegin(keyCodec, valueCodec)

  def writeMapBegin[K, V](keyCodec: Codec[K], valueCodec: Codec[V])

  def readMapBegin()

  /**
   * Returns `true` if it reaches the map end.
   */
  def readMapEnd(): Boolean
}


