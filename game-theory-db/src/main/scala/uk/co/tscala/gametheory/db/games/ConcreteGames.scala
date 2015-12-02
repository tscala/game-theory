package uk.co.tscala.gametheory.db.games

import com.datastax.driver.core.ResultSet
import com.websudos.phantom.dsl._
import uk.co.tscala.gametheory.domain.Game

import scala.concurrent.Future

/**
  * Games database object.
  *
  * @author tscala
  * @since 06/11/15
  */
abstract class ConcreteGames extends Games with RootConnector {

  def store(game: Game): Future[ResultSet] = {
    insert.value(_.gameId, game.gameId)
      .value(_.name, game.name)
      .value(_.averageRating, game.averageRating)
      .future()
  }

  def getByGameId(gameId: Integer): Future[Option[Game]] = {
    select.where(_.gameId eqs gameId).one()
  }
}
