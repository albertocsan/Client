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
  //When you do UPDATE, the system simulate one ALIVE EVENT.
  // PLAY - UPDATE - UPDATEOPERATIONAL - STOP
  val listActions: List[String] = List("PLAY","UPDATEOPERATIONAL","UPDATEOPERATIONAL","UPDATE","STOP")
  //Number Users at once
  val users = 1

  val region = "MONTEREY"
  

  //Number max Users
  val maxUsers =  200
  // Time Injects a given number of users with a linear ramp over a given duration.
  val rampUpTime = 20 
  val runTime = 2
 

  val workbench = new Workbench(kinesisStreamName, sessionType, listActions, region)
  val clientAction = new ClientActionBuilder(workbench)
  //Same number exec(clientAction) that length to listActions
  val testScenario = scenario("Put Records into Kinesis Stream").exec(clientAction).exec(clientAction).exec(clientAction).exec(clientAction).exec(clientAction)


 //Operational
  setUp(
    testScenario.inject(
      atOnceUsers(users)
    )
  )
 //Carga
/*  setUp(
        testScenario.inject(
                rampUsers(maxUsers) over (rampUpTime seconds),
                rampUsers(maxUsers * runTime) over (runTime minutes)
        )
)*/
}