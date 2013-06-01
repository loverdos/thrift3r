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

package com.ckkloverdos.thrift3r.tests.map

import com.ckkloverdos.thrift3r.TTypeEnum
import com.ckkloverdos.thrift3r.tests.BaseFixture
import org.junit.Test

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
class ScalaMapTest extends BaseFixture {
  @Test def testCMap() {
    good(
      BeanCMap(
        scala.collection.Map(
          TTypeEnum.BOOL → TTypeEnum.BOOL.ordinal()
        )
      )
    )
  }

  @Test def testNullCMap() {
    same(
      BeanCMap(null),
      BeanCMap(scala.collection.Map())
    )
  }

  @Test def testOptionCMapSome() {
    good(
      BeanOptionCMap(
        Some(scala.collection.Map(TTypeEnum.BOOL → TTypeEnum.BOOL.ordinal()))
      )
    )
  }

  @Test def testOptionCMapNone() {
    good(
      BeanOptionCMap(
        None
      )
    )
  }

  @Test def testOptionCMapNoneNull() {
    same(
      BeanOptionCMap(null),
      BeanOptionCMap(None)
    )
  }

  @Test def testIMap() {
    good(
      BeanIMap(
        scala.collection.immutable.Map(
          TTypeEnum.MAP → TTypeEnum.MAP.ordinal()
        )
      )
    )
  }

  @Test def testNullIMap() {
    same(
      BeanIMap(null),
      BeanIMap(scala.collection.immutable.Map())
    )
  }

  @Test def testMMap() {
    good(
      BeanMMap(
        scala.collection.mutable.Map(
          TTypeEnum.STRUCT → TTypeEnum.STRUCT.ordinal()
        )
      )
    )
  }

  @Test def testNullMMap() {
    same(
      BeanMMap(null),
      BeanMMap(scala.collection.mutable.Map())
    )
  }
}
