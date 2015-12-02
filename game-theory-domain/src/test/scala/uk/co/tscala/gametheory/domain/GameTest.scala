package uk.co.tscala.gametheory.domain

import org.scalatest._

/**
  * @author tscala
  * @since 19/11/15.
  */
class GameTest extends UnitSpec {

  "A game" should "contain an ID field" in {
    val game = new Game(0, "", 0.0)
    assert(game.gameId !== null)
  }
}

/**
  * Simple Spec for unit testing.
  */
abstract class UnitSpec extends FlatSpec with Matchers with OptionValues with Inside
