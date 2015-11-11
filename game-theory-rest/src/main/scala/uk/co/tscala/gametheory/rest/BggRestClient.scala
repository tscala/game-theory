package uk.co.tscala.gametheory.rest

import akka.actor.ActorSystem
import akka.event.Logging
import com.websudos.phantom.dsl._
import spray.client.pipelining._
import spray.json._
import uk.co.tscala.gametheory.db.cassandra.{CassandraDefaults, GameTheoryDatabase}
import uk.co.tscala.gametheory.domain.{UserGame, Game, User}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}
import scala.xml._

object BggRestClient extends App with CassandraDefaults.connector.Connector {

  implicit val system = ActorSystem("bgg-rest")
  val log = Logging(system, getClass)

  val collectionPipeline = sendReceive ~> unmarshal[String]
  val guildPipeline = sendReceive ~> unmarshal[NodeSeq]

  def getCollection(name: String): Future[String] = collectionPipeline {
    Get("http://bgg-json.azurewebsites.net/collection/" + name.replaceAll(" ", "%20"))
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
      Await.ready(GameTheoryDatabase.games.create.ifNotExists().future(), 5.seconds)
      Await.ready(GameTheoryDatabase.userGames.create.ifNotExists().future(), 5.seconds)

      members.foreach { name =>
        // update db?

        GameTheoryDatabase.users.store(User(name)).onComplete(res =>
          println("Stored: " + res)
        )

        getCollection(name) onComplete {
          case Success(json) =>
            import uk.co.tscala.gametheory.rest.json.BggJsonProtocol._
            val games = json.parseJson.convertTo[List[Game]]

            games.foreach { game =>

              GameTheoryDatabase.games.store(game).onComplete(res =>
                println("Stored: " + game.name + " (" + res + ")")
              )

              GameTheoryDatabase.userGames.store(UserGame(User(name), game)).onComplete(res =>
                println("Stored: " + game.name + ", " + name +  " (" + res + ")")
              )

            }
          case Failure(error) =>
            println("Failed to get collection for " + name)
        }
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
