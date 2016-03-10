package scalera.ddd.message

import scala.reflect.{ClassTag, classTag}

import scalera.ddd.{Aggregate, Ref}

/**
 * Special message whose purpose is to
 * modify / mutate the internal state of
 * some [[Aggregate]].
 * @param addressee Aggregate [[ClassTag]] target of current command.
 * @tparam A Aggregate type.
 */
abstract class Command[A<:Aggregate[_,A]:ClassTag](
  val addressee: Ref[A]) extends Message {
  val aggregateType = classTag[A]
}
