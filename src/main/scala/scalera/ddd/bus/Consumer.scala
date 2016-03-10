package scalera.ddd.bus

import akka.actor.Actor
import scalera.ddd.message.Event

/**
 * A [[Consumer]] actually subscribes itself to all events
 * that are published in the event bus.
 */
trait Consumer extends Actor {

  type Handler = PartialFunction[Event,Unit]

  override def preStart(): Unit = {
    super.preStart()
    context.system.eventStream.subscribe(self,classOf[Event])
  }

  val handler: Handler

  override def receive = {
    case event: Event => handler.lift(event)
  }

}
