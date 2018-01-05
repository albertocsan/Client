package cache.client

import io.gatling.commons.stats.OK
import io.gatling.commons.util.TimeHelper
import io.gatling.core.action.{Action, ActionActor}
import io.gatling.core.session.Session
import io.gatling.core.stats.StatsEngine
import io.gatling.core.stats.message.ResponseTimings

class PutRecordAction(protocol: Workbench, val next: Action, statsEngine: StatsEngine) extends ActionActor {

  override def execute(session: Session): Unit = {
    val start = TimeHelper.nowMillis
    //println("SESSION USER ID:"+session.userId)
    //println("")
    protocol.execute(session.userId, protocol)
    val end = TimeHelper.nowMillis

    val timings = ResponseTimings(start, end)
    statsEngine.logResponse(session, "Put Records", timings, OK, None, None)
    next ! session
  }
}
