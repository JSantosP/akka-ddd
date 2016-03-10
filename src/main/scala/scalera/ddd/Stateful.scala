package scalera.ddd

/**
 * Just something with an updatable typed state.
 */
trait Stateful[S] {

  private var _initialized: Boolean = false

  private var _state: Option[S] = None

  def state_=(newState: Option[S]): Unit =
    synchronized{
      _initialized = true
      _state = newState
    }

  def state: Option[S] =
    synchronized(_state)

  def initialized: Boolean =
    synchronized(_initialized)

}
