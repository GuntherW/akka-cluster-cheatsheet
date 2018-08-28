package de.codecentric.wittig.akkacluster.actor.normal

import akka.actor.{Actor, ActorLogging, Props}
import de.codecentric.wittig.akkacluster.messages.Hallo

class HalloActor(name: String) extends Actor with ActorLogging {
  override def receive: Receive = state(0)

  def state(i: Int): Receive = {
    case hallo: Hallo =>
      log.info("Name: {}, Message: {}, State: {}", name, hallo, i)
      context.become(state(i + 1))
  }
}

object HalloActor {
  def props(name: String) = Props(new HalloActor(name))
}
