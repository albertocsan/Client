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

  private val kinesisStreamName =  Helpers.getEnvOrDefault("kinesis-stream", "tvmetrix")
  
  // VOD OR LIVE
  val sessionType: String = "VOD"
  // PLAY - UPDATE - UPDATECODEC - UPDATEPROFILE - UPDATEBANDWIDTH - UPDATECONNECTION - STOP
  val listActions: List[String] = List("PLAY","UPDATECONNECTION","STOP")
  //Number Users at once
  val users = 100

  //Number max Users
  val maxUsers =  200
  // Time Injects a given number of users with a linear ramp over a given duration.
  val rampUpTime = 20 
  val runTime = 2
 
  //keepalive
  val keepalive = 2

  val workbench = new Workbench(kinesisStreamName, sessionType, listActions, keepalive)
  val clientAction = new ClientActionBuilder(workbench)
  //Same number exec(clientAction) that length to listActions
  val testScenario = scenario("Put Records into Kinesis Stream").exec(clientAction).exec(clientAction).exec(clientAction)


 //Operational
 /* setUp(
    testScenario.inject(
      atOnceUsers(users)
    )
  )*/
 //Carga
  setUp(
        testScenario.inject(
                rampUsers(maxUsers) over (rampUpTime seconds),
                rampUsers(maxUsers * runTime) over (runTime minutes)
        )
)
}