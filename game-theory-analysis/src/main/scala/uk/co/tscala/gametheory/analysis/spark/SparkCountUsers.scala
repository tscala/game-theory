package uk.co.tscala.gametheory.analysis.spark

import org.apache.spark.{SparkContext, SparkConf}
import com.datastax.spark.connector._

  /** Computes an approximation to pi */
object SparkCountUsers {
      val conf = new SparkConf().set("spark.cassandra.connection.host","localhost")
        .setMaster("local[6]").setAppName("Spark Pi")
      val spark = new SparkContext(conf)

    val userTable = spark.cassandraTable[(String)]("bgg_scala", "users")

      val count = userTable.map(n => n.length).reduce(_+_)
      println("The user table contains "+ count +" characters.")
      spark.stop()
}
