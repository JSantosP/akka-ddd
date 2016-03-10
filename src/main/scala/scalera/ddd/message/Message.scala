package scalera.ddd.message

trait Message {

  val timestamp: Long = System.currentTimeMillis()

}
