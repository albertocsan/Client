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


class Device(kinesisStream: String) extends Protocol {

  val kinesisClient = new AmazonKinesisClient()
  var region = Regions.US_EAST_1
  kinesisClient.setRegion(Region.getRegion(region))
  checkIsAuthorised(kinesisClient)
  val client  = TvMetrix.create(msgDeviceInfo())
  val listSession : List[String] = List("START","LIVE")//leer de fichero de configuracion
  var indexSession : Int = 0
  
  def execute() = {
    var sentToKinesis = ""
    var sessionType = listSession(indexSession)
    println("indexSession: " + indexSession)
    if (sessionType == "START") {
      sentToKinesis = buildStart()
    } else if (sessionType == "VOD") {
      val vod = new VOD(client)
      sentToKinesis = vod.executeNextAction()
    } else if (sessionType == "LIVE"){
      val live = new LIVE(client)
      sentToKinesis = live.executeNextAction()
    } else {
      sentToKinesis = "Session Error"
    }
      
    this.indexSession +=1

    println("sentToKinesis: " + sentToKinesis)
    //  <---- KINESIS ---->
    val request = new PutRecordRequest()
    request.setStreamName(kinesisStream)
    val jsonPayload = serialNumberGenerator(sentToKinesis)
    request.setData(ByteBuffer.wrap(jsonPayload.getBytes()))
    request.setPartitionKey(util.Random.nextInt(10000).toString)
    kinesisClient.putRecord(request) 
    println("KINESIS SUBIDO")   

  }

  //<---- GENERATE DEVICE ---->
  def msgDeviceInfo() : HashMap[String,Object] = {

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