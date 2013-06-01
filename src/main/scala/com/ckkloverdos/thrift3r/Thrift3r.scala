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

import com.ckkloverdos.thrift3r.Thrift3rHelpers.{getterOf, defaultProtocolFor}
import com.ckkloverdos.thrift3r.codec.collection.{ScalaSeqSetCodec, ScalaMapCodec}
import com.ckkloverdos.thrift3r.codec.enumeration.EnumCodec
import com.ckkloverdos.thrift3r.codec.misc.OptionCodec
import com.ckkloverdos.thrift3r.codec.struct.StructCodec
import com.ckkloverdos.thrift3r.codec.{Codec, Codecs}
import com.ckkloverdos.thrift3r.collection.builder.CollectionBuilderFactory
import com.ckkloverdos.thrift3r.collection.descriptor.{ScalaDescriptor, CollectionDescriptor}
import com.ckkloverdos.thrift3r.collection.generics.ScalaGenerics
import com.ckkloverdos.thrift3r.collection.ordering.{ScalaCollectionOrdering, ScalaOrderings, CollectionOrdering}
import com.ckkloverdos.thrift3r.descriptor.{StructDescriptor, FieldDescriptor}
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.google.common.cache.{CacheLoader, CacheBuilder, LoadingCache}
import com.google.common.reflect.TypeToken
import com.thoughtworks.paranamer.{DefaultParanamer, BytecodeReadingParanamer, AnnotationParanamer, AdaptiveParanamer, Paranamer}
import java.io.{StringReader, StringWriter, ByteArrayInputStream, ByteArrayOutputStream, InputStream, OutputStream}
import java.lang.reflect.Constructor
import org.apache.thrift.protocol.TProtocol
import org.apache.thrift.transport.{TIOStreamTransport, TTransport}
import scala.collection.{Set ⇒ CSet, Seq ⇒ CSeq, Map ⇒ CMap, GenMap, GenTraversable, immutable}
import com.ckkloverdos.thrift3r.protocol.Protocol
import com.ckkloverdos.thrift3r.protocol.thrift.TProtocolAdapter
import com.ckkloverdos.thrift3r.protocol.json.{JSONHelpers, JSONProtocol}
import com.ckkloverdos.thrift3r.protocol.helper.ProtocolHelpers

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case class Thrift3r(
  userCodecs: Map[JType, Codec[_]] = Map(),
  userCollectionDescriptors: Map[JClass, CollectionDescriptor] = Map(),
  userCollectionOrderings: Map[JClass, CollectionOrdering[_, _]] = Map(),
  paranamer: Paranamer = new AdaptiveParanamer(
    new AnnotationParanamer(new BytecodeReadingParanamer),
    new DefaultParanamer
  ),
  loader: ClassLoader = Thread.currentThread().getContextClassLoader
) {

  protected val codecCache: LoadingCache[JType, Codec[_]] = CacheBuilder.newBuilder().build(
    new CacheLoader[JType, Codec[_]] { def load(javaType: JType) = codecOf0(javaType, Nil) }
  )

  val KnownCodecs = Codecs.StdNumericRefCodecs ++ userCodecs ++ Codecs.genFixedCodecs(loader)

  val KnownCollectionDescriptors = ScalaDescriptor.KnownCollectionDescriptors ++ userCollectionDescriptors

  val KnownOrderings = ScalaOrderings.StdOrderings ++ userCollectionOrderings

  def codecOfType(jvmType: JType): Codec[_] =
    try codecCache.get(jvmType)
    catch {
      case e: Throwable ⇒
        throw Thrift3rHelpers.unwrapExecutionExceptionCause(e)
    }

  def codecOfClass[T](jvmClass: Class[T]): Codec[T] =
    codecOfType(jvmClass).asInstanceOf[Codec[T]]

  def codecOfBean[T <: AnyRef](bean: T): Codec[T] =
    codecOfClass(bean.getClass.asInstanceOf[Class[T]])

  protected def codecOfOption(
    typeToken: TypeToken[_],
    elementCodec: Codec[_]
  ): Codec[_] = OptionCodec(typeToken.asInstanceOf[TypeToken[Option[Any]]], elementCodec.asInstanceOf[Codec[Any]])

  protected def codecOfEnum[E <: Enum[_]](
    enumClass: Class[E]
  ): Codec[_] = EnumCodec(enumClass)

  protected def codecOfSeq(
    typeToken: TypeToken[_],
    elementCodec: Codec[_]
  ): Codec[_] = codecOfSeqSet(TTypeEnum.LIST, typeToken, elementCodec)

  protected def codecOfSet(
    typeToken: TypeToken[_],
    elementCodec: Codec[_]
  ): Codec[_] = codecOfSeqSet(TTypeEnum.SET, typeToken, elementCodec)

  protected def codecOfMap(
    typeToken: TypeToken[_],
    keyCodec: Codec[_],
    valueCodec: Codec[_]
  ): Codec[_] = {
    val collectionClass = typeToken.getRawType

    KnownCollectionDescriptors.get(collectionClass) match {
      case None ⇒
        throw new Exception("Unknown map collection descriptor for %s".format(collectionClass))

      case Some(descriptor) ⇒
        if(descriptor.isOrdered) {
          val keyClass = keyCodec.jvmClass
          KnownOrderings.get(keyClass) match {
            case None ⇒
              throw new Exception("Unknown ordering for key %s to be used for map %s".format(keyClass, collectionClass))

            case Some(ScalaCollectionOrdering(_, ordering)) ⇒
              ScalaMapCodec[Any, Any, Ordering[Any]](
                typeToken.asInstanceOf[TypeToken[GenMap[Any, Any]]],
                keyCodec.asInstanceOf[Codec[Any]],
                valueCodec.asInstanceOf[Codec[Any]],
                ordering,
                descriptor.builderFactory.asInstanceOf[CollectionBuilderFactory[(Any, Any), GenMap[Any, Any], Ordering[Any]]]
              )
          }
        }
        else {
          ScalaMapCodec[Any, Any, Unit](
            typeToken.asInstanceOf[TypeToken[GenMap[Any, Any]]],
            keyCodec.asInstanceOf[Codec[Any]],
            valueCodec.asInstanceOf[Codec[Any]],
            (),
            descriptor.builderFactory.asInstanceOf[CollectionBuilderFactory[(Any, Any), GenMap[Any, Any], Unit]]
          )
        }
    }
  }

  protected def codecOfSeqSet(
    tTypeEnum: TTypeEnum,
    typeToken: TypeToken[_],
    elementCodec: Codec[_]
  ): Codec[_] = {
    val collectionClass = typeToken.getRawType

    KnownCollectionDescriptors.get(collectionClass) match {
      case None ⇒
        throw new Exception("Unknown collection descriptor for %s".format(collectionClass))

      case Some(descriptor) ⇒
        if(descriptor.isOrdered) {
          val elementClass = elementCodec.jvmClass
          KnownOrderings.get(elementClass) match {
            case None ⇒
              throw new Exception("Unknown ordering for %s to be used for %s".format(elementClass, collectionClass))

            case Some(ScalaCollectionOrdering(_, ordering)) ⇒
              ScalaSeqSetCodec[Any, Any](
                tTypeEnum,
                typeToken.asInstanceOf[TypeToken[GenTraversable[Any]]],
                elementCodec.asInstanceOf[Codec[Any]],
                ordering,
                descriptor.builderFactory.asInstanceOf[CollectionBuilderFactory[Any, GenTraversable[Any], Any]]
              )

            case Some(ordering) ⇒
              throw new Exception("Ordering %s is unsuitable for %s to be used for %s".format(ordering, elementClass, collectionClass))
          }
        }
        else {
          ScalaSeqSetCodec[Any, Unit](
            tTypeEnum,
            typeToken.asInstanceOf[TypeToken[GenTraversable[Any]]],
            elementCodec.asInstanceOf[Codec[Any]],
            (),
            descriptor.builderFactory.asInstanceOf[CollectionBuilderFactory[Any, GenTraversable[Any], Unit]]
          )
        }
    }
  }

  protected def codecOf0(jvmType: JType, typeStack: List[JType]): Codec[_] = {
    val typeToken = typeTokenOfType(jvmType)
    val jvmClass = typeToken.getRawType

    if(jvmClass == classOf[java.lang.Object]) {
      throw new Exception("Requested codec for java.lang.Object. " +
        "If you do not explicitly use it, make sure your Scala containers do not use primitive types. " +
        "The offending type sequence is: %s".format((jvmType :: typeStack).reverse.mkString("[", " -> ", "]")))
    }

    val codec = KnownCodecs.get(jvmClass) match {
      case Some(codec) ⇒
        codec

      case None ⇒
        // Not a known codec.
        // Then it must either be a collection or a struct.
        // TODO: Use a strategy instead of IFs??? (Yeah, OOPatterns feel good)
        if(classOf[CSeq[_]].isAssignableFrom(jvmClass)) {
          // LIST
          val elementJVMType = ScalaGenerics.elementTypeOfSeq(jvmType)
          val elementCodec = codecOf0(elementJVMType, jvmType :: typeStack)

          codecOfSeq(typeToken, elementCodec)
        }
        else if(classOf[CSet[_]].isAssignableFrom(jvmClass)) {
          // SET
          val elementJVMType = ScalaGenerics.elementTypeOfSet(jvmType)
          val elementCodec = codecOf0(elementJVMType, jvmType :: typeStack)

          codecOfSet(typeToken, elementCodec)
        }
        else if(classOf[CMap[_,_]].isAssignableFrom(jvmClass)) {
          // LIST
          val mapKeyJVMType = ScalaGenerics.keyTypeOfMap(jvmType)
          val mapValueJVMType = ScalaGenerics.valueTypeOfMap(jvmType)
          val keyCodec = codecOf0(mapKeyJVMType, jvmType :: typeStack)
          val valueCodec = codecOf0(mapValueJVMType, jvmType :: typeStack)

          codecOfMap(typeToken, keyCodec, valueCodec)
        }
        else if(classOf[Option[_]].isAssignableFrom(jvmClass)) {
          // SET for Option[T]
          val elementJVMType = ScalaGenerics.elementTypeOfOption(jvmType)
          val elementCodec = codecOf0(elementJVMType, jvmType :: typeStack)

          codecOfOption(typeToken, elementCodec)
        }
        // else if Java Collections ...
        else if(classOf[Enum[_]].isAssignableFrom(jvmClass)) {
          codecOfEnum(jvmClass.asInstanceOf[Class[Enum[_]]])
        }
        else {
          // last resort. It must be a user-defined class
          StructCodec(this, structDescriptorOf(jvmType))
        }
    }

//    println("let codecOf (%s) = %s".format(jvmClass, codec))
    codec
  }

  def structDescriptorOf(jvmType: JType) = {
    val javaTypeToken = typeTokenOfType(jvmType)
    val jvmClass = javaTypeToken.getRawType
//    println("let structDescriptorOf (%s) =".format(jvmClass))

    // If there are more than one constructors, pick the longest
    val (_, constructorOpt) = jvmClass.getConstructors.foldLeft[(Int, Option[Constructor[_]])]((-1, None)) {
      case (found@(foundNParams, foundOption), constructor) ⇒
        val paramTypes = constructor.getParameterTypes
        val nparams = paramTypes.size

        if(nparams > foundNParams) {
          (nparams, Some(constructor))
        } else {
          found
        }
    }

    val name = jvmClass.getSimpleName
    val fullName = jvmClass.getName
    var fieldMap = immutable.SortedMap[Short, FieldDescriptor]()

    val descriptor = constructorOpt match {
      case None ⇒
        throw new IllegalArgumentException("No constructor found for %s".format(jvmType))

      case Some(constructor) ⇒
//        println("  let constructor =")
        val fieldTypes = constructor.getGenericParameterTypes

        for {
          (fieldName, fieldIndex) ←
            try paranamer.lookupParameterNames(constructor, true).zipWithIndex
            catch { case e: Throwable ⇒
              throw new Exception("Could not lookup parameter names of %s for %s".format(constructor, jvmType))
            }
        } {
          val fieldType = fieldTypes(fieldIndex)
          val fieldTypeToken = typeTokenOfType(fieldType)
          val fieldGetter = getterOf(jvmClass, fieldName, fieldType)

          val fieldDescr = FieldDescriptor(
            fieldIndex,
            fieldName,
            fieldTypeToken,
            true,
            fieldGetter
          )

//          println("    let fieldDescr = %s".format(fieldDescr))

          fieldMap += fieldIndex.toShort → fieldDescr
        }
//        println("    %s".format(constructor))

        StructDescriptor(
          name,
          fullName,
          fieldMap,
          javaTypeToken,
          constructor
        )
    }

//    println("  %s".format(descriptor))

    descriptor
  }

  def encodeBean[A <: AnyRef](bean: A, protocol: Protocol) {
    codecOfClass(bean.getClass.asInstanceOf[Class[A]]).encode(protocol, bean)
  }

  def encodeBean[A <: AnyRef](bean: A, tprotocol: TProtocol) {
    val protocol = new TProtocolAdapter(tprotocol)
    encodeBean(bean, protocol)
  }

  def encodeBean[A <: AnyRef](bean: A, transport: TTransport, close: Boolean) {
    val protocol = defaultProtocolFor(transport)
    encodeBean(bean, protocol)
    if(close) { transport.close() }
  }

  def encodeBean[A <: AnyRef](bean: A, outStream: OutputStream, close: Boolean) {
    val transport = new TIOStreamTransport(outStream)
    encodeBean(bean, transport, close)
  }

  def decodeBean[A <: AnyRef](javaClass: Class[A], protocol: Protocol): A =
    codecOfClass(javaClass).decode(protocol)

  def decodeBean[A <: AnyRef](javaClass: Class[A], tprotocol: TProtocol): A = {
    val protocol = new TProtocolAdapter(tprotocol)
    decodeBean(javaClass, protocol)
  }

  def decodeBean[A <: AnyRef](beanClass: Class[A], transport: TTransport, close: Boolean): A = {
    val protocol = defaultProtocolFor(transport)
    val bean = decodeBean(beanClass, protocol)
    if(close) { transport.close() }
    bean
  }

  def decodeBean[A <: AnyRef](beanClass: Class[A], inStream: InputStream, close: Boolean): A = {
    val transport = new TIOStreamTransport(inStream)
    decodeBean(beanClass, transport, close)
  }

  def beanToBytes[A <: AnyRef](bean: A, byteSize: Int = 512): Array[Byte] = {
    val outStream = new ByteArrayOutputStream(byteSize)
    encodeBean(bean, outStream, true)
    outStream.toByteArray
  }

  def bytesToBean[A <: AnyRef](beanClass: Class[A], bytes: Array[Byte]): A = {
    val inStream = new ByteArrayInputStream(bytes)
    decodeBean(beanClass, inStream, true)
  }

  def beanToJSON[A <: AnyRef](bean: A, prettyPrint: Boolean = true): String = {
    val writer = new StringWriter
    val protocol = JSONHelpers.jacksonProtocolForOutput(writer)

    val codec = codecOfBean(bean)
    ProtocolHelpers.encodeAndFlush(protocol, bean, codec)

    writer.toString
  }

  def jsonToBean[A <: AnyRef](beanClass: Class[A], json: String): A = {
    val protocol = JSONHelpers.jacksonProtocolForInputString(json)
    val codec = codecOfClass(beanClass)
    codec.decode(protocol)
  }
}
