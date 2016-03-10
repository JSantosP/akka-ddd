package scalera.ddd

import akka.actor._
import akka.testkit.{TestProbe, TestKit}
import org.scalatest._
import scalera.ddd.message.Event

class AggregateTest extends TestKit(testSystem)
  with FlatSpecLike
  with Matchers {

  behavior of "Aggregate"

  it should "update its state when it reacts to some command" in {

    val consumer = TestProbe()
    system.eventStream.subscribe(consumer.ref, classOf[Event])

    val accuRef = reference[Accumulator]
    val accu = system.actorOf(
      Props(new Accumulator(accuRef.id)),
      accuRef.id)

    //  we send an Add(1) to the aggregate

    accu ! Add(1, accuRef)

    val Seq(Added(ref, n)) = consumer.receiveN(1)
    ref shouldEqual accuRef
    n shouldEqual 1

    accu ! QueryState(accuRef)

    val Seq(StateQueried(ref1, state1)) = consumer.receiveN(1)
    ref1 shouldEqual accuRef
    state1 shouldEqual Option(1)

  }

  it should "restore its state when reviving it" in {

    val consumer = TestProbe()
    system.eventStream.subscribe(consumer.ref, classOf[Event])

    val accuRef = reference[Accumulator]
    val accu = system.actorOf(
      Props(new Accumulator(accuRef.id)),
      accuRef.id)

    //  we send an Add(1) to the aggregate

    accu ! Add(1, accuRef)

    val Seq(Added(ref, n)) = consumer.receiveN(1)
    ref shouldEqual accuRef
    n shouldEqual 1

    //  we now kill the aggregate

    accu ! Kill

    //FIXME Wait until accu is dead in a fancier way
    Thread.sleep(3000)

    val accuRevived = system.actorOf(
      Props(new Accumulator(accuRef.id)),
      accuRef.id)

    accuRevived ! Add(1, accuRef)
    accuRevived ! QueryState(accuRef)

    val Seq(_,StateQueried(ref2, state2)) = consumer.receiveN(2)
    ref2 shouldEqual accuRef
    state2 shouldEqual Option(2)

  }
}
