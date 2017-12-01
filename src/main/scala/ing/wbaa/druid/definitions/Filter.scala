/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ing.wbaa.druid
package definitions

import utils.StringUtils

sealed trait FilterType {
  lazy val value: String = StringUtils.decapitalize(this.getClass.getSimpleName.replace("$", ""))
}

object FilterType {
  case object And extends FilterType
  case object Or extends FilterType
  case object Selector extends FilterType
  case object ColumnComparison extends FilterType
  case object Regex extends FilterType
  case object Not extends FilterType
  case object Javascript extends FilterType
  val values = Set(And, Or, Selector, ColumnComparison, Regex, Not, Javascript)
}

sealed trait Filter {
  val `type`: FilterType
}

object FilterOperators {
  implicit class FilterExtensions(filter: Filter) {
    def &&(otherFilter: Filter) = (filter, otherFilter) match {
      case (AndFilter(fields), AndFilter(otherFields)) => AndFilter(fields = fields ++ otherFields)
      case (AndFilter(fields), other) => AndFilter(fields = fields :+ other)
      case (other, AndFilter(fields)) => AndFilter(fields = fields :+ other)
      case _ => AndFilter(fields = List(filter, otherFilter))
    }
    def ||(otherFilter: Filter) = (filter, otherFilter) match {
      case (OrFilter(fields), OrFilter(otherFields)) => OrFilter(fields = fields ++ otherFields)
      case (OrFilter(fields), other) => OrFilter(fields = fields :+ other)
      case (other, OrFilter(fields)) => OrFilter(fields = fields :+ other)
      case _ => OrFilter(fields = List(filter, otherFilter))
    }
    def unary_!() = NotFilter(field = filter)
  }
}

case class SelectFilter(dimension: String, value: Any) extends Filter { val `type` = FilterType.Selector}
case class RegexFilter(dimension: String, pattern: String) extends Filter { val `type` = FilterType.Regex }
case class AndFilter(fields: List[Filter]) extends Filter { val `type` = FilterType.And }
case class OrFilter(fields: List[Filter]) extends Filter { val `type` = FilterType.Or }
case class NotFilter(field: Filter) extends Filter { val `type` = FilterType.Not }
case class JavascriptFilter(dimension: String, function: String) extends Filter { val `type` = FilterType.Javascript }
