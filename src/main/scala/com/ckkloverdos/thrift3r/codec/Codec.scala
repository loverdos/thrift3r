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

import org.apache.thrift.protocol.TProtocol
import com.ckkloverdos.thrift3r.TTypeEnum
import com.google.common.reflect.TypeToken

/**
 * A thrift codec for a particular type `T`.
 * A codec supports only one [[com.ckkloverdos.thrift3r.TTypeEnum]].
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
trait Codec[T] {
  /**
   * The supported [[com.ckkloverdos.thrift3r.TTypeEnum]].
   */
  def tTypeEnum: TTypeEnum

  final def tType = tTypeEnum.ttype

  def typeToken: TypeToken[T]

  final def jvmType: JType = typeToken.getType

  final def jvmClass: Class[T] = typeToken.getRawType.asInstanceOf[Class[T]]

  def encode(protocol: TProtocol, value: T)

  def decode(protocol: TProtocol): T
}
