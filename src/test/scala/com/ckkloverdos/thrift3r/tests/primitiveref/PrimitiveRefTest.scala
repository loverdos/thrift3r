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
  @Test def testByteRef() { goodThrift(BeanByteRef(1.toByte)) }

  @Test def testNullByteRef() { sameThrift(BeanByteRef(null), BeanByteRef(0.toByte)) }

  @Test def testBooleanRefT() { goodThrift(BeanBooleanRef(true)) }

  @Test def testBooleanRefF() { goodThrift(BeanBooleanRef(false)) }

  @Test def testNullBooleanRef() { sameThrift(BeanBooleanRef(null), BeanBooleanRef(false))}

  @Test def testCharRef() { goodThrift(BeanCharRef('Y')) }

  @Test def testNullCharRef() { sameThrift(BeanCharRef(null), BeanCharRef('\u0000')) }

  @Test def testShortRef() { goodThrift(BeanShortRef(1.toShort)) }

  @Test def testNullShortRef() { sameThrift(BeanShortRef(null), BeanShortRef(0.toShort)) }

  @Test def testIntRef() { goodThrift(BeanIntRef(1)) }

  @Test def testNullIntRef() { sameThrift(BeanIntRef(null), BeanIntRef(0)) }

  @Test def testLongRef() { goodThrift(BeanLongRef(1L)) }

  @Test def testNullLongRef() { sameThrift(BeanLongRef(null), BeanLongRef(0L)) }

  @Test def testFloatRef() { goodThrift(BeanFloatRef(1.1.toFloat)) }

  @Test def testNullFloatRef() { sameThrift(BeanFloatRef(null), BeanFloatRef(0.0.toFloat)) }

  @Test def testDoubleRef() { goodThrift(BeanDoubleRef(1.1)) }

  @Test def testNullDoubleRef() { sameThrift(BeanDoubleRef(null), BeanDoubleRef(0.0)) }

  @Test def testFullRef() { goodThrift(BeanFullRef(1.toByte, true, 'c', 2.toShort, 3, 4.toLong, 5.0.toFloat, 6.0)) }
}