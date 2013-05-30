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

import com.ckkloverdos.thrift3r.tests.BaseFixture
import org.junit.Test

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
class PrimitiveTest extends BaseFixture {
  @Test def testByte() { good(BeanByte(1.toByte)) }

  @Test def testBooleanT() { good(BeanBoolean(true)) }

  @Test def testBooleanF() { good(BeanBoolean(false)) }

  @Test def testChar() { good(BeanChar('Y')) }

  @Test def testShort() { good(BeanShort(1.toShort)) }

  @Test def testInt() { good(BeanInt(1)) }

  @Test def testLong() { good(BeanLong(1L)) }

  @Test def testFloat() { good(BeanFloat(1.1.toFloat)) }

  @Test def testDouble() { good(BeanDouble(1.1)) }

  @Test def testFull() { good(BeanFull(1.toByte, true, 'c', 2.toShort, 3, 4.toLong, 5.0.toFloat, 6.0)) }
}
