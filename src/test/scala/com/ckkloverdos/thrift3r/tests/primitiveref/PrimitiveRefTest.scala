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

package com.ckkloverdos.thrift3r.tests.primitiveref

import com.ckkloverdos.thrift3r.tests.BaseFixture
import org.junit.Test

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
class PrimitiveRefTest extends BaseFixture {
  @Test def testByteRef() { good(BeanByteRef(1.toByte)) }

  @Test def testNullByteRef() { same(BeanByteRef(null), BeanByteRef(0.toByte)) }

  @Test def testBooleanRefT() { good(BeanBooleanRef(true)) }

  @Test def testBooleanRefF() { good(BeanBooleanRef(false)) }

  @Test def testNullBooleanRef() { same(BeanBooleanRef(null), BeanBooleanRef(false))}

  @Test def testCharRef() { good(BeanCharRef('Y')) }

  @Test def testNullCharRef() { same(BeanCharRef(null), BeanCharRef('\u0000')) }

  @Test def testShortRef() { good(BeanShortRef(1.toShort)) }

  @Test def testNullShortRef() { same(BeanShortRef(null), BeanShortRef(0.toShort)) }

  @Test def testIntRef() { good(BeanIntRef(1)) }

  @Test def testNullIntRef() { same(BeanIntRef(null), BeanIntRef(0)) }

  @Test def testLongRef() { good(BeanLongRef(1L)) }

  @Test def testNullLongRef() { same(BeanLongRef(null), BeanLongRef(0L)) }

  @Test def testFloatRef() { good(BeanFloatRef(1.1.toFloat)) }

  @Test def testNullFloatRef() { same(BeanFloatRef(null), BeanFloatRef(0.0.toFloat)) }

  @Test def testDoubleRef() { good(BeanDoubleRef(1.1)) }

  @Test def testNullDoubleRef() { same(BeanDoubleRef(null), BeanDoubleRef(0.0)) }

  @Test def testFullRef() { good(BeanFullRef(1.toByte, true, 'c', 2.toShort, 3, 4.toLong, 5.0.toFloat, 6.0)) }
}
