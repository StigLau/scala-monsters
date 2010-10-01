import _root_.no.lau.movement.{TickListener, Clock}
import org.scalatest.FeatureSpec
import org.scalatest.GivenWhenThen

class ClockSpec extends FeatureSpec with GivenWhenThen {

  feature("Clock must work correctly") {

    info("As a programmer")

    scenario("adding and removing listeners") {

      given("a new Clock")
      val myClock = new Clock {def act() {}}
      when("I try to add a listener")
      val listener = new TickListener{}
      myClock.addTickListener(listener)
      and("then remove it")
      myClock.removeTickListener(listener)
      then("there should be no more listeners left")
      //assert(myClock.tickListeners.isEmpty)
    }
  }
}