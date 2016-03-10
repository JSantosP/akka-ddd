package scalera.ddd.utils

import scala.reflect.classTag

import org.scalatest.{FlatSpec, Matchers}

class TypeUtilsTest extends FlatSpec with Matchers {

  behavior of "TypeUtils"

  import TypeUtils._

  class Kid
  class Daddy extends Kid
  class Grandpa extends Daddy

  it should "determine if a type inherits from some other" in {
    classTag[Kid].isA[Kid] should equal(true)
    classTag[Kid].isA[Daddy] should equal(false)
    classTag[Daddy].isA[Grandpa] should equal(false)
    classTag[Grandpa].isA[Kid] should equal(true)
  }

  it should "determine if a type inherits from some other (using class format)" in {
    classOf[Kid].isA[Kid] should equal(true)
    classOf[Kid].isA[Daddy] should equal(false)
    classOf[Daddy].isA[Grandpa] should equal(false)
    classOf[Grandpa].isA[Kid] should equal(true)
  }

}
