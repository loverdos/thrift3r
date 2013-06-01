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

import com.ckkloverdos.thrift3r.protocol.Protocol
import com.ckkloverdos.thrift3r.protocol.json.jackson.{JacksonJSONReader, JacksonJSONWriter}
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import java.io.{StringReader, Reader, Writer}

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
object JSONHelpers {
  final val StdJsonFactory = new JsonFactory()
  final val StdPrettyPrinter = new DefaultPrettyPrinter()


  final def jacksonWriterOf(writer: Writer, prettyPrint: Boolean = true, factory: JsonFactory = StdJsonFactory) = {
    val gen = factory.createGenerator(writer)
    if(prettyPrint) { gen.setPrettyPrinter(StdPrettyPrinter) }
    new JacksonJSONWriter(gen, factory)
  }

  final def jacksonReaderOf(reader: Reader, factory: JsonFactory = StdJsonFactory) = {
    val parser = factory.createParser(reader)

    new JacksonJSONReader(parser, factory)
  }

  final def jacksonProtocolForInput(reader: Reader, factory: JsonFactory = StdJsonFactory): Protocol = {
    new JSONProtocol(
      jacksonReaderOf(reader, factory),
      null
    )
  }

  final def jacksonProtocolForInputString(json: String, factory: JsonFactory = StdJsonFactory): Protocol = {
    jacksonProtocolForInput(new StringReader(json), factory)
  }

  final def jacksonProtocolForOutput(
    writer: Writer,
    prettyPrint: Boolean = true,
    factory: JsonFactory = StdJsonFactory
  ) = {
    new JSONProtocol(
      null,
      jacksonWriterOf(writer, prettyPrint, factory)
    )
  }

  final def jacksonProtocolForIO(
    reader: Reader,
    writer: Writer,
    prettyPrint: Boolean = true,
    factory: JsonFactory = StdJsonFactory
  ) = {
    new JSONProtocol(
      jacksonReaderOf(reader, factory),
      jacksonWriterOf(writer, prettyPrint, factory)
    )
  }

}
