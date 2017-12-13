import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import cache.helpers.Helpers
import cache.client.{ClientActionBuilder, Device, Workbench}
import scala.concurrent.duration._
import scala.collection.immutable.List
import java.time.Instant


import tvmetrix.client.java._
import java.util.HashMap
import cache.client._
import java.util.ArrayList


class ProbarLibreria extends Simulation{

  private val batchSize = Helpers.getEnvOrDefault("BatchSize", 20)
  private val testDuration = Helpers.getEnvOrDefault("TestDuration", 1)
  private val kinesisStreamName =  Helpers.getEnvOrDefault("kinesis-stream", "tvmetrix")

  val workbench = new Workbench(kinesisStreamName)
  val clientAction = new ClientActionBuilder(batchSize, workbench)
  
  val testScenario = scenario("Put Records into Kinesis Stream").exec(clientAction).pause(2).exec(clientAction)

  val utils =  new Utils()
  val deviceInfo = utils.getDevice()
  val device : HashMap[String, Object] = new HashMap[String, Object]
    device.put("class", deviceInfo.`class`)
    device.put("platform", deviceInfo.platform)
    device.put("platformVersion", deviceInfo.platformVersion)
    device.put("make", deviceInfo.make)
    device.put("model", deviceInfo.model)
    device.put("deviceId", deviceInfo.deviceId)  

    val configLib : HashMap[String, Object] = new HashMap[String, Object]
    configLib.put("appName", "app")
    configLib.put("appVersion", "app-1.0.0")
    configLib.put("device", device)

    configLib.put("timeFn", new TvMetrixTimeProvider() {
      def getCurrentTime() : Long = {
        var now:Long = System.currentTimeMillis() 
        return now
      } 
    })
    configLib.put("putFn", new TvMetrixEventSink() {
      def put(action:Object , data:String ) {
      }
    })
    val client  = TvMetrix.create(configLib)

  val sessionParams : HashMap[String, Object] = new HashMap[String, Object]
    sessionParams.put("language", "SPA")

    val sessionAction : HashMap[String, Object] = new HashMap[String, Object]
    sessionAction.put("action", "new-session")
    sessionAction.put("params", sessionParams)

    client.log(sessionAction)

  val vodContent  = utils.getVod()
  val trackContent = utils.getTrack()
  val profileContent = utils.getProfile()
  var playposition : Int = 0


  val content : HashMap[String, Object] = new HashMap[String, Object]
    content.put("contentId", vodContent.content.contentId)
    //content.put("genres",genres)
    //content.put("parentalRating", vodContent.content.parentalRating)
    content.put("title", vodContent.content.title)

    val productParams : HashMap[String, Object] = new HashMap[String, Object]
    productParams.put("price",new Integer (vodContent.product.price))
    productParams.put("productId", vodContent.product.productId)
    productParams.put("productName", vodContent.product.productName)
    productParams.put("system", vodContent.product.system)
    productParams.put("commercialType", vodContent.product.commercialType)

    /*val options : HashMap[String, Object] = new HashMap[String, Object]
    options.put("videoFormat", "HD")
    options.put("audioFormat", "stereo")
    options.put("audioMode", "decode")*/

    val delivery : HashMap[String, Object] = new HashMap[String, Object]
    /*delivery.put("deliveryId", vodContent.delivery.deliveryId)
    delivery.put("audioLanguages",audioLanguages)
    delivery.put("audioFormats",audioFormats)*/
    delivery.put("deliveryContext", vodContent.delivery.deliveryContext)

    /*val subscription : HashMap[String, Object] = new HashMap[String, Object]
    subscription.put("subscriptionPackageId", "1297969")
    subscription.put("subscriptionServiceId", "173")
    subscription.put("subscriptionServiceName", "urn:tve:hbo")
    subscription.put("subscriptionPackageName", "SVOD - Full")*/

    val tracks : HashMap[String, Object] = new HashMap[String, Object]
    tracks.put("type", trackContent.`type`)
    tracks.put("coding", trackContent.coding)
    tracks.put("resolution", trackContent.resolution)

    val streamingQuality : HashMap[String, Object] = new HashMap[String, Object]
    streamingQuality.put("bufferLengthTime", new Integer (1))

    var availableBitrates = new ArrayList[Int]()
    availableBitrates.add(1)
    availableBitrates.add(2)

    val profile : HashMap[String, Object] = new HashMap[String, Object]
    profile.put("bitrate", new Integer (profileContent.bitrate))
    profile.put("resolution", deviceInfo.resolution)
    profile.put("frameRate", new Integer (profileContent.frameRate))

    val streaming : HashMap[String, Object] = new HashMap[String, Object]
    streaming.put("availableBitrates", availableBitrates)
    streaming.put("currentProfile", profile)

    val vst : HashMap[String, Object] = new HashMap[String, Object]
    vst.put("totalTime", new Integer (1))
    vst.put("ottProvisionTime", new Integer (2))
    vst.put("deeplinkTime", new Integer (3))
    vst.put("drmSetupTime", new Integer (4))
    vst.put("authoringTime", new Integer (5))

    val params : HashMap[String, Object] = new HashMap[String, Object]
    params.put("content", content)
    params.put("product", productParams)
    //params.put("options", options)
    params.put("delivery", delivery)
    params.put("tracks", tracks)
    params.put("streaming", streaming)
    params.put("streamingQuality", streamingQuality)
    params.put("vst", vst)

    //params.put("subscription", subscription)
    params.put("playposition", new Integer(generatePlayposition()))
    params.put("pageName", "Kids Home|George De La Selva")
    
    val playback : HashMap[String, Object] = new HashMap[String, Object]
    playback.put("action", "new-playback")
    playback.put("params", params)
    println("LLAMADA LIBRERIA")
    client.log(playback)


private def generatePlayposition() : Int = {
    
    val random = scala.util.Random
    this.playposition = random.nextInt(100000)
    println("RANDOM: " + playposition)
    return playposition
  }
 
  setUp(
    testScenario.inject(
      atOnceUsers(1)
    )
  )
}