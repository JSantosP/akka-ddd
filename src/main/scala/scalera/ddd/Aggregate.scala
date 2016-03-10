package scalera.ddd

import scala.reflect.{ClassTag, classTag}

import akka.persistence._
import scalera.ddd.message.Event

import scalera.ddd.message.Command
import scalera.ddd.utils.TypeUtils._

/**
 * An [[Aggregate]] is just an uniquely identified entity
 * capable of updating its internal state based on persistent
 * events associated with received commands.
 * @param id Aggregate unique id.
 * @tparam S State type.
 */
abstract class Aggregate[S,SelfType<:Aggregate[S,SelfType]:ClassTag](id: String) extends PersistentActor
  with Stateful[S]
  with bus.Producer { thisAggregate =>

  override val eventStream = context.system.eventStream

  //  Reflect type info

  private val selfClassTag: ClassTag[SelfType] =
    classTag[SelfType]

  //  Update function types

  type State = Option[S]

  type StateUpdate = PartialFunction[(Event,State),State]

  type EventForCommand = PartialFunction[Command[SelfType],Event]

  //  Aggregate state

  override def persistenceId: String = id

  /**
   * Define the way that the aggregate state should be updated
   * when a new event tries to be applied.
   */
  val stateUpdate: StateUpdate

  /**
   * Define which is the event that results of applying
   * certain command.
   */
  val eventForCommand: EventForCommand

  override def preStart(): Unit = {
    super.preStart()
    context.system.log.info(
      s"$prettyMe Started")
  }

  /*
   * When receiving an event, we'll just update the state
   * using the 'updateState' function. If it's not defined,
   * then do not change the state.
   *
   * When receiving an snapshot offer, just update the state
   * with the given one.
   */
  override def receiveRecover: Receive = {
    case event: Event => updateState(event)
    case SnapshotOffer(_, someState: State) => state = someState
    case RecoveryCompleted => onRecoveryCompleted()
  }

  /*
   * When receiving a command, check if there's a behavior defined for it.
   * If so, persist the event associated with that command.
   * When the event has been persisted, then execute the associated logic
   * to update current aggregate state.
   */
  override def receiveCommand: Receive = {
    case command: Command[SelfType@unchecked]
      if command.aggregateType.isA(selfClassTag)
        && command.addressee.id == id =>
      import context.dispatcher
      context.system.log.info(s"$prettyMe Received $command")
      eventForCommand.lift(command).fold[Unit](){
        event => persist(event){
          persisted =>
            updateState(event)
            context.system.log.info(s"$prettyMe Publishing $persisted")
            publish(persisted)
        }
      }
  }

  override def unhandled(msg: Any): Unit = {
    super.unhandled(msg)
    context.system.log.info(s"$prettyMe Unhandled $msg")
  }

  /**
   * Callback associated when the recovery of the current aggregate is completed
   */
  def onRecoveryCompleted(): Unit = {
    context.system.log.info(s"$prettyMe Recovery completed. State : $state")
  }

  /**
   * Given a event, apply if possible an update to
   * current state with that event.
   */
  private def updateState(event: Event): Unit = {
    state = stateUpdate.lift((event,state)).getOrElse(state)
  }

  private def prettyMe: String =
    s"[$persistenceId]"

}
object Aggregate {

  /**
   * An Aggregate [[Factory]] is just an alias for
   * a function that provides a way of creating an [[Aggregate]]
   * given a [[Ref]].
   */
  type Factory[S,A<:Aggregate[S,A]] = Ref[A] => A

}
