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

package com.ckkloverdos

import com.google.common.reflect.{TypeParameter, TypeToken}

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
package object thrift3r {
  def println(x: Any) { Predef.println(x) }

  type JType = java.lang.reflect.Type
  type JClass = Class[_]

  type Bool = Boolean
  type Int8 = Byte
  type Int16 = Short
  type Int32 = Int
  type Int64 = Long
  type Float32 = Float
  type Float64 = Double

  type ByteRef = java.lang.Byte
  type BooleanRef = java.lang.Boolean
  type CharRef = java.lang.Character
  type ShortRef = java.lang.Short
  type IntRef = java.lang.Integer
  type LongRef = java.lang.Long
  type FloatRef = java.lang.Float
  type DoubleRef = java.lang.Double

  @inline final val ByteClass = classOf[Byte]
  @inline final val ByteRefClass = classOf[ByteRef]
  @inline final val BooleanClass = classOf[Boolean]
  @inline final val BooleanRefClass = classOf[BooleanRef]
  @inline final val CharClass = classOf[Char]
  @inline final val CharRefClass = classOf[CharRef]
  @inline final val ShortClass = classOf[Short]
  @inline final val ShortRefClass = classOf[ShortRef]
  @inline final val IntClass = classOf[Int]
  @inline final val IntRefClass = classOf[IntRef]
  @inline final val LongClass = classOf[Long]
  @inline final val LongRefClass = classOf[LongRef]
  @inline final val FloatClass = classOf[Float]
  @inline final val FloatRefClass = classOf[FloatRef]
  @inline final val DoubleClass = classOf[Double]
  @inline final val DoubleRefClass = classOf[DoubleRef]
  @inline final val StringClass = classOf[String]

  @inline final def typeTokenOfClass[A](jvmClass: Class[A]): TypeToken[A] = TypeToken.of(jvmClass)

  @inline final def typeTokenOfType(jvmType: JType): TypeToken[_] = TypeToken.of(jvmType)

  @inline final def jvmClassOf[T](typeToken: TypeToken[T]): Class[T] = typeToken.getRawType.asInstanceOf[Class[T]]

  @inline final def jvmTypeOf[T](typeToken: TypeToken[T]): JType = typeToken.getType

  @inline final def typeParameterOf[A] = new TypeParameter[A] {}

  @inline final def jvmTypeOf[A](jvmClass: Class[A]): JType = typeTokenOfClass(jvmClass).getType
}
