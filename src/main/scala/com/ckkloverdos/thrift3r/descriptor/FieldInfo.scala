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

package com.ckkloverdos.thrift3r.descriptor

import com.ckkloverdos.thrift3r.codec.Codec

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
final case class FieldInfo(field: FieldDescriptor, codec: Codec[_]) {
  def jvmType = field.jvmType
  def jvmClass = field.jvmClass
  def id =  field.id
  def name = field.name
}
