package cache.client

import java.nio.ByteBuffer
import com.amazonaws.AmazonClientException
import com.amazonaws.regions.Region
import com.amazonaws.services.kinesis.AmazonKinesisClient
import com.amazonaws.services.kinesis.model.{PutRecordRequest}
import io.gatling.core.protocol._
import cache.infrastructure.{EventConfig, EventFileLoader}
import org.joda.time.DateTime


import scala.collection.JavaConverters._

class VODPlayback(config : EventConfig,kinesisStream: String) extends Protocol {
  val kinesisClient = new AmazonKinesisClient()
  //val sessions : Map = new Map<Integer, PlaybackSession>();

  kinesisClient.setRegion(Region.getRegion(config.region))
  checkIsAuthorised(kinesisClient)
  
  /*def execute(data_blob_count : Int, user_id : Long) = {
    PlaybackSession playback_session;
    if (sessions.get(user_id)) {
        playback_session = sessions.get(user_id);
    } else {
        playback_session = new PlaybackSession(kinesisStream);
        sessions.put(user_id, playback_session);
    }
    playback_session.execute();
  }*/

  def execute(data_blob_count : Int, user_id : Long) = {
    val request = new PutRecordRequest()
    request.setStreamName(kinesisStream)
    val jsonPayload = serialNumberGenerator(getBaseEventJson)
    request.setData(ByteBuffer.wrap(jsonPayload.getBytes()))
    request.setPartitionKey(util.Random.nextInt(10000).toString)
    kinesisClient.putRecord(request)
  }

  private def serialNumberGenerator(jsonData:String): String ={
    val randomGuid = util.Random.nextInt(1000000)

    jsonData.replace("${guid}", randomGuid.toString)
                   .replace("${sentAt}", DateTime.now().toString())
  }

  private def getBaseEventJson: String = {
    EventFileLoader.getEventJson(config.eventType)
  }

  private def checkIsAuthorised(kinesisClient: AmazonKinesisClient) {
    try {
          kinesisClient.describeStream("tvmetrix")//Deberia ser automatica y no ponerla a pelo
    } catch {
      case awsEx : AmazonClientException =>
        println("\nCheck you are logged in to AWS using ADFS (aws-adfs login --profile <ProfileType> --adfs-host <hostName> --region <some-region>)\n")
        throw awsEx
      case ex : Exception => throw ex
    }
  }
}