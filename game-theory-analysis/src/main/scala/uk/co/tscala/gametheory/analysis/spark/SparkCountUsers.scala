package uk.co.tscala.gametheory.analysis.spark

import org.apache.spark.{SparkContext, SparkConf}
import com.datastax.spark.connector._

object SparkCountUsers {
      val conf = new SparkConf().set("spark.cassandra.connection.host","localhost")
        .setMaster("local[6]").setAppName("Spark Pi")
      val spark = new SparkContext(conf)

      val userTable = spark.cassandraTable[(String)]("bgg_scala", "users")

      val count = userTable.map(n => n.length).reduce(_+_)
      println("The user table contains "+ count +" characters.")
      spark.stop()
}

class SimpleUserGameOperations {

  val conf = new SparkConf().set("spark.cassandra.connection.host","localhost")
        .setMaster("local[6]").setAppName("Spark Pi")
      val spark = new SparkContext(conf)

      val userGames = spark.cassandraTable("bgg_scala", "usergames")
      
//      // Most popular (by count)
//      userGames.map(e => (e.gameId,1))
//               .reduceByKey(_.2 + _.2)
//               .map(e => e.swap)
//               .sortByKey(1, 1) // Issue with repeated keys?
//               .take(10)
//               .foreach{
//        	     println _.1 + ": " + _.2
//		       }
//
//	// Most popular, by rating (counting the ratings in this table)
//	userGames.filter(e => e.rating > 0)
//			 .map(e => (e.gameId, e.rating))
//			 .combineByKey(v => (v,1), x, v => (x(0) + v, x(1) + 1), x, y => (x(0)+y(0),x(1)+y(1)))
//			 .map(l,(s,c) => (l , s/c)
//			 .collectAsMap()
//			 .foreach{ println }
			 
	// Largest differences from BGG average rating?
	
	// Pagerank
	
	// Ownership graph
	
	// Ownership by numbers - most featured, user with the most
	
	// Performance improvements - refactoring, caching?
	
	// Widen db? Collect more information? - perhaps follow on from graph
	
	// Graph visualisation using Zeppelin?
	
	// General improvements - how to make this usable? Can all of this be accessed through 
	// a simple dashboard (like Zeppelin)? Simple gradle tasks for running some commonly
	// used operations (e.g. db refresh, simple analytics)?
	
	// Install/move to cluster - can a web UI be driven from the master node? (does Zeppelin
	// do any/much intensive work?) Is there enough disk space to run cassandra (few GB per
	// node). Is there enough memory to cache data on individual nodes. How to effectively
	// use partitioning tto improve performance on "real" hardware?
	
}
