package de.codecentric.wittig.akkacluster

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{ClusterDomainEvent, MemberUp}
import akka.cluster.ddata.{DistributedData, ORSet, ReplicatedData}
import akka.cluster.ddata.Replicator.{Get, GetSuccess, ReadLocal}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import akka.pattern.ask
import akka.util.Timeout
import de.codecentric.wittig.akkacluster.actor._
import de.codecentric.wittig.akkacluster.crdt.CrdtSet
import de.codecentric.wittig.akkacluster.crdt.CrdtSet.MyMessage
import de.codecentric.wittig.akkacluster.messages.{Hallo, HalloSharded}

import scala.concurrent.duration.DurationDouble
import scala.concurrent.{ExecutionContextExecutor, Future}

object Main extends App {

  implicit val system: ActorSystem = ActorSystem("Gunthers")
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  val actor = system.actorOf(Props[ClusterListener])
  val hallo = Hallo("Test")

//  einfach()
//  singleton()
//  sharded()

  crdt()

  def einfach() = {
    (1 to 5).foreach { i =>
      val hallo = system.actorOf(HalloActor.props(s"${i}Heiner"))
      system.scheduler.scheduleOnce(1 seconds, hallo, hallo)
    }
  }

  def singleton() = {
    val proxy: Future[ActorRef] = HalloActorSingleton.getOrCreate(system, "der-wahre-Jacob")
    proxy.map { p =>
      system.scheduler.scheduleOnce(1 second, p, hallo)
      system.scheduler.scheduleOnce(5 second, p, hallo)
      system.scheduler.scheduleOnce(10 second, p, hallo)
//      system.scheduler.scheduleOnce(11 second, p, PoisonPill)
    }
  }

  def sharded() = {

    ClusterSharding(system).start(
      typeName = HalloActorSharded.shardName,
      entityProps = HalloActorSharded.props,
      settings = ClusterShardingSettings(system),
      extractEntityId = HalloActorSharded.extractEntityId,
      extractShardId = HalloActorSharded.extractShardId
    )

    val counterRegion: ActorRef = ClusterSharding(system).shardRegion(HalloActorSharded.shardName)
    val hs = HalloSharded("1Heiner", hallo)
    counterRegion ! hs
    system.scheduler.scheduleOnce(1 second, counterRegion, hs.copy(name = "2Heiner"))
    system.scheduler.scheduleOnce(4 second, counterRegion, hs.copy(name = "3Heiner"))
    system.scheduler.scheduleOnce(8 second, counterRegion, hs.copy(name = "3Heiner"))
    system.scheduler.scheduleOnce(12 second, counterRegion, hs.copy(name = "1Heiner"))
  }

  def crdt() = {
    Thread.sleep(3000)
    val setCrdt = system.actorOf(CrdtSet.props)
    (1 to 10).foreach { i =>
      system.scheduler.scheduleOnce(i seconds, setCrdt, MyMessage("Hallo " + CrdtSet.randomString))
    }

    implicit val node: Cluster = Cluster(system)
    val replicator = DistributedData(system).replicator
    implicit val timeout: Timeout = Timeout(5 seconds) // needed for `?` below

    Thread.sleep(10000)
    val f = replicator ? Get(CrdtSet.DataKey, ReadLocal)

    f.foreach {
      case g @ GetSuccess(CrdtSet.DataKey, req) =>
        val x: ORSet[String] = g.get(CrdtSet.DataKey)
        println(x.elements)
      case sonst => println(sonst)
    }

  }
}
