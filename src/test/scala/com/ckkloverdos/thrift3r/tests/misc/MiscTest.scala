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
  @Test def testClass() { goodThrift(BeanClass(classOf[MiscTest])) }

  @Test def testOptionIntNone() { badThrift(BeanOptionInt(None)) }
  @Test def testOptionIntSome() { badThrift(BeanOptionInt(Some(1))) }

  @Test def testOptionIntRefSome() { goodThrift(BeanOptionIntRef(Some(1))) }
  @Test def testOptionIntRefNone() { goodThrift(BeanOptionIntRef(None)) }

  @Test def testOptionStructSomeSome() { goodThrift(BeanOptionStruct(Some(BeanOptionString(Some("yes"))))) }
  @Test def testOptionStructSomeNone() { goodThrift(BeanOptionStruct(Some(BeanOptionString(None)))) }
  @Test def testOptionStructNone() { goodThrift(BeanOptionStruct(None)) }
}
