package scalera.ddd.utils

import scala.reflect.{ClassTag,classTag}

/**
 * [[TypeUtils]] module allows determining type relationships.
 * i.e.:
 * {{{
 *    class Kid
 *    class Daddy extends Kid
 *    class Grandpa extends Daddy
 *
 *    classTag[Kid].isA[Kid] should equal(true)
 *    classTag[Kid].isA[Daddy] should equal(false)
 *    classTag[Daddy].isA[Grandpa] should equal(false)
 *    classTag[Grandpa].isA[Kid] should equal(true)
 * }}}
 */
object TypeUtils {

  implicit class objectHelper[T](obj: T) {
    def isA[U:ClassTag]: Boolean =
      classTag[U].runtimeClass.isAssignableFrom(obj.getClass)
  }

  implicit class classTagHelper[T](ctag: ClassTag[T]){
    def isA[U:ClassTag]: Boolean =
      classTag[U].runtimeClass.isAssignableFrom(ctag.runtimeClass)
  }

  implicit class classHelper[T](clazz: Class[T]){
    def isA[U:ClassTag]: Boolean = ClassTag(clazz).isA[U]
  }

}
