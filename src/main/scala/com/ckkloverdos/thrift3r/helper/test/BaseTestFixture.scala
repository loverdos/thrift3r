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

package com.ckkloverdos.thrift3r.helper.test

import com.ckkloverdos.thrift3r.Thrift3r

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
trait BaseTestFixture {
  val StdThrifter = new Thrift3r()
  val Helpers = new TestHelpers

  protected def goodThrift[A <: AnyRef](obj: A, thrifter: Thrift3r = StdThrifter) {
    Helpers.goodThrift(obj, thrifter)
  }

  protected def goodJSON[A <: AnyRef](obj: A, thrifter: Thrift3r = StdThrifter) {
    Helpers.goodJSON(obj, thrifter)
  }

  protected def goodJSONH[A <: AnyRef](obj: A, thrifter: Thrift3r = StdThrifter)
                                      (jsonHandler: (String) ⇒ Any = _ ⇒ {}) {
    Helpers.goodJSONH(obj, thrifter)(jsonHandler)
  }

  protected def good[A <: AnyRef](obj: A, thrifter: Thrift3r = StdThrifter) {
    Helpers.good(obj, thrifter)
  }

  protected def goodH[A <: AnyRef](obj: A, thrifter: Thrift3r = StdThrifter)
                                  (jsonHandler: (String) ⇒ Any = _ ⇒ {}) {
    Helpers.goodH(obj, thrifter)(jsonHandler)
  }

  protected def badThrift[A <: AnyRef](obj: A, thrifter: Thrift3r = StdThrifter) {
    Helpers.badThrift(obj, thrifter)
  }

  protected def sameThrift[A <: AnyRef](x: A, y: A, thrifter: Thrift3r = StdThrifter) {
    Helpers.sameThrift(x, y, thrifter)
  }

  protected def sameJSON[A <: AnyRef](x: A, y: A, thrifter: Thrift3r = StdThrifter) {
    Helpers.sameJSON(x, y, thrifter)
  }

  protected def sameJSONH[A <: AnyRef](x: A, y: A, thrifter: Thrift3r = StdThrifter)
                                      (jsonHandler: (String) ⇒ Any = _ ⇒ {}) {
    Helpers.sameJSONH(x, y, thrifter)(jsonHandler)
  }

  protected def same[A <: AnyRef](x: A, y: A, thrifter: Thrift3r = StdThrifter) {
    Helpers.same(x, y, thrifter)
  }

  protected def sameH[A <: AnyRef](x: A, y: A, thrifter: Thrift3r = StdThrifter)
                                  (jsonHandler: (String) ⇒ Any = _ ⇒ {}) {
    Helpers.sameH(x, y, thrifter)(jsonHandler)
  }

}
