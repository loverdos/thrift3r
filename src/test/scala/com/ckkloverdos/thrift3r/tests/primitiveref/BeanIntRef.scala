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
package tests.primitiveref

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case class BeanIntRef(v: IntRef)

case class BeanByteRef(v: ByteRef)

case class BeanBooleanRef(v: BooleanRef)

case class BeanCharRef(v: CharRef)

case class BeanShortRef(v: ShortRef)

case class BeanLongRef(v: LongRef)

case class BeanFloatRef(v: FloatRef)

case class BeanDoubleRef(v: DoubleRef)

case class BeanFullRef(
  byte: ByteRef,
  boolean: BooleanRef,
  char: CharRef,
  short: ShortRef,
  int: IntRef,
  long: LongRef,
  float: FloatRef,
  double: DoubleRef
)
