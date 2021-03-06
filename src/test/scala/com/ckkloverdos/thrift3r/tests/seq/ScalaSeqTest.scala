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

package com.ckkloverdos.thrift3r.tests.seq

import com.ckkloverdos.thrift3r.tests.BaseFixture
import org.junit.Test

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
class ScalaSeqTest extends BaseFixture {
  @Test def testC() { good(BeanCSeq(collection.Seq("One", "Two"))) }

  @Test def testNullC() { same(BeanCSeq(null), BeanCSeq(collection.Seq())) }

  @Test def testI() { good(BeanISeq(collection.immutable.Seq("One", "Two"))) }

  @Test def testNullI() { same(BeanISeq(null), BeanISeq(collection.immutable.Seq())) }

  @Test def testM() { good(BeanMSeq(collection.mutable.Seq("One", "Two"))) }

  @Test def testNullM() { same(BeanMSeq(null), BeanMSeq(collection.mutable.Seq())) }
}
