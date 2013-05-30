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

  protected def goodThrift[A <: AnyRef](obj: A, thrifter: Thrift3r = DefaultThrifter) {
    val bytes = thrifter.beanToBytes(obj)
    val bean  = thrifter.bytesToBean(obj.getClass, bytes)
    require(obj == bean)
  }

  protected def goodJSON[A <: AnyRef](obj: A, thrifter: Thrift3r = DefaultThrifter) {
    println(obj)
    val json = thrifter.beanToJSON(obj)
    println(json)
    val bean  = thrifter.jsonToBean(obj.getClass, json)
    println(bean)
    require(obj == bean)
  }

  protected def good[A <: AnyRef](obj: A, thrifter: Thrift3r = DefaultThrifter) {
    goodThrift(obj, thrifter)
    goodJSON(obj, thrifter)
  }

  protected def badThrift[A <: AnyRef](obj: A, thrifter: Thrift3r = DefaultThrifter) {
    try {
      thrifter.beanToBytes(obj)
    } catch {
      case e: Throwable ⇒
//        System.out.println("Ignoring: %s".format(e))
//        for(trace0 ← e.getStackTrace) {
//          System.out.println("      at: %s.%s(%s:%s)".format(trace0.getClassName, trace0.getMethodName, trace0.getFileName, trace0.getLineNumber))
//        }
        return
    }

    assert(false, "Encoding of %s should have failed".format(obj))
  }

  protected def sameThrift[A <: AnyRef](x: A, y: A, thrifter: Thrift3r = DefaultThrifter) {
    val xBytes = thrifter.beanToBytes(x)
    val yBytes = thrifter.beanToBytes(y)
    Assert.assertArrayEquals(xBytes, yBytes)

    val x2 = thrifter.bytesToBean(x.getClass, xBytes)
    val y2 = thrifter.bytesToBean(y.getClass, yBytes)
    Assert.assertEquals(x2, y2)
  }

  protected def sameJSON[A <: AnyRef](x: A, y: A, thrifter: Thrift3r = DefaultThrifter) {
    val xJSON = thrifter.beanToJSON(x)
    val yJSON = thrifter.beanToJSON(y)
    require(xJSON == yJSON)

    val x2 = thrifter.jsonToBean(x.getClass, xJSON)
    val y2 = thrifter.jsonToBean(y.getClass, yJSON)
    require(x2 == y2)
  }

  protected def same[A <: AnyRef](x: A, y: A, thrifter: Thrift3r = DefaultThrifter) {
    sameThrift(x, y, thrifter)
    sameJSON(x, y, thrifter)
  }
}
