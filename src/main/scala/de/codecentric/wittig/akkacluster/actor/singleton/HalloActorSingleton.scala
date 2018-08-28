package de.codecentric.wittig.akkacluster.actor.singleton

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, PoisonPill, Props}
import akka.cluster.singleton._
import akka.util.Timeout
import de.codecentric.wittig.akkacluster.messages.Hallo

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

class HalloActorSingleton(name: String) extends Actor with ActorLogging {

  override def receive: Receive = state(0)

  def state(i: Int): Receive = {
    case hallo: Hallo => {
      log.info("HalloActor {}, message {}, i {}", name, hallo, i)
      context.become(state(i + 1))
    }
  }
}

object HalloActorSingleton {
  def props(name: String) = Props(new HalloActorSingleton(name))
  private def nameProxy(name: String) = name + "Proxy"
  private implicit val timeout: Timeout = Timeout(FiniteDuration(1, TimeUnit.SECONDS))

  def getOrCreate(system: ActorSystem, name: String)(implicit ex: ExecutionContext): Future[ActorRef] = {

    system
      .actorSelection("/user/" + nameProxy(name))
      .resolveOne()
      .recover {
        case NonFatal(ex) =>
          createSingleton(system, name)
          createProxy(system, name)
      }

  }

  private def createSingleton(system: ActorSystem, name: String) =
    system.actorOf(
      ClusterSingletonManager.props(
        singletonProps = HalloActorSingleton.props(name),
        terminationMessage = PoisonPill,
        settings = ClusterSingletonManagerSettings(system)
      ),
      name = name
    )

  private def createProxy(system: ActorSystem, name: String) = system.actorOf(
    ClusterSingletonProxy.props(
      singletonManagerPath = "/user/" + name,
      settings = ClusterSingletonProxySettings(system)
    ),
    name = nameProxy(name)
  )
}
