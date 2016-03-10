package scalera.ddd

import scala.reflect.ClassTag

import akka.actor._
import scalera.ddd.message.Command
import scalera.ddd.utils.TypeUtils._

/**
 * An [[AggregateManager]] is in charge of managing (dispatching
 * messages) all the aggregates that share the same type.
 * @param ev1 Aggregate [[ClassTag]].
 * @param af The AggregateManager requires an [[Aggregate.Factory]]
 *           which is actually an implicit view from [[Ref]] to [[A]].
 * @tparam S State type.
 * @tparam A Aggregate type.
 */
abstract class AggregateManager[S,A<:Aggregate[S,A]:ClassTag]()(
  implicit af: Aggregate.Factory[S,A]) extends Actor {

  override def receive = {
    case command: Command[A] if command.aggregateType.isA[A]=>
      createOrGetChild(command.addressee) ! command
  }

  /**
   * Define the way to retrieve or create an aggregate
   * given its id.
   * @param id
   * @return
   */
  def createOrGetChild(id: Ref[A]): ActorRef =
    context.child(id.id).getOrElse(context.actorOf(Props(af(id))))

}
