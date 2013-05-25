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
package collection.ordering

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
object ScalaOrderings {
  final val StdOrderingsSeq = Seq[CollectionOrdering[_, _]](
    of(ByteClass),
    of(ByteRefClass),
    of(IntClass),
    of(IntRefClass),
    of(StringClass)
  )

  final val StdOrderings = StdOrderingsSeq.
    map(o ⇒ (o.javaClass, o)).asInstanceOf[Seq[(Class[_], CollectionOrdering[_, _])]].toMap

  final def of[A : Ordering](javaClass: Class[A]) =
    ScalaCollectionOrdering(javaClass, implicitly[Ordering[A]])
}
