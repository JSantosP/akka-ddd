package scalera

import scala.language.implicitConversions

import scala.reflect.{ClassTag, classTag}

import akka.actor.ActorSystem

package object ddd {

  /**
   * It creates a new [[ActorSystem]] for testing purposes.
   * @return A brand new [[ActorSystem]]
   */
  def testSystem: ActorSystem =
    ActorSystem(s"system-${System.currentTimeMillis()}")

  /**
   * Create a new random reference for some [[A]] aggregate type
   */
  def reference[A<:Aggregate[_,A]:ClassTag]: Ref[A] =
    Ref[A](s"${classTag[A].runtimeClass.getSimpleName}-${System.currentTimeMillis()}")

  implicit def fromRefToId(ref: Ref[_]): String = ref.id

}
