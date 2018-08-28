package de.codecentric.wittig.akkacluster.actor.crdt

import java.util.concurrent.ThreadLocalRandom

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.Cluster
import akka.cluster.ddata.{DistributedData, ORSet, ORSetKey}
import akka.cluster.ddata.Replicator._

class CrdtSet extends Actor with ActorLogging {
  import CrdtSet._

  private val replicator = DistributedData(context.system).replicator
  private implicit val node: Cluster = Cluster(context.system)

  private var addRemove = true

  replicator ! Subscribe(DataKey, self)

  def receive = {
    case MyMessage(msg) =>
      if (addRemove) {
        log.info("Adding: {}", msg)
        replicator ! Update(DataKey, ORSet.empty[String], WriteLocal)(_ + msg)
      } else {
        log.info("Removing: {}", msg)
        replicator ! Update(DataKey, ORSet.empty[String], WriteLocal)(_ - msg)
      }
      addRemove = !addRemove

    case _: UpdateResponse[_] => // ignore

    case c @ Changed(DataKey) =>
      val data = c.get(DataKey)
      log.info("Current elements: {}", data.elements)
  }
}

object CrdtSet {
  val DataKey: ORSetKey[String] = ORSetKey[String]("key")
  case class MyMessage(msg: String)
  def props: Props = Props(new CrdtSet())
  def randomString: String = ThreadLocalRandom.current().nextInt(97, 123).toChar.toString
}
