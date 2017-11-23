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


class Device(kinesisStream: String) extends Protocol {

  val utils =  new Utils()
  val deviceInfo = utils.generateDevice()
  val kinesisClient = new AmazonKinesisClient()
  var region = Regions.US_EAST_1
  val vod = new VOD(deviceInfo)

  kinesisClient.setRegion(Region.getRegion(region))
  checkIsAuthorised(kinesisClient)

  def execute() = {

    val sentToKinesis = vod.executeISession()
    println ("sentToKinesis " +sentToKinesis)

    //  <---- KINESIS ---->
    val request = new PutRecordRequest()
    request.setStreamName(kinesisStream)
    val jsonPayload = serialNumberGenerator(sentToKinesis)
    request.setData(ByteBuffer.wrap(jsonPayload.getBytes()))
    request.setPartitionKey(util.Random.nextInt(10000).toString)
    kinesisClient.putRecord(request)

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
        println("\nCheck you are logged in to AWS using ADFS (aws-adfs login --profile <ProfileType> --adfs-host <hostName> --region <some-region>)\n")
        throw awsEx
      case ex : Exception => throw ex
    }
  }
}