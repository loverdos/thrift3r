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

package com.ckkloverdos.thrift3r.codec

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
trait CodecToString { this: Codec[_] ⇒
  protected def codecPrefix =
    if(this.isInstanceOf[Product]) this.asInstanceOf[Product].productPrefix
    else this.getClass.getSimpleName

  protected final lazy val stdToStringElements = List(tTypeEnum, typeToken)
  protected def extraToStringElements: List[Any] = Nil
  protected final lazy val toStringElements = stdToStringElements ++ extraToStringElements

  override def toString = "%s(%s)".format(codecPrefix, toStringElements.mkString(", "))
}
