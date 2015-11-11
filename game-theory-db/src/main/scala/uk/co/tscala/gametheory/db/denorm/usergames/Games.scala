package uk.co.tscala.gametheory.db.denorm.usergames

import com.websudos.phantom.CassandraTable
import com.websudos.phantom.dsl.{IntColumn, Row, StringColumn, DoubleColumn}
import com.websudos.phantom.keys.PartitionKey
import uk.co.tscala.gametheory.domain.{Game, User}


class UserGames extends CassandraTable[ConcreteUserGames, UserGame] {

  object gameId extends IntColumn(this) with PartitionKey[Int]
  object name extends StringColumn(this)
  object rating extends DoubleColumn(this)
  object averageRating extends DoubleColumn(this)
  object userName extends StringColumn(this) with PartitionKey[String]

  def fromRow(row: Row): UserGame = {
    UserGame(
      User(
        name(row)
      ),
      Game(
        gameId(row),
        name(row),
        rating(row),
        averageRating(row)
      )
    )
  }
}

