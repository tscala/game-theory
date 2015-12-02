package uk.co.tscala.gametheory.analysis.spark.graphx

import org.apache.spark.SparkContext
import com.datastax.spark.connector._
import org.apache.spark.graphx._

/**
  * @author tscala
  * @since 02/12/15.
  */
class SimpleGraphOperations {

  val sc = new SparkContext()


  def buildGraph() : Graph = {

    // Load table RDD from Cassandra...
    val userGames = sc.cassandraTable("bgg_scala", "usergames")

    // Create verticies...
    val verticies = userGames.map(e => (e.getInt("id"), e.getString("name")))

    // Create edges...


    null
  }

}
