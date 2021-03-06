package cache.client

import java.nio.ByteBuffer
import com.amazonaws.AmazonClientException
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.kinesis.AmazonKinesisClient
import com.amazonaws.services.kinesis.model.{PutRecordRequest}
import io.gatling.core.protocol._
import org.joda.time.DateTime
import scala.collection.JavaConverters._
import tvmetrix.client.java._
import java.util.HashMap


class Device(kinesisStream: String, sessionType: String, listActions: List[String], region: String) extends Protocol {
  val utils =  new Utils()
  val deviceInfo = utils.getDevice()
  val kinesisClient = new AmazonKinesisClient()
  var regionAWS = Regions.US_EAST_1
  kinesisClient.setRegion(Region.getRegion(regionAWS))
  checkIsAuthorised(kinesisClient)
  val client  = TvMetrix.create(msgDeviceInfo())
  var index = 0
  val vod = new VOD(client, listActions)
  val live = new LIVE(client, listActions)
  val dvr = new DVR(client,listActions)
  
  def execute() = {
    var sentToKinesis = ""

    if (index == 0) {
      sentToKinesis = buildStart()
      println("SENT TO KINESIS: " + sentToKinesis)
      val request = new PutRecordRequest()
      request.setStreamName(kinesisStream)
      val jsonPayload = serialNumberGenerator(sentToKinesis)
      request.setData(ByteBuffer.wrap(jsonPayload.getBytes()))
      request.setPartitionKey(deviceInfo.deviceId)
      val response = kinesisClient.putRecord(request)
      println("KINESIS OK")
     
      this.index = 1
    } 

    if (sessionType == "VOD") {
      sentToKinesis = vod.executeNextAction(deviceInfo.resolution)
    }

    if (sessionType == "LIVE"){
      sentToKinesis = live.executeNextAction(deviceInfo.resolution)
    }

    if (sessionType == "DVR"){
      sentToKinesis = dvr.executeNextAction(deviceInfo.resolution)
    }
    
    //  <---- KINESIS ---->
    if (sentToKinesis != ""){
      println("SENT TO KINESIS: " + sentToKinesis)
      println("")
      val request = new PutRecordRequest()
      request.setStreamName(kinesisStream)
      val jsonPayload = serialNumberGenerator(sentToKinesis)
      request.setData(ByteBuffer.wrap(jsonPayload.getBytes()))
      request.setPartitionKey(deviceInfo.deviceId)
      kinesisClient.putRecord(request) 
      println("KINESIS OK")
     }
  }

  //<---- GENERATE DEVICE ---->
  def msgDeviceInfo() : HashMap[String,Object] = {

    
   
    val device : HashMap[String, Object] = new HashMap[String, Object]
    device.put("class", deviceInfo.`class`)
    device.put("platform", deviceInfo.platform)
    device.put("platformVersion", deviceInfo.platformVersion)
    device.put("make", deviceInfo.make)
    device.put("model", deviceInfo.model)
    device.put("deviceId", deviceInfo.deviceId)
    device.put("ip", deviceInfo.ip)
    

    val configLib : HashMap[String, Object] = new HashMap[String, Object]
    configLib.put("appName", "app")
    configLib.put("appVersion", "app-1.0.0")
    configLib.put("device", device)
    /*configLib.put("putFn", new TvMetrixEventSink() {
      def put(action:Object , data:String ) {
      }
    })*/
    configLib.put("timeFn", new TvMetrixTimeProvider() {
      var now:Long = System.currentTimeMillis()
      def getCurrentTime() : Long = {
        var t:Long = now
        this.now = (this.now + 5*60000);
        return t
      } 
    })

    return configLib
  }

  // <---- START SESSION ---->
  private def buildStart() :  String = {

    val random = scala.util.Random

    val subscriber : HashMap[String, Object] = new HashMap[String, Object]
    subscriber.put("subscriberId", "TEST888")
    subscriber.put("region", region)
    subscriber.put("zipcode", "28400")
    subscriber.put("usage","usagetest")

    val listBoostrap : List[String] = List("1.0.6-Mirada","1.0.10-Mirada","1.0.7","1.1.7-Rc","1.1.8")

    val sessionParams : HashMap[String, Object] = new HashMap[String, Object]
    sessionParams.put("language", "SPA")
    sessionParams.put("subscriber", subscriber)
    sessionParams.put("bootstrap",listBoostrap(random.nextInt(listBoostrap.length)))  

    val sessionAction : HashMap[String, Object] = new HashMap[String, Object]
    sessionAction.put("action", "new-session")
    sessionAction.put("params", sessionParams)

    var start = "" 
    try {
        start = client.log(sessionAction)
        println("ACTION START SESSION")
        println("")
      } catch {
        case e: Exception => e.printStackTrace
      } 

    return start
  }

  private def serialNumberGenerator(jsonData:String): String ={
    val randomGuid = util.Random.nextInt(1000000)

    jsonData.replace("${guid}", randomGuid.toString)
                   .replace("${sentAt}", DateTime.now().toString())
  }

  private def checkIsAuthorised(kinesisClient: AmazonKinesisClient) {
    try {
          kinesisClient.describeStream(kinesisStream)
    } catch {
      case awsEx : AmazonClientException =>
        println("\nCheck you are logged in to AWS \n")
        throw awsEx
      case ex : Exception => throw ex
    }
  }
}