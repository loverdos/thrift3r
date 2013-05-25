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

package com.ckkloverdos.thrift3r.collection.descriptor

import collection.generic.{SeqFactory, BitSetFactory, SortedMapFactory, SortedSetFactory, GenericOrderedTraversableTemplate, OrderedTraversableFactory, MapFactory, SetFactory, TraversableFactory, GenericTraversableTemplate}
import collection.{Seq, BitSetLike, BitSet, SortedMap, SortedMapLike, SortedSet, SortedSetLike, MapLike, Map, SetLike, Set, GenTraversable}
import com.ckkloverdos.thrift3r.JClass
import com.ckkloverdos.thrift3r.collection.builder.ScalaBuilderFactory
import com.google.common.reflect.TypeToken

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
object ScalaDescriptor {
  final val KnownCollectionDescriptors: Predef.Map[JClass, CollectionDescriptor] = Seq(
    // Unordered, general
    ofTraversable(classOf[collection.Traversable[_]], collection.Traversable),
    ofTraversable(classOf[collection.immutable.Traversable[_]], collection.immutable.Traversable),
    ofTraversable(classOf[collection.mutable.Traversable[Any]], collection.mutable.Traversable),

    ofTraversable(classOf[collection.Iterable[_]], collection.Iterable),
    ofTraversable(classOf[collection.immutable.Iterable[_]], collection.immutable.Iterable),
    ofTraversable(classOf[collection.mutable.Iterable[Any]], collection.mutable.Iterable),

    ofSeq(classOf[collection.Seq[_]], collection.Seq),
    ofSeq(classOf[collection.immutable.Seq[_]], collection.immutable.Seq),
    ofSeq(classOf[collection.mutable.Seq[Any]], collection.mutable.Seq),

    ofSeq(classOf[collection.IndexedSeq[_]], collection.IndexedSeq),
    ofSeq(classOf[collection.immutable.IndexedSeq[_]], collection.immutable.IndexedSeq),
    ofSeq(classOf[collection.mutable.IndexedSeq[Any]], collection.mutable.IndexedSeq),

    ofSeq(classOf[collection.LinearSeq[_]], collection.LinearSeq),
    ofSeq(classOf[collection.immutable.LinearSeq[_]], collection.immutable.LinearSeq),
    ofSeq(classOf[collection.mutable.LinearSeq[Any]], collection.mutable.LinearSeq),

    ofSet(classOf[collection.Set[Any]], collection.Set),
    ofSet(classOf[collection.immutable.Set[Any]], collection.immutable.Set),
    ofSet(classOf[collection.mutable.Set[Any]], collection.mutable.Set),

    ofMap(classOf[collection.Map[Any,_]], collection.Map),
    ofMap(classOf[collection.immutable.Map[Any,_]], collection.immutable.Map),
    ofMap(classOf[collection.mutable.Map[Any,Any]], collection.mutable.Map),

    // Unordered, specific
    ofBitSet(classOf[collection.BitSet], collection.BitSet),
    ofBitSet(classOf[collection.immutable.BitSet], collection.immutable.BitSet),
    ofBitSet(classOf[collection.mutable.BitSet], collection.mutable.BitSet),

    ofSeq(classOf[collection.immutable.List[_]], collection.immutable.List),
    ofSeq(classOf[collection.immutable.Vector[_]], collection.immutable.Vector),
    ofSeq(classOf[collection.immutable.Queue[_]], collection.immutable.Queue),
    ofSeq(classOf[collection.immutable.Stream[_]], collection.immutable.Stream),

    ofSeq(classOf[collection.mutable.Buffer[Any]], collection.mutable.Buffer),
    ofSeq(classOf[collection.mutable.ListBuffer[Any]], collection.mutable.ListBuffer),
    ofSeq(classOf[collection.mutable.LinkedList[Any]], collection.mutable.LinkedList),
    ofSeq(classOf[collection.mutable.DoubleLinkedList[Any]], collection.mutable.DoubleLinkedList),
    ofSeq(classOf[collection.mutable.ArrayBuffer[Any]], collection.mutable.ArrayBuffer),
    ofSeq(classOf[collection.mutable.ArraySeq[Any]], collection.mutable.ArraySeq),
    ofSeq(classOf[collection.mutable.ArrayStack[Any]], collection.mutable.ArrayStack),
    ofSeq(classOf[collection.mutable.Queue[Any]], collection.mutable.Queue),
    ofSeq(classOf[collection.mutable.Stack[Any]], collection.mutable.Stack),

    ofSet(classOf[collection.immutable.HashSet[Any]], collection.immutable.HashSet),
    ofSet(classOf[collection.mutable.HashSet[Any]], collection.mutable.HashSet),

    ofSet(classOf[collection.mutable.LinkedHashSet[Any]], collection.mutable.LinkedHashSet),

    ofMap(classOf[collection.immutable.HashMap[Any,_]], collection.immutable.HashMap),
    ofMap(classOf[collection.mutable.HashMap[Any,Any]], collection.mutable.HashMap),

    // Ordered, general
    ofSortedSet(classOf[collection.SortedSet[Any]], collection.SortedSet),
    ofSortedSet(classOf[collection.immutable.SortedSet[Any]], collection.immutable.SortedSet),

    ofSortedMap(classOf[collection.SortedMap[Any,_]], collection.SortedMap),
    ofSortedMap(classOf[collection.immutable.SortedMap[Any,_]], collection.immutable.SortedMap),

    // Ordered, specific
    ofOrderedTraversable(classOf[collection.mutable.PriorityQueue[Any]], collection.mutable.PriorityQueue),

    ofSortedSet(classOf[collection.immutable.TreeSet[Any]], collection.immutable.TreeSet),

    ofSortedMap(classOf[collection.immutable.TreeMap[Any,_]], collection.immutable.TreeMap)
  ).map(d ⇒ (d.jvmClass, d)).toMap


