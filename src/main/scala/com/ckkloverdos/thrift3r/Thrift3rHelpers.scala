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

package com.ckkloverdos.thrift3r

import java.util.concurrent.ExecutionException
import com.google.common.util.concurrent.UncheckedExecutionException
import org.apache.thrift.transport.TTransport
import org.apache.thrift.protocol.{TBinaryProtocol, TCompactProtocol, TProtocol}
import scala.annotation.tailrec

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
object Thrift3rHelpers {


  def capitalize(name: String) = {
    name.substring(0, 1).toUpperCase + name.substring(1)
  }

  def getterOf(javaClass: JClass, fieldName: String, fieldType: JType): (AnyRef) ⇒ Any = {
    // 1. Find a public method with the same name, no args and a matching return type
    val methods = javaClass.getMethods
    methods.find { case method ⇒
      method.getGenericReturnType == fieldType && {
        method.getName == fieldName ||
          method.getName == "get" + capitalize(fieldName) ||
          method.getName == "is" + capitalize(fieldName)
      }
    } match {
      case Some(method) ⇒
        (bean: AnyRef) ⇒ method.invoke(bean)

      case None ⇒
        throw new IllegalArgumentException(
          "Unknown field %s [: %s] for %s".format(
            fieldName,
            fieldType,
            javaClass
          ))
    }
  }

  @tailrec
  final def unwrapExecutionExceptionCause(e: Throwable): Throwable = {
    e match {
      case checked: ExecutionException ⇒
        unwrapExecutionExceptionCause(checked.getCause)

      case unchecked: UncheckedExecutionException ⇒
        unwrapExecutionExceptionCause(unchecked.getCause)

      case _ ⇒
        e
    }
  }

  final def valueAndTypeStr(v: Any) =
    "%s: %s".format(
      v,
      v match {
        case null ⇒ "Null"
        case v: AnyRef ⇒ v.getClass.getName
      }
    )

  def newTBinaryProtocol(
    transport: TTransport,
    strictRead: Boolean = true,
    strictWrite: Boolean = true
  ): TProtocol = new TBinaryProtocol(transport, strictRead, strictWrite) {
    override def toString =
      "TBinaryProtocol(%s, %s, %s)".format(
        transport.getClass.getSimpleName,
        strictRead,
        strictWrite
      )
  }

  def newTCompactProtocol(
    transport: TTransport,
    maxNetworkBytes: Int = -1
  ): TProtocol = new TCompactProtocol(transport, maxNetworkBytes) {
    override def toString =
      "TCompactProtocol(%s, %s)".format(
        transport.getClass.getSimpleName,
        maxNetworkBytes
      )
  }

  def defaultProtocolFor(transport: TTransport): TProtocol = newTCompactProtocol(transport)
}
