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

package com.ckkloverdos.thrift3r.tests.json

import org.junit.Test
import com.ckkloverdos.thrift3r.tests.BaseFixture

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
class JSONTest extends BaseFixture {
  @Test def test() {
    val bean = Bean(
      name = "John Woo",
      age = 36,
      addresses = Set(
        Address("Hill Road 1", "Valley City", "FUBAR-ZIP"),
        Address("Will Moad 2", "Ralley Valley", "Globul-ZIP")
      )
    )

    val sample = """
      {
        "name" : "John Woo",
        "age" : 36,
        "addresses" : [ {
          "street" : "Hill Road 1",
          "city" : "Valley City",
          "zip" : "FUBAR-ZIP"
        }, {
          "street" : "Will Moad 2",
          "city" : "Ralley Valley",
          "zip" : "Globul-ZIP"
        } ]
      }
    """

    val json = DefaultThrifter.beanToJSON(bean, true)
    val bean2 = DefaultThrifter.jsonToBean(classOf[Bean], json)
    assert(bean == bean2)
  }
}
