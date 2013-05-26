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
package codec

import com.ckkloverdos.thrift3r.codec.misc.ClassCodec
import com.ckkloverdos.thrift3r.codec.numericref.{ScalaBigDecimalCodec, ScalaBigIntCodec, JavaBigDecimalCodec, JavaBigIntegerCodec}
import com.ckkloverdos.thrift3r.codec.primitive.{FloatCodec, CharCodec, BooleanCodec, LongCodec, ShortCodec, ByteCodec, DoubleCodec, IntCodec}
import com.ckkloverdos.thrift3r.codec.primitiveref.{FloatRefCodec, CharRefCodec, BooleanRefCodec, DoubleRefCodec, LongRefCodec, IntRefCodec, ByteRefCodec, ShortRefCodec}
import com.ckkloverdos.thrift3r.codec.stdref.StringCodec
import java.math.{RoundingMode, MathContext}

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
object Codecs {
  final val StdRadix = 10
  final val StdMathContext = new MathContext(26, RoundingMode.HALF_UP)

  @inline final def typeMapOfCodecSet(codecs: Set[Codec[_]]): Map[JClass, Codec[_]] =
    codecs.map(c ⇒ (c.jvmClass, c)).toMap

  final val PrimitiveCodecsSet = Set[Codec[_]](
    ByteCodec,
    BooleanCodec,
    CharCodec,
    ShortCodec,
    IntCodec,
    LongCodec,
    FloatCodec,
    DoubleCodec
  )

  final val PrimitiveCodecs: Map[JClass, Codec[_]] = typeMapOfCodecSet(PrimitiveCodecsSet)

  final def isPrimitiveClass[T](jvmClass: Class[T]) = PrimitiveCodecs.contains(jvmClass)

  final val PrimitiveRefCodecsSet = Set[Codec[_]](
    ByteRefCodec,
    BooleanRefCodec,
    CharRefCodec,
    ShortRefCodec,
    IntRefCodec,
    LongRefCodec,
    FloatRefCodec,
    DoubleRefCodec
  )

  final val PrimitiveRefCodecs: Map[JClass, Codec[_]] = typeMapOfCodecSet(PrimitiveRefCodecsSet)

  final def isPrimitiveRefClass[T](jvmClass: Class[T]) = PrimitiveRefCodecs.contains(jvmClass)

  final def AllPrimitiveCodecs = PrimitiveCodecs ++ PrimitiveRefCodecs

  final def genNumericRefCodecs(
    radix: Int = StdRadix,
    mc: MathContext = StdMathContext
  ) =
    typeMapOfCodecSet(
      Set[Codec[_]](
        JavaBigIntegerCodec(radix),
        JavaBigDecimalCodec(mc),
        ScalaBigIntCodec(radix),
        ScalaBigDecimalCodec(mc)
      )
    )

  final val StdNumericRefCodecs = genNumericRefCodecs(StdRadix, StdMathContext)

  final def genStdRefCodecs(loader: ClassLoader) = typeMapOfCodecSet(
    Set(
      StringCodec,
      ClassCodec(loader)
    )
  )

  final val StdRefCodecs = genStdRefCodecs(Thread.currentThread().getContextClassLoader)

  final def genFixedCodecs(loader: ClassLoader) = AllPrimitiveCodecs ++ genStdRefCodecs(loader)

  /**
   * These codecs cannot be overridden in [[com.ckkloverdos.thrift3r.Thrift3r]],
   * even if the `userCodecs` provided in the constructor specify alternatives.
   */
  final val StdFixedCodecs = genFixedCodecs(Thread.currentThread().getContextClassLoader)

  def genStdCodecs(
    loader: ClassLoader,
    radix: Int = StdRadix,
    mc: MathContext = StdMathContext
  ) = genFixedCodecs(loader) ++ genNumericRefCodecs(radix, mc)

  final val StdCodecs = StdFixedCodecs ++ StdNumericRefCodecs
}
