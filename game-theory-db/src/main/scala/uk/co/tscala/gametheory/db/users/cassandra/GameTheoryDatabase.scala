package uk.co.tscala.gametheory.db.users.cassandra

import com.websudos.phantom.connectors.KeySpaceDef
import com.websudos.phantom.db.DatabaseImpl

/**
  * Created by tom on 06/11/15.
  */
class GameTheoryDatabase(val keyspace: KeySpaceDef) extends DatabaseImpl(keyspace) {
  object users extends ConcreteUsers with keyspace.Connector
}

object GameTheoryDatabase extends GameTheoryDatabase(CassandraDefaults.connector)
