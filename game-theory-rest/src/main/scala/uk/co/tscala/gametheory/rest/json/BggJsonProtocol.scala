package uk.co.tscala.gametheory.rest.json

import spray.json.DefaultJsonProtocol
import uk.co.tscala.gametheory.domain.Game

object BggJsonProtocol extends DefaultJsonProtocol {
    implicit val gameFormat = jsonFormat4(Game)
}
