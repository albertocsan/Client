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


class Device(kinesisStream: String, sessionType: String, listActions: List[String]) extends Protocol {
  val utils =  new Utils()
  val deviceInfo = utils.getDevice()

  val kinesisClient = new AmazonKinesisClient()
  var region = Regions.US_EAST_1
  kinesisClient.setRegion(Region.getRegion(region))
  checkIsAuthorised(kinesisClient)
  val client  = TvMetrix.create(msgDeviceInfo())
  var index = 0 
  val vod = new VOD(client, listActions)
  val live = new LIVE(client, listActions)
   
  
  def execute() = {
    var sentToKinesis = ""

    if (index == 0) {
      sentToKinesis = buildStart()
      this.index = 1
    }

    if (sessionType == "VOD") {
      sentToKinesis = vod.executeNextAction(deviceInfo.resolution)
    }

    if (sessionType == "LIVE"){
      sentToKinesis = live.executeNextAction(deviceInfo.resolution)
    }

    
    //  <---- KINESIS ---->
    if (sentToKinesis != ""){
      println("sentToKinesis: " + sentToKinesis)
      val request = new PutRecordRequest()
      request.setStreamName(kinesisStream)
      val jsonPayload = serialNumberGenerator(sentToKinesis)
      request.setData(ByteBuffer.wrap(jsonPayload.getBytes()))
      request.setPartitionKey(util.Random.nextInt(10000).toString)
      kinesisClient.putRecord(request) 
      println("KINESIS SUBIDO")  
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

    val configLib : HashMap[String, Object] = new HashMap[String, Object]
    configLib.put("appName", "app")
    configLib.put("appVersion", "app-1.0.0")
    configLib.put("device", device)
    configLib.put("keepalive",new Integer(2))

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

    return configLib
  }

  // <---- START SESSION ---->
  private def buildStart() :  String = {

    val sessionParams : HashMap[String, Object] = new HashMap[String, Object]
    sessionParams.put("language", "SPA")

    val sessionAction : HashMap[String, Object] = new HashMap[String, Object]
    sessionAction.put("action", "new-session")
    sessionAction.put("params", sessionParams)

    var start = client.log(sessionAction)
    println("return de libreria : "+ start )
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