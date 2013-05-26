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

package com.ckkloverdos.thrift3r.tests.primitive

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case class BeanInt(v: Int)

case class BeanByte(v: Byte)
case class BeanBoolean(v: Boolean)
case class BeanChar(v: Char)
case class BeanShort(v: Short)
case class BeanLong(v: Long)
case class BeanFloat(v: Float)
case class BeanDouble(v: Double)

case class BeanFull(
  byte: Byte,
  boolean: Boolean,
  char: Char,
  short: Short,
  int: Int,
  long: Long,
  float: Float,
  double: Double
)
