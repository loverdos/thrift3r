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