  final def ofSeq[A, CC[X] <: Seq[X] with GenericTraversableTemplate[X, CC]](
    javaClass: Class[CC[A]],
    factory: SeqFactory[CC]
  ) =
    CollectionDescriptor(
      TypeToken.of(javaClass),
      ScalaBuilderFactory.ofSeq(factory),
      false
    )

  final def ofTraversable[A, CC[X] <:   Traversable[X]
                                   with GenTraversable[X]
                                   with GenericTraversableTemplate[X, CC]](
    javaClass: Class[CC[A]],
    factory: TraversableFactory[CC]
  ) =
    CollectionDescriptor(
      TypeToken.of(javaClass),
      ScalaBuilderFactory.ofTraversable(factory),
      false
    )

  final def ofOrderedTraversable[A, CC[X] <:   Traversable[X]
                                          with GenTraversable[X]
                                          with GenericOrderedTraversableTemplate[X, CC]](
    javaClass: Class[CC[A]],
    factory: OrderedTraversableFactory[CC]
  ) =
    CollectionDescriptor(
      TypeToken.of(javaClass),
      ScalaBuilderFactory.ofOrderedTraversable(factory),
      true
    )

  final def ofSet[A, CC[X] <: Set[X] with SetLike[X, CC[X]]](
    javaClass: Class[CC[A]],
    factory: SetFactory[CC]
  ) =
    CollectionDescriptor(
      TypeToken.of(javaClass),
      ScalaBuilderFactory.ofSet(factory),
      false
    )

  final def ofSortedSet[A, CC[X] <: SortedSet[X] with SortedSetLike[X, CC[X]]](
    javaClass: Class[CC[A]],
    factory: SortedSetFactory[CC]
  ) =
    CollectionDescriptor(
      TypeToken.of(javaClass),
      ScalaBuilderFactory.ofSortedSet(factory),
      true
    )

  final def ofMap[A, B, CC[X, Y] <: Map[X, Y] with MapLike[X, Y, CC[X, Y]]](
    javaClass: Class[CC[A, B]],
    factory: MapFactory[CC]
  ) =
    CollectionDescriptor(
      TypeToken.of(javaClass),
      ScalaBuilderFactory.ofMap(factory),
      false
    )

  final def ofSortedMap[A, B, CC[X, Y] <: SortedMap[X, Y] with SortedMapLike[X, Y, CC[X, Y]]](
    javaClass: Class[CC[A, B]],
    factory: SortedMapFactory[CC]
  ) =
    CollectionDescriptor(
      TypeToken.of(javaClass),
      ScalaBuilderFactory.ofSortedMap(factory),
      true
    )

  final def ofBitSet[Coll <: BitSet with BitSetLike[Coll]](
    javaClass: Class[Coll],
    factory: BitSetFactory[Coll]
  ) =
    CollectionDescriptor(
      TypeToken.of(javaClass),
      ScalaBuilderFactory.ofBitSet(factory),
      false
    )
}
