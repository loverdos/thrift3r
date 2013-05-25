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

package com.ckkloverdos.thrift3r.tests

import com.ckkloverdos.thrift3r.Thrift3r
import org.junit.Assert

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
trait BaseFixture {
  final val DefaultThrifter = new Thrift3r()

  protected def check[A <: AnyRef](obj: A, thrifter: Thrift3r = DefaultThrifter) {
    val bytes = thrifter.beanToBytes(obj)
    val bean  = thrifter.bytesToBean(obj.getClass, bytes)
    Assert.assertEquals(obj, bean)
  }
}
