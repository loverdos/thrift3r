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

import com.google.common.reflect.TypeToken
import java.lang.reflect.Constructor
import org.apache.thrift.protocol.TStruct
import scala.collection.immutable

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case class StructDescriptor(
  name: String,
  fullName: String,
  fields: immutable.SortedMap[Short, FieldDescriptor],
  typeToken: TypeToken[_],
  constructor: Constructor[_]
) extends Descriptor {

  final val arity = fields.size

  final val thriftStruct: TStruct = new TStruct(fullName)

  def construct(params: Array[AnyRef]): AnyRef = constructor.newInstance(params:_*).asInstanceOf[AnyRef]
}
