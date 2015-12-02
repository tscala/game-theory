package uk.co.tscala.gametheory.db.denorm.usergames

import com.websudos.phantom.CassandraTable
import com.websudos.phantom.dsl._
import com.websudos.phantom.keys.PartitionKey
import uk.co.tscala.gametheory.domain.{Game, User, UserGame}

/**
  *
  */
abstract class UserGames extends CassandraTable[ConcreteUserGames, UserGame] {

  object gameId extends IntColumn(this) with PartitionKey[Int]

  object name extends StringColumn(this)

  object rating extends DoubleColumn(this)

  object averageRating extends DoubleColumn(this)

  object username extends StringColumn(this) with PrimaryKey[String]

  /**
    * Create a {@link UserGame} object from a {@link Row} in the UserGames table.
    *
    * @param row the row object
    * @return a new UserGame object
    */
  def fromRow(row: Row): UserGame = {
    UserGame(
      User(
        username(row)
      ),
      Game(
        gameId(row),
        name(row),
        averageRating(row)
      )
    )
  }
}

