package uk.co.tscala.gametheory.db.users

import com.websudos.phantom.CassandraTable
import com.websudos.phantom.dsl.{Row, StringColumn}
import com.websudos.phantom.keys.PartitionKey
import uk.co.tscala.gametheory.db.users.cassandra.ConcreteUsers
import uk.co.tscala.gametheory.domain.User

class Users extends CassandraTable[ConcreteUsers, User] {

  object name extends StringColumn(this) with PartitionKey[String]

  def fromRow(row: Row): User = { User(name(row)) }
}

