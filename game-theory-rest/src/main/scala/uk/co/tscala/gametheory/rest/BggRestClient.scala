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

  val jsonPipeline = sendReceive ~> unmarshal[String]
  val xmlPipeline = sendReceive ~> unmarshal[NodeSeq]

  def getCollection(name: String): Future[String] = jsonPipeline {
    Get("http://bgg-json.azurewebsites.net/collection/" + name.replaceAll(" ", "%20"))
  }

  def getGame(thingId: Int): Future[String] = jsonPipeline {
    Get("http://bgg-json.azurewebsites.net/thing/" + thingId)
  }

  val guildResponseFuture = xmlPipeline {
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
          println("Stored user: " + res)
        )

        getCollection(name) onComplete {
          case Success(json) =>
            import uk.co.tscala.gametheory.rest.json.BggJsonProtocol._
            val games = json.parseJson.convertTo[List[Game]]

            games.foreach { game =>

              GameTheoryDatabase.games.store(game).onComplete(res =>
                println("Stored game: " + res)
              )

              GameTheoryDatabase.userGames.store(UserGame(User(name), game)).onComplete(res =>
                println("Stored userGame: " + res)
              )
            }

            (1000 to 9000).foreach(i => getGame(i) onComplete {
              case Success(json) =>

                // Something like this....
               val owned =  json.parseJson.asJsObject.fields.getOrElse("owned", false)

                // only add the entry to the DB if it is owned...
                if (owned) {
                  println "Owned"
                }

                val game = json.parseJson.convertTo[Game]

                GameTheoryDatabase.games.store(game).onComplete(res =>
                  println("Stored game: " + res)
                )
            })
          case Failure(error) =>
            println("Failed to get collection for " + name)
        }
      }

    case Failure(error) =>
      println("Failed to get guild information")
  }
}
