package uk.co.tscala.gametheory.db.cassandra

import com.websudos.phantom.connectors.ContactPoint

/**
  * Created by tom on 06/11/15.
  */
object CassandraDefaults {
  val connector = ContactPoint.local.keySpace("bgg_scala")
}
