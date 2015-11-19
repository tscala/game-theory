package uk.co.tscala.gametheory.db.games

import com.websudos.phantom.CassandraTable
import com.websudos.phantom.dsl.{IntColumn, Row, StringColumn, DoubleColumn}
import com.websudos.phantom.keys.PartitionKey
import uk.co.tscala.gametheory.domain.Game


abstract class Games extends CassandraTable[ConcreteGames, Game] {

  object gameId extends IntColumn(this) with PartitionKey[Int]
  object name extends StringColumn(this)
  object rating extends DoubleColumn(this)
  object averageRating extends DoubleColumn(this)

  def fromRow(row: Row): Game = {
    Game(
      gameId(row),
      name(row),
      rating(row),
      averageRating(row)
    )
  }
}

