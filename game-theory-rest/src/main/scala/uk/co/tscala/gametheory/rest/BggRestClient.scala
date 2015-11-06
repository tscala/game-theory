package uk.co.tscala.gametheory.rest

import uk.co.tscala.gametheory.db.users.cassandra.{GameTheoryDatabase, CassandraDefaults}
import uk.co.tscala.gametheory.domain.{User, Game}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.xml._
import scala.util.{Success,Failure}
import akka.actor.ActorSystem
import spray.client.pipelining._
import akka.event.Logging
import com.websudos.phantom.dsl._

object BggRestClient extends App with CassandraDefaults.connector.Connector {

  implicit val system = ActorSystem("bgg-rest")
  val log = Logging(system, getClass)

  val collectionPipeline = sendReceive ~> unmarshal[String]
  val guildPipeline = sendReceive ~> unmarshal[NodeSeq]

  def getCollection(name: String): Future[String] = collectionPipeline {
    Get("http://bgg-json.azurewebsites.net/collection/" + name.replaceAll(" ","%20"))
  }

  val guildResponseFuture = guildPipeline {
    Get("http://www.boardgamegeek.com/xmlapi2/guild?id=410&members=1")
  }

  val gameMap = scala.collection.mutable.Map[Game, Int]()

  guildResponseFuture onComplete {
    case Success(xml) =>
      val members = (xml \\ "members" \\ "member" \\ "@name").map(_.text)

      Await.result(GameTheoryDatabase.autocreate.future(), 5.seconds)
      Await.ready(GameTheoryDatabase.users.create.ifNotExists().future(), 5.seconds)

     members.foreach{ name =>
        // update db?

       GameTheoryDatabase.users.store(User(name)).onComplete(res =>
          println("Stored: " + res)
        )
      }

    case Failure(error) =>
      println("Failed to get guild information")
  }

//  guildResponseFuture onComplete {
//    case Success(xml) =>
//      val members = (xml \\ "members" \\ "member" \\ "@name").map(_.text)
//
//      members.foreach{ name => getCollection(name) onComplete {
//        case Success(json) =>
//          val games = json.parseJson.convertTo[List[Game]]
//
//          games.foreach { game =>
//            val count = gameMap.getOrElse(game, 0)
//            gameMap.put(game, count + 1)
//          }
//        case Failure(error) =>
//          println("Failed to get collection for " + name)
//      }
//      }
//
//    case Failure(error) =>
//      println("Failed to get guild information")
//  }
//  Thread.sleep(60000)
//
//  gameMap.foreach{ e =>
//    println(e._1.name + " occurs " + e._2 + " times.")
//  }
}
