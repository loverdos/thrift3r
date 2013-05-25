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
package misc

import com.ckkloverdos.thrift3r.TTypeEnum
import org.apache.thrift.protocol.TProtocol
import com.google.common.reflect.TypeToken

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case class ClassCodec(loader: ClassLoader) extends Codec[JClass]{
  def tTypeEnum = TTypeEnum.STRING

  def typeToken = typeTokenOf(classOf[JClass]).asInstanceOf[TypeToken[JClass]]

  def encode(protocol: TProtocol, value: JClass) {
    protocol.writeString(value.getName)
  }

  def decode(protocol: TProtocol) = {
    val name = protocol.readString()
    loader.loadClass(name)
  }
}
