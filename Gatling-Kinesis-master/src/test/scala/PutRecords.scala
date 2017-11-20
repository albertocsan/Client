import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import cache.helpers.Helpers
import cache.client.{ClientActionBuilder, Device, Workbench}
import scala.concurrent.duration._
import scala.collection.immutable.List

class PutRecords extends Simulation{

  private val batchSize = Helpers.getEnvOrDefault("BatchSize", 20)
  private val testDuration = Helpers.getEnvOrDefault("TestDuration", 1)
  private val kinesisStreamName =  Helpers.getEnvOrDefault("kinesis-stream", "tvmetrix")
  
  val workbench = new Workbench(kinesisStreamName)
  val clientAction = new ClientActionBuilder(batchSize, workbench)

  val testScenario = scenario("Put Records into Kinesis Stream").exec(clientAction)

  setUp(
    testScenario.inject(
      atOnceUsers(1)
    )
  )
}