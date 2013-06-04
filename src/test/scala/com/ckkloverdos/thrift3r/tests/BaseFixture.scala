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
import com.ckkloverdos.thrift3r.helper.test.TestHelpers

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
trait BaseFixture {
  val DefaultThrifter = new Thrift3r()
  val helpers = new TestHelpers

  protected def goodThrift[A <: AnyRef](obj: A, thrifter: Thrift3r = DefaultThrifter) {
    helpers.goodThrift(obj, thrifter)
  }

  protected def goodJSON[A <: AnyRef](obj: A, thrifter: Thrift3r = DefaultThrifter) {
    helpers.goodJSON(obj, thrifter)
  }

  protected def good[A <: AnyRef](obj: A, thrifter: Thrift3r = DefaultThrifter) {
    helpers.good(obj, thrifter)
  }

  protected def badThrift[A <: AnyRef](obj: A, thrifter: Thrift3r = DefaultThrifter) {
    helpers.badThrift(obj, thrifter)
  }

  protected def sameThrift[A <: AnyRef](x: A, y: A, thrifter: Thrift3r = DefaultThrifter) {
    helpers.sameThrift(x, y, thrifter)
  }

  protected def sameJSON[A <: AnyRef](x: A, y: A, thrifter: Thrift3r = DefaultThrifter) {
    helpers.sameJSON(x, y, thrifter)
  }

  def same[A <: AnyRef](x: A, y: A, thrifter: Thrift3r = DefaultThrifter) {
    helpers.same(x, y, thrifter)
  }
}
