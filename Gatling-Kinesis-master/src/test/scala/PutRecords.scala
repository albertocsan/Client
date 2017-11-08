import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import cache.helpers.Helpers
import cache.infrastructure.{Event, EventConfig}
import cache.client.{ClientActionBuilder, VODPlayback}
import scala.concurrent.duration._
import scala.collection.immutable.List
//Libreria
import tvmetrix.client.java._
import java.util.HashMap
import org.json._

/*//para dynamic
import scala.collection.mutable.ArraySeq
import io.gatling.core.structure.PopulationBuilder*/

class PutRecords extends Simulation{

  private val batchSize = Helpers.getEnvOrDefault("BatchSize", 20)
  private val testDuration = Helpers.getEnvOrDefault("TestDuration", 1)
  private val kinesisStreamName =  Helpers.getEnvOrDefault("KINESIS_STREAM", "cache-perf-essentials")
  val config = new EventConfig(Event.Event1)

  val vodPlayback = new VODPlayback(config,kinesisStreamName)
  
  val clientAction = new ClientActionBuilder(batchSize, vodPlayback)

  val testScenario = scenario("Put Records into Kinesis Stream").exec(clientAction)
/*
  object TestScenario2{
    val testScenario2  = exec(kinesisAction)
  }
  
  val scnTest2 = scenario("Porbando el test 2").exec(TestScenario2.testScenario2).inject(nothingFor(5 seconds),
      constantUsersPerSec(1) during(testDuration minutes))*/
  

  //TEST LIBRERIA
  val device : HashMap[String, Object] = new HashMap[String, Object]
    device.put("class", "MOBILE")
    device.put("platform", "Android")
    device.put("platformVersion", "6.0")
    device.put("make", "Google, Inc.")
    device.put("model", "Nexus 6")
    device.put("deviceId", "somedeviceid")    
    //device.put("keepalive", 120)

  val configLib : HashMap[String, Object] = new HashMap[String, Object]
    configLib.put("appName", "app")
    configLib.put("appVersion", "app-1.0.0")
    configLib.put("device", device)
    configLib.put("timeFn", new TvMetrixTimeProvider() {
      def getCurrentTime() : Long = {
        println("--->calling getCurrentTime");
        var now:Long = System.currentTimeMillis() 
        return now
      } 
    })
    configLib.put("putFn", new TvMetrixEventSink() {
      def put(action:Object , data:String ) {

      }
    })

   println("configLib " + configLib) 
    // Create TvMetrixClient
  val client = TvMetrix.create(configLib)

  // Create a hashmap representing a new-session action
  val sessionParams : HashMap[String, Object] = new HashMap[String, Object]
  sessionParams.put("language", "SPA")

  val sessionAction : HashMap[String, Object] = new HashMap[String, Object]
  sessionAction.put("action", "new-session")
  sessionAction.put("params", sessionParams)
  // Register action
  val session_log_str : String = client.log(sessionAction)

  println("CHANNEL")
  val channel : HashMap[String, Object] = new HashMap[String, Object]
  channel.put("serviceId", "97007")
  channel.put("channelName", "Canal de las Estrellas")
  channel.put("channelNumber", "116")

  println("DELIVERY")
  val delivery : HashMap[String, Object] = new HashMap[String, Object]
  delivery.put("deliveryContext", "TV")
  delivery.put("serviceId", "97007")

  println("NEWPLAYBACKPARAMS")
  val newPlaybackParams : HashMap[String, Object] = new HashMap[String, Object]
  newPlaybackParams.put("channel", channel)
  newPlaybackParams.put("delivery", delivery)
  newPlaybackParams.put("playtime", "2017-09-01T09:44:14.012Z")
  

  println("NEWPLAYBACK")
  val newPlayback : HashMap[String, Object] = new HashMap[String, Object]
  newPlayback.put("action", "new-playback")
  newPlayback.put("params", newPlaybackParams)
  // Register action

   println("newPlayback: "+ newPlayback)


  val log_str : String = client.log(newPlayback)
  // Dump debug history
  //Thread.sleep(20000)
  println("<-- LOG -->")
  println("Resultado Log:"+log_str)
  

   //val workload = scnTest2.protocols(kinesisProtocol)

  //setUp(workload)

  setUp(
    testScenario.inject(
      nothingFor(10.seconds),
      atOnceUsers(10)
    ).protocols(vodPlayback)
  )
}