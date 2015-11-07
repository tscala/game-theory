package uk.co.tscala.gametheory.db.users

import com.datastax.driver.core.ResultSet
import com.websudos.phantom.dsl._
import uk.co.tscala.gametheory.domain.User

import scala.concurrent.Future

/**
  * Created by tom on 06/11/15.
  */
// The root connector comes from import com.websudos.phantom.dsl._
abstract class ConcreteUsers extends Users with RootConnector {

  def store(user: User): Future[ResultSet] = {
    insert.value(_.name, user.name)
      .future()
  }

  def getByName(name: String): Future[Option[User]] = {
    select.where(_.name eqs name).one()
  }
}
