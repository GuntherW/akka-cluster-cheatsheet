package de.codecentric.wittig.akkacluster.actor
import akka.actor.{Actor, ActorLogging}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{ClusterDomainEvent, MemberUp}

class ClusterListener extends Actor with ActorLogging {

  override def preStart(): Unit =
    Cluster(context.system)
      .subscribe(self, classOf[ClusterDomainEvent])

  override def receive: Receive = {
    case MemberUp(member) => log.info("memberup={}", member.address)
    case event            => log.debug("event={}", event.toString)
  }
}
