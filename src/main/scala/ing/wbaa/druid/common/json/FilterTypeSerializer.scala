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

package ing.wbaa.druid.common.json

import org.json4s.MappingException
import ing.wbaa.druid.definitions.FilterType
import org.json4s.CustomSerializer
import org.json4s.JsonAST.JString

case object FilterTypeSerializer extends CustomSerializer[FilterType](
  format => ( {
    case JString(x) =>
      FilterType.values.find(_.value == x).getOrElse(
        throw new MappingException(s"$x is not a known filter type")
      )
  }, {
    case x: FilterType => JString(x.value)
  }))
