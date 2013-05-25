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
  type JType = java.lang.reflect.Type
  type JClass = Class[_]

  type IntRef = java.lang.Integer

  @inline final val ByteClass = classOf[Byte]
  @inline final val ByteRefClass = classOf[java.lang.Byte]
  @inline final val ShortClass = classOf[Short]
  @inline final val ShortRefClass = classOf[java.lang.Short]
  @inline final val IntClass = classOf[Int]
  @inline final val IntRefClass = classOf[java.lang.Integer]
  @inline final val LongClass = classOf[Long]
  @inline final val LongRefClass = classOf[java.lang.Long]
  @inline final val DoubleClass = classOf[Double]
  @inline final val DoubleRefClass = classOf[java.lang.Double]
  @inline final val StringClass = classOf[String]

  @inline final def typeTokenOf[A](jvmClass: Class[A]): TypeToken[A] = TypeToken.of(jvmClass)

  @inline final def typeTokenOf(jvmType: JType): TypeToken[_] = TypeToken.of(jvmType)

  @inline final def jvmClassOf[T](typeToken: TypeToken[T]): Class[T] = typeToken.getRawType.asInstanceOf[Class[T]]

  @inline final def jvmTypeOf[T](typeToken: TypeToken[T]): JType = typeToken.getType

  @inline final def typeParameterOf[A] = new TypeParameter[A] {}

  @inline final def jvmTypeOf[A](jvmClass: Class[A]): JType = typeTokenOf(jvmClass).getType
}
