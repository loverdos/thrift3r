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

import com.ckkloverdos.thrift3r.codec.Codec
import com.ckkloverdos.thrift3r.codec.collection.ScalaMapCodec
import com.ckkloverdos.thrift3r.codec.struct.StructCodec
import com.ckkloverdos.thrift3r.collection.descriptor.CollectionDescriptor
import com.ckkloverdos.thrift3r.collection.ordering.CollectionOrdering
import com.ckkloverdos.thrift3r.helper.{ScalaMapBasedStructCodec, StringBasedStructCodec, FuncBasedStructCodec}
import com.ckkloverdos.thrift3r.protocol.Protocol
import com.thoughtworks.paranamer.{DefaultParanamer, BytecodeReadingParanamer, AnnotationParanamer, AdaptiveParanamer, Paranamer}
import scala.collection.{Set ⇒ CSet, Seq ⇒ CSeq, Map ⇒ CMap, GenMap}

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
class Thrift3rBuilder {
  private[this] var _userCodecs: Map[Class[_], Codec[_]] = Map()

  private[this] var _userCollectionDescriptors: Map[JClass, CollectionDescriptor] = Map()

  private[this] var _userCollectionOrderings: Map[JClass, CollectionOrdering[_, _]] = Map()

  private[this] var _paranamer: Paranamer = new AdaptiveParanamer(
    new AnnotationParanamer(new BytecodeReadingParanamer),
    new DefaultParanamer
  )

  private[this] var _classLoader: ClassLoader = Thread.currentThread().getContextClassLoader

  private[this] val thrifter = new Thrift3r()

  def addCodec[T](codec: Codec[T]): this.type = {
    require(codec ne null)
    val jvmClass = codec.jvmClass
    _userCodecs += jvmClass → codec
    this
  }

  def addCodecFor[T](jvmClass: Class[T], codec: Codec[T]): this.type = {
    require(jvmClass ne null)
    require(codec ne null)
    _userCodecs += jvmClass → codec
    this
  }

  def addFuncBasedStructCodec[T](jvmClass: Class[T])
                                (encoder: (Protocol, T) ⇒ Unit)
                                (decoder: (Protocol) ⇒ T): this.type = {
    require(jvmClass ne null)
    require(encoder ne null)
    require(decoder ne null)

    val codec = FuncBasedStructCodec(typeTokenOfClass(jvmClass), encoder, decoder)
    addCodec(codec)
  }

  def addStringBasedStructCodec[T](jvmClass: Class[T])
                                  (toStringRepr: (T) ⇒ String)
                                  (fromStringRepr: (String) ⇒ T): this.type = {
    require(jvmClass ne null)
    require(toStringRepr ne null)
    require(fromStringRepr ne null)

    // Validation
    thrifter.codecOfClass(jvmClass) match {
      case StructCodec(_, descriptor) ⇒
        val fields = descriptor.fields.valuesIterator
        if(!fields.hasNext) {
          throw new RuntimeException("Requested string-based codec for %s but it has no fields".format(jvmClass))
        }
        val field = fields.next()
        val fieldClass = field.jvmClass
        if(fieldClass != StringClass) {
          throw new RuntimeException("Requested string-based codec for %s but it has a field of %s".format(
            jvmClass,
            fieldClass
          ))
        }

      case codec ⇒
        throw new RuntimeException("%s is not a struct. %s parsed %s".format(
          jvmClass,
          thrifter.getClass.getSimpleName,
          codec
        ))
    }

    val codec = StringBasedStructCodec(typeTokenOfClass(jvmClass), toStringRepr, fromStringRepr)
    addCodec(codec)
  }

  def addScalaMapBasedStructCodec[T, A, B](jvmClass: Class[T])
                                          (toMapRepr: (T) ⇒ GenMap[A, B])
                                          (fromMapRepr: (GenMap[A, B]) ⇒ T): this.type = {
    require(jvmClass ne null)
    require(toMapRepr ne null)
    require(fromMapRepr ne null)

    // Validation
    val structCodec = thrifter.codecOfClass(jvmClass)
    val mapCodec = structCodec match {
      case StructCodec(_, descriptor) ⇒
        val fields = descriptor.fields.valuesIterator
        if(!fields.hasNext) {
          throw new RuntimeException("Requested string-based codec for %s but it has no fields".format(jvmClass))
        }
        val field = fields.next()
        val fieldClass = field.jvmClass
        if(!classOf[scala.collection.Map[_,_]].isAssignableFrom(fieldClass)) {
          throw new RuntimeException("Requested map-based codec for [STRUCT] %s but it has a field of %s".format(
            jvmClass,
            fieldClass
          ))
        }

        val fieldType = field.jvmType
        val mapCodec = thrifter.codecOfType(fieldType)
        mapCodec match {
          case codec @ ScalaMapCodec(_, _, _, _, _) ⇒
            codec.asInstanceOf[ScalaMapCodec[A, B, Any]]

          case codec ⇒
            throw new RuntimeException("Requested map-based codec for %s but it is %s".format(
              fieldType,
              codec
            ))
        }

      case codec ⇒
        throw new RuntimeException("%s is not a struct. %s parsed %s".format(
          jvmClass,
          thrifter.getClass.getSimpleName,
          codec
        ))
    }

    val codec = ScalaMapBasedStructCodec[T, A, B](
      typeTokenOfClass(jvmClass),
      mapCodec,
      toMapRepr,
      fromMapRepr
    )

    addCodec(codec)
  }


  def addCollectionDescriptor[T](jvmClass: Class[T], descriptor: CollectionDescriptor): this.type = {
    _userCollectionDescriptors += jvmClass → descriptor
    this
  }

  def addCollectionOrdering[T, O](jvmClass: Class[T], ordering: CollectionOrdering[T, O]): this.type = {
    _userCollectionOrderings += jvmClass → ordering
    this
  }

  def setParanamer(p: Paranamer): this.type = {
    _paranamer = p
    this
  }

  def setClassLoader(loader: ClassLoader): this.type = {
    _classLoader = loader
    this
  }

  def build(): Thrift3r = {
    new Thrift3r(
      _userCodecs,
      _userCollectionDescriptors,
      _userCollectionOrderings,
      _paranamer,
      _classLoader
    )
  }
}
