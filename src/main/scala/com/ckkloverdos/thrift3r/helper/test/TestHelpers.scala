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
class TestHelpers {
  def goodThrift[A <: AnyRef](obj: A, thrifter: Thrift3r) {
    val bytes = thrifter.beanToBytes(obj)
    val bean  = thrifter.bytesToBean(obj.getClass, bytes)
    require(obj == bean)
  }

  def goodJSON[A <: AnyRef](obj: A, thrifter: Thrift3r) {
    val json = thrifter.beanToJSON(obj)
    val bean  = thrifter.jsonToBean(obj.getClass, json)
    require(obj == bean)
  }

  def goodJSONH[A <: AnyRef](obj: A, thrifter: Thrift3r)(jsonHandler: (String) ⇒ Any = _ ⇒ {}) {
    val json = thrifter.beanToJSON(obj)
    jsonHandler(json)
    val bean  = thrifter.jsonToBean(obj.getClass, json)
    require(obj == bean)
  }

  def good[A <: AnyRef](obj: A, thrifter: Thrift3r) {
    goodThrift(obj, thrifter)
    goodJSON(obj, thrifter)
  }

  def goodH[A <: AnyRef](obj: A, thrifter: Thrift3r)(jsonHandler: (String) ⇒ Any = _ ⇒ {}) {
    goodThrift(obj, thrifter)
    goodJSONH(obj, thrifter)(jsonHandler)
  }

  def badThrift[A <: AnyRef](obj: A, thrifter: Thrift3r) {
    try {
      thrifter.beanToBytes(obj)
    } catch {
      case e: Throwable ⇒
      return
    }

    require(false, "Encoding of %s should have failed".format(obj))
  }

  def sameThrift[A <: AnyRef](x: A, y: A, thrifter: Thrift3r) {
    val xBytes = thrifter.beanToBytes(x)
    val yBytes = thrifter.beanToBytes(y)
    require(java.util.Arrays.equals(xBytes, yBytes))

    val x2 = thrifter.bytesToBean(x.getClass, xBytes)
    val y2 = thrifter.bytesToBean(y.getClass, yBytes)
    require(x2 == y2)
  }

  def sameJSON[A <: AnyRef](x: A, y: A, thrifter: Thrift3r) {
    val xJSON = thrifter.beanToJSON(x)
    val yJSON = thrifter.beanToJSON(y)
    require(xJSON == yJSON)

    val x2 = thrifter.jsonToBean(x.getClass, xJSON)
    val y2 = thrifter.jsonToBean(y.getClass, yJSON)
    require(x2 == y2)
  }

  def sameJSONH[A <: AnyRef](x: A, y: A, thrifter: Thrift3r)(jsonHandler: (String) ⇒ Any = _ ⇒ {}) {
    val xJSON = thrifter.beanToJSON(x)
    jsonHandler(xJSON)
    val yJSON = thrifter.beanToJSON(y)
    jsonHandler(yJSON)
    require(xJSON == yJSON)

    val x2 = thrifter.jsonToBean(x.getClass, xJSON)
    val y2 = thrifter.jsonToBean(y.getClass, yJSON)
    require(x2 == y2)
  }

  def same[A <: AnyRef](x: A, y: A, thrifter: Thrift3r) {
    sameThrift(x, y, thrifter)
    sameJSON(x, y, thrifter)
  }

  def sameH[A <: AnyRef](x: A, y: A, thrifter: Thrift3r)(jsonHandler: (String) ⇒ Any = _ ⇒ {}) {
    sameThrift(x, y, thrifter)
    sameJSONH(x, y, thrifter)(jsonHandler)
  }
}
