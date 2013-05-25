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

package com.ckkloverdos.thrift3r.collection.descriptor

import com.ckkloverdos.thrift3r.collection.builder.CollectionBuilderFactory
import com.ckkloverdos.thrift3r.descriptor.Descriptor
import com.google.common.reflect.TypeToken

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case class CollectionDescriptor(
  typeToken: TypeToken[_],
  builderFactory: CollectionBuilderFactory[_, _, _],
  isOrdered: Boolean
) extends Descriptor {
  // def typeParameterTokens: List[TypeToken[_]] // List(A) for Seq[A] and List(A, B) for Map[A, B]
  final def name = jvmClass.getName
}
