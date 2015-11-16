package uk.co.tscala.gametheory.db.denorm.usergames

import com.datastax.driver.core.ResultSet
import com.websudos.phantom.dsl._
import uk.co.tscala.gametheory.domain.UserGame

import scala.concurrent.Future

/**
  * Created by tom on 06/11/15.
  */
// The root connector comes from import com.websudos.phantom.dsl._
abstract class ConcreteUserGames extends UserGames with RootConnector {

  //TODO: does this need to be a single, more complex object?
  def store(userGame: UserGame): Future[ResultSet] = {
    insert.value(_.gameId, userGame.game.gameId)
      .value(_.username, userGame.user.name)
      .value(_.rating, userGame.game.rating)
      .value(_.name, userGame.game.name)
      .value(_.averageRating, userGame.game.averageRating)
      .future()
  }

  def getByUserAndGameId(username: String, gameId: Integer): Future[Option[UserGame]] = {
    select.where(_.gameId eqs gameId).and(_.username eqs username).one()
    // TODO: this select needs to use the username as well
    // TODO: how do we handle domain objects in denormalised tables?
  }
}
