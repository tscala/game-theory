package uk.co.tscala.gametheory.db.cassandra

import com.websudos.phantom.connectors.KeySpaceDef
import com.websudos.phantom.db.DatabaseImpl
import uk.co.tscala.gametheory.db.games.ConcreteGames
import uk.co.tscala.gametheory.db.users.ConcreteUsers
import uk.co.tscala.gametheory.db.denorm.usergames.ConcreteUserGames

/**
  * Implementation of the database objects for the game-theory project.
  *
  * @author tscala
  * @since 06/11/15
  */
sealed class GameTheoryDatabase(val keyspace: KeySpaceDef) extends DatabaseImpl(keyspace) {

  object users extends ConcreteUsers with keyspace.Connector

  object games extends ConcreteGames with keyspace.Connector

  object userGames extends ConcreteUserGames with keyspace.Connector
}

/**
  * Database singleton object.
  */
object GameTheoryDatabase extends GameTheoryDatabase(CassandraDefaults.connector)
