package scalera.ddd.bus

import scalera.ddd.message.Event

/**
 * An Ack of having produced / consumed certain event
 * from the event bus.
 */
case class Ack(event: Event)
