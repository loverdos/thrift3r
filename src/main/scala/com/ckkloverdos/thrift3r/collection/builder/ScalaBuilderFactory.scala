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

package com.ckkloverdos.thrift3r.collection.builder

import scala.collection.generic.{SeqFactory, BitSetFactory, SortedMapFactory, MapFactory, SortedSetFactory, SetFactory, OrderedTraversableFactory, GenericOrderedTraversableTemplate, TraversableFactory, GenericTraversableTemplate}
import scala.collection.{Seq, BitSetLike, BitSet, SortedMapLike, SortedMap, MapLike, Map, SortedSetLike, SortedSet, SetLike, Set, GenTraversable}

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
object ScalaBuilderFactory {
  final def ofSeq[A, CC[X] <: Seq[X] with GenericTraversableTemplate[X, CC]](
    factory: SeqFactory[CC]
  ) = new ScalaCollectionBuilderFactory[A, CC[A]] {
    def newBuilder(meta: Unit) = new CollectionBuilder[A, CC[A]] {
      final private[this] val builder = factory.newBuilder[A]

      def add(element: A) { builder += element}

      def build = builder.result()
    }
  }

  final def ofTraversable[A, CC[X] <: Traversable[X] with GenTraversable[X] with GenericTraversableTemplate[X, CC]](
    factory: TraversableFactory[CC]
  ) =
    new ScalaCollectionBuilderFactory[A, CC[A]] {
      def newBuilder(meta: Unit) = new CollectionBuilder[A, CC[A]] {
        final private[this] val builder = factory.newBuilder[A]

        def add(element: A) { builder += element}

        def build = builder.result()
      }
    }

  final def ofOrderedTraversable[A, CC[X] <: Traversable[X] with GenTraversable[X] with GenericOrderedTraversableTemplate[X, CC]](
    factory: OrderedTraversableFactory[CC]
  ) =
    new ScalaOrderedTraversableBuilderFactory[A, CC[A]] {
      def newBuilder(ordering: Ordering[A]) = new CollectionBuilder[A, CC[A]] {
        final private[this] val builder = factory.newBuilder[A](ordering)

        def add(element: A) { builder += element}

        def build = builder.result()
      }
    }

  final def ofSet[A, CC[X] <: Set[X] with SetLike[X, CC[X]]](
    factory: SetFactory[CC]
  ) =
    new StdCollectionBuilderFactory[A, CC[A]] {
      def newBuilder(meta: Unit) = new CollectionBuilder[A, CC[A]] {
        final private[this] val builder = factory.newBuilder[A]

        def add(element: A) { builder += element }

        def build = builder.result()
      }
    }

  final def ofSortedSet[A, CC[X] <: SortedSet[X] with SortedSetLike[X, CC[X]]](
    factory: SortedSetFactory[CC]
  ) =
    new ScalaOrderedTraversableBuilderFactory[A, CC[A]] {
      def newBuilder(ordering: Ordering[A]) = new CollectionBuilder[A, CC[A]] {
        final private[this] val builder = factory.newBuilder[A](ordering)

        def add(element: A) { builder += element }

        def build = builder.result()
      }
    }

  final def ofMap[A, B, CC[X, Y] <: Map[X, Y] with MapLike[X, Y, CC[X, Y]]](
    factory: MapFactory[CC]
  ) =
    new StdCollectionBuilderFactory[(A, B), CC[A, B]] {
      def newBuilder(meta: Unit) = new CollectionBuilder[(A, B), CC[A, B]] {
        final private[this] val builder = factory.newBuilder[A, B]

        def add(element: (A, B)) { builder += element }

        def build = builder.result()
      }
    }

  final def ofSortedMap[A, B, CC[X, Y] <: SortedMap[X, Y] with SortedMapLike[X, Y, CC[X, Y]]](
    factory: SortedMapFactory[CC]
  ) =
    new ScalaOrderedMapBuilderFactory[A, B, CC[A, B]] {
      def newBuilder(ordering: Ordering[A]) = new CollectionBuilder[(A, B), CC[A, B]] {
        final private[this] val builder = factory.newBuilder[A, B](ordering)

        def add(element: (A, B)) { builder += element }

        def build = builder.result()
      }
    }

  final def ofBitSet[Coll <: BitSet with BitSetLike[Coll]](
    factory: BitSetFactory[Coll]
  ) =
    new StdCollectionBuilderFactory[Int, Coll] {
      def newBuilder(meta: Unit) = new CollectionBuilder[Int, Coll] {
        final private[this] val builder = factory.newBuilder

        def add(element: Int) { builder += element }

        def build = builder.result()
      }
    }
}