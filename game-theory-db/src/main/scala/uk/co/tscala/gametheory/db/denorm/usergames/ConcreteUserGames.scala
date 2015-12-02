package uk.co.tscala.gametheory.db.denorm.usergames

import com.datastax.driver.core.ResultSet
import com.websudos.phantom.dsl._
import uk.co.tscala.gametheory.domain.UserGame

import scala.concurrent.Future

/**
  * Implementation of the database objects for the game-theory project.
  *
  * @author tscala
  * @since 06/11/15
  */
abstract class ConcreteUserGames extends UserGames with RootConnector {

  def store(userGame: UserGame): Future[ResultSet] = {
    insert.value(_.gameId, userGame.game.gameId)
      .value(_.username, userGame.user.name)
      .value(_.name, userGame.game.name)
      .value(_.averageRating, userGame.game.averageRating)
      .future()
  }

  def getByUserAndGameId(username: String, gameId: Integer): Future[Option[UserGame]] = {
    select.where(_.gameId eqs gameId).and(_.username eqs username).one()
  }
}
