package de.codecentric.wittig.akkacluster.actor.persistence
import akka.actor._
import akka.persistence._

case class Cmd(data: String)
case class Evt(data: String)

case class ExampleState(events: List[String] = Nil) {
  def updated(evt: Evt): ExampleState = copy(evt.data :: events)
  def size: Int = events.length
  override def toString: String = events.reverse.toString
}

class ExamplePersistentActor extends PersistentActor {

  val snapShotInterval = 1000
  var state = ExampleState()

  override def persistenceId = "sample-id-1"

  def updateState(event: Evt): Unit =
    state = state.updated(event)

  def numEvents =
    state.size

  val receiveRecover: Receive = {
    case evt: Evt ⇒ updateState(evt)
    case SnapshotOffer(_, snapshot: ExampleState) ⇒ state = snapshot
  }

  val receiveCommand: Receive = {
    case Cmd(data) ⇒
      persist(Evt(s"${data}-${numEvents}")) { event ⇒
        updateState(event)
        context.system.eventStream.publish(event)
        if (lastSequenceNr % snapShotInterval == 0 && lastSequenceNr != 0)
          saveSnapshot(state)
      }
    case "print" ⇒ println(state)
  }

}
