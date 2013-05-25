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

package com.ckkloverdos.thrift3r.collection.builder

/**
 *
 * @tparam A The element type
 * @tparam C The collection type
 * @tparam M The type of extra information needed to create the builder
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
trait CollectionBuilderFactory[A, C, M] {
  def newBuilder(meta: M): CollectionBuilder[A, C]
}

/**
 * A [[com.ckkloverdos.thrift3r.collection.builder.CollectionBuilderFactory]] with no extra information.
 *
 * @tparam A The element type
 * @tparam C The collection type
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
trait StdCollectionBuilderFactory[A, C] extends CollectionBuilderFactory[A, C, Unit]

trait ScalaCollectionBuilderFactory[A, C] extends StdCollectionBuilderFactory[A, C]
trait ScalaOrderedTraversableBuilderFactory[A, C] extends CollectionBuilderFactory[A, C, Ordering[A]]
trait ScalaOrderedMapBuilderFactory[A, B, C] extends CollectionBuilderFactory[(A, B), C, Ordering[A]]
