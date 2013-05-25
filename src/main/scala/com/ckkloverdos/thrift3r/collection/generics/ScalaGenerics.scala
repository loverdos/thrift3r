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
package collection.generics

import com.google.common.reflect.TypeToken
import scala.collection.{Set ⇒ CSet, Seq ⇒ CSeq, Map ⇒ CMap, Iterator ⇒ CIterator}

/**
 * Computations of generics for Scala collections types.
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
object ScalaGenerics {
  final private[this] val (mapKeyGenericJVMType, mapValueGenericJVMType) = {
    val applyMethod = classOf[CMap[_, _]].getMethod("apply", classOf[AnyRef])
    val keyJVMType = applyMethod.getGenericParameterTypes()(0)
    val valueJVMType = applyMethod.getGenericReturnType

    // Get "A", "B" from "Map[A, B]"
    (keyJVMType, valueJVMType)
  }

  final private[this] val (seqJVMType, seqElementGenericJVMType) = {
    val iteratorJVMType = classOf[CSeq[_]].getMethod("iterator").getGenericReturnType
    val iteratorElementJVMType = classOf[CIterator[_]].getMethod("next").getGenericReturnType

    // Get "A" from "Seq[A]"
    (iteratorJVMType, iteratorElementJVMType)
  }

  final private[this] val optionElementGenericJVMType = {
    classOf[Option[_]].getMethod("get").getGenericReturnType
  }

  final def elementTypeOfTraversable(jvmType: JType): JType =
    typeTokenOf(jvmType).resolveType(seqJVMType).resolveType(seqElementGenericJVMType).getType

  final def elementTypeOfSeq(jvmType: JType): JType = elementTypeOfTraversable(jvmType)

  final def elementTypeOfSet(jvmType: JType): JType = elementTypeOfTraversable(jvmType)

  final def keyTypeOfMap(jvmType: JType): JType =
    typeTokenOf(jvmType).resolveType(mapKeyGenericJVMType).getType

  final def valueTypeOfMap(jvmType: JType): JType =
    typeTokenOf(jvmType).resolveType(mapValueGenericJVMType).getType

  final def elementTypeOfOption(jvmType: JType): JType =
    typeTokenOf(jvmType).resolveType(optionElementGenericJVMType).getType
}
