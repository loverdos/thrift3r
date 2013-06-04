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

package com.ckkloverdos.thrift3r.tests.misc

import com.ckkloverdos.thrift3r.tests.BaseFixture
import org.junit.Test

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
class MiscTest extends BaseFixture {
  @Test def testClass() { good(BeanClass(classOf[MiscTest])) }

  @Test def testOptionIntNone() { badThrift(BeanOptionInt(None)) }

  @Test def testOptionIntSome() { badThrift(BeanOptionInt(Some(1))) }

  @Test def testOptionIntRefSome() { good(BeanOptionIntRef(Some(1))) }
  @Test def testOptionIntRefNone() { good(BeanOptionIntRef(None)) }

  @Test def testOptionStructSomeSome() { good(BeanOptionStruct(Some(BeanOptionString(Some("yes"))))) }
  @Test def testOptionStructSomeNone() { good(BeanOptionStruct(Some(BeanOptionString(None)))) }
  @Test def testOptionStructNone() { good(BeanOptionStruct(None)) }
}
