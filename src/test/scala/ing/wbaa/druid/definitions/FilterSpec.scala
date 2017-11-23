package ing.wbaa.druid
package definitions

import org.scalatest._
import FilterOperators._

class FilterSpec extends WordSpec with Matchers {
  val filterFoo = SelectFilter(dimension = "foo", value = "bar")
  val filterBar = SelectFilter(dimension = "bar", value = "baa")
  val filterBoo = SelectFilter(dimension = "boo", value = "foo")

  "A filter" should {
    "be inverted" in {
      !(filterFoo) shouldBe NotFilter(field = filterFoo)
    }
    "compose to an AND composition from 2 filters" in {
      filterFoo && filterBar shouldBe AndFilter(fields = List(filterFoo, filterBar))
    }
    "compose to an AND composition from 3 filters" in {
      filterFoo && filterBar && filterBoo shouldBe AndFilter(fields = List(filterFoo, filterBar, filterBoo))
    }
    "merge AND filters" in {
      (filterFoo && filterBar) && (filterBar && filterBoo) shouldBe AndFilter(fields = List(filterFoo, filterBar, filterBar, filterBoo))
    }
    "compose to an OR composition from 2 filters" in {
      filterFoo || filterBar shouldBe OrFilter(fields = List(filterFoo, filterBar))
    }
    "compose to an OR composition from 3 filters" in {
      filterFoo || filterBar || filterBoo shouldBe OrFilter(fields = List(filterFoo, filterBar, filterBoo))
    }
    "give precedence to AND over OR" in {
      filterFoo && filterBar || filterBoo shouldBe OrFilter(fields = List(filterFoo && filterBar, filterBoo))
      filterFoo || filterBar && filterBoo shouldBe OrFilter(fields = List(filterFoo, filterBar && filterBoo))
    }
  }
}
