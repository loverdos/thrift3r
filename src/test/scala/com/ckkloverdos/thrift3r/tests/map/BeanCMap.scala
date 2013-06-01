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
package tests.map

/**
 *
 * @author Christos KK Loverdos <loverdos@gmail.com>
 */
case class BeanCMap(map: scala.collection.Map[TTypeEnum, IntRef])

case class BeanIMap(map: scala.collection.immutable.Map[TTypeEnum, IntRef])

case class BeanMMap(map: scala.collection.mutable.Map[TTypeEnum, IntRef])

case class BeanOptionCMap(optMap: Option[scala.collection.Map[TTypeEnum, IntRef]])

case class BeanMapOfMap(outerMap: scala.collection.Map[String, scala.collection.Map[String, BeanCMap]])

case class BeanMapOfCompositeKey(map: scala.collection.Map[BeanCMap, String])

case class BeanMapOfCompositeKeyValue(map: scala.collection.Map[BeanCMap, BeanCMap])
