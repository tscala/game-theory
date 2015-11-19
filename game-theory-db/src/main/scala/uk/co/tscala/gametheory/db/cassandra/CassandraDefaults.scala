package uk.co.tscala.gametheory.db.cassandra

import com.websudos.phantom.connectors.ContactPoint

/**
  * Default settings for the Cassandra connection.
  *
  * @author tscala
  * @since 06/11/15
  */
object CassandraDefaults {
  val connector = ContactPoint.local.keySpace("bgg_scala")
}
