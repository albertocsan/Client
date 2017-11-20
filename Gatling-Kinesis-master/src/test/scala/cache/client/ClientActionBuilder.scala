package cache.client

import akka.actor.Props
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.action.{Action, ExitableActorDelegatingAction}
import io.gatling.core.structure.ScenarioContext
import io.gatling.jms.action.JmsReqReply.genName

class ClientActionBuilder(eventCount: Int, protocol: Workbench) extends ActionBuilder {

  override def build(context: ScenarioContext, nextAction: Action): Action = {
    val statsEngine = context.coreComponents.statsEngine
    val putRecordActor = Props(new PutRecordAction(protocol, nextAction, statsEngine, eventCount))

    val actor = context.system.actorOf(putRecordActor)
    new ExitableActorDelegatingAction(genName("dev"), statsEngine, nextAction, actor)
  }
}
