import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import cache.helpers.Helpers
import cache.client.{ClientActionBuilder, Device, Workbench}
import scala.concurrent.duration._
import scala.collection.immutable.List
import java.time.Instant
import tvmetrix.client.java._
import java.util.HashMap




class AnalyticsClient extends Simulation{

  private val batchSize = Helpers.getEnvOrDefault("BatchSize", 20)
  private val testDuration = Helpers.getEnvOrDefault("TestDuration", 1)
  private val kinesisStreamName =  Helpers.getEnvOrDefault("kinesis-stream", "tvmetrix")
  
  // VOD OR LIVE
  val sessionType: String = "LIVE"
  // PLAY - UPDATE - UPDATECODEC - UPDATEPROFILE - UPDATEBANDWIDTH - UPDATECONNECTION - STOP
  val listActions: List[String] = List("PLAY","UPDATECONNECTION","UPDATE")

  val workbench = new Workbench(kinesisStreamName, sessionType, listActions)
  val clientAction = new ClientActionBuilder(batchSize, workbench)
 //Same number exec(clientAction) that length to listActions
  val testScenario = scenario("Put Records into Kinesis Stream").exec(clientAction).exec(clientAction).pause(2).exec(clientAction)
 
 //Number Users
 val users = 1

 
  setUp(
    testScenario.inject(
      atOnceUsers(users)
    )
  )
}