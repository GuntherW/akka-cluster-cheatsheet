package de.codecentric.wittig.akkacluster.actor

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.sharding.ShardRegion
import de.codecentric.wittig.akkacluster.messages.{Hallo, HalloSharded}

class HalloActorSharded extends Actor with ActorLogging {

  override def receive: Receive = state(0)

  def state(i: Int): Receive = {
    case hallo: Hallo =>
      log.info("HalloActor Sharded, message {}, state {}", hallo, i)
      context.become(state(i + 1))
  }
}

object HalloActorSharded {

  def props: Props = Props(new HalloActorSharded)

  val shardName = "halloShard"

  val extractEntityId: ShardRegion.ExtractEntityId = {
    case halloId: HalloSharded => (halloId.name, halloId.hallo)
  }

  val numberOfShards = 3

  val extractShardId: ShardRegion.ExtractShardId = {
    case HalloSharded(name, hallo)   => (name.hashCode % numberOfShards).abs.toString
    case ShardRegion.StartEntity(id) => (id.hashCode % numberOfShards).abs.toString
  }
}
