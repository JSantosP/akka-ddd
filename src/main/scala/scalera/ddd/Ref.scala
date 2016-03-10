package scalera.ddd

import scala.reflect.ClassTag

/**
 * A [[Ref]] represents a typed identifier that
 * points to some existing aggregate.
 * @param id Aggregate's id
 */
case class Ref[A<:Aggregate[_,A]:ClassTag](id: String)
