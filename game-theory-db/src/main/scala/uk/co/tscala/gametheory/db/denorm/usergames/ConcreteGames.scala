package uk.co.tscala.gametheory.db.denorm.usergames

import com.datastax.driver.core.ResultSet
import com.websudos.phantom.dsl._
import uk.co.tscala.gametheory.domain.{Game, User}

import scala.concurrent.Future

/**
  * Created by tom on 06/11/15.
  */
// The root connector comes from import com.websudos.phantom.dsl._
abstract class ConcreteGames extends Games with RootConnector {

  //TODO: does this need to be a single, more complex object?
  def store(game: Game, user, User): Future[ResultSet] = {
    insert.value(_.gameId, game.gameId)
      .value(_.name, game.name)
      .value(_.rating, game.rating)
      .value(_.averageRating, game.averageRating)
      .future()
  }

  def getByUserAndGameId(username: String, gameId: Integer): Future[Option[UserGame]] = {
    select.where(_.gameId eqs gameId).one()
    // TODO: this select needs to use the username as well
    // TODO: how do we handle domain objects in denormalised tables?
  }
}
