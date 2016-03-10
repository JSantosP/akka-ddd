package scalera.ddd.bus

import scala.concurrent.{ExecutionContext, Future}

import akka.actor.Actor
import akka.event.EventStream
import scalera.ddd.message.Event

/**
 * A [[Producer]] will publish every single event
 * it receives to the event bus.
 */
trait Producer extends Actor {

  val eventStream: EventStream

  def publish(event: Event)(implicit ec: ExecutionContext): Future[Ack] = {
    Future {
      eventStream.publish(event)
      Ack(event)
    }
  }

}

