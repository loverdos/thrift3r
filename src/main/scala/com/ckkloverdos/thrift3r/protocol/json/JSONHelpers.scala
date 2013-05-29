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

package com.ckkloverdos.thrift3r.protocol.json

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import java.io.{OutputStream, Reader, InputStream, Writer}
import com.ckkloverdos.thrift3r.protocol.Protocol

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
object JSONHelpers {
  final val StdJsonFactory = new JsonFactory()
  final val StdPrettyPrinter = new DefaultPrettyPrinter()

  @inline final def generatorOfWriter(writer: Writer, prettyPrint: Boolean = true) = {
    val jsonGen = StdJsonFactory.createGenerator(writer)
    if(prettyPrint) { jsonGen.setPrettyPrinter(StdPrettyPrinter) }
    jsonGen
  }

  @inline final def parserOfReader(reader: Reader) = StdJsonFactory.createParser(reader)

  @inline final def jsonProtocolForInput(reader: Reader): Protocol =
    new JSONProtocol(
      StdJsonFactory,
      null,
      parserOfReader(reader)
    )

  @inline final def jsonProtocolForOutput(writer: Writer, prettyPrint: Boolean = true) =
    new JSONProtocol(
      StdJsonFactory,
      generatorOfWriter(writer, prettyPrint),
      null
    )

  @inline final def jsonProtocolForIO(reader: Reader, writer: Writer, prettyPrint: Boolean = true) =
    new JSONProtocol(
      StdJsonFactory,
      generatorOfWriter(writer, prettyPrint),
      parserOfReader(reader)
    )
}
