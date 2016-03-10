package scalera.ddd

import scalera.ddd.message.{Command, Event}

class Accumulator(id: String) extends Aggregate[Int,Accumulator](id){

  override val stateUpdate: StateUpdate = {
    case (Added(_, n), state)=> state.fold(Option(n))(s => Option(s + n))
  }

  override val eventForCommand: EventForCommand = {
    case Add(n,to) => Added(to,n)
    case QueryState(to) => StateQueried(to,state)
  }

}

case class Add(n: Int, to: Ref[Accumulator]) extends Command[Accumulator](to)
case class Added(accumulatorRef: Ref[Accumulator], n: Int) extends Event

//  Only for testing purposes. State should be queried via persistence queries

case class QueryState(to: Ref[Accumulator]) extends Command[Accumulator](to)
case class StateQueried(accumulatorRef: Ref[Accumulator], state: Accumulator#State) extends Event
