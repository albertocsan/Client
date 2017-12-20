package cache.client

//Libreria
import tvmetrix.client.java._
import java.util.HashMap
import org.json._

import java.util.ArrayList
import java.time.Instant

import scala.collection.JavaConversions._
class LIVE(client : TvMetrixClient, listActions: List[String]) extends ISession{
	
	val utils =  new Utils()	
 	var indexAction = 0
 	
 	val liveContent  = utils.getLive()
 	val trackContent = utils.getTrack()
 	val profileContent = utils.getProfile()

	def executeNextAction(resolution : List[Integer]): String ={
		println("indexAction: " +indexAction)
		println("this:"+this+", index:"+indexAction)
		val action = listActions(indexAction)
		println ("action " + action)

		this.indexAction +=1
 		var jsonToKinesis = ""
 		action match {
 			case "PLAY"   		    =>  jsonToKinesis = buildPlay(resolution)
 		    case "UPDATE"			=>  jsonToKinesis = buildUpdatePlayback()
	    	case "UPDATECODEC" 		=>  buildUpdateCodec()
    		case "UPDATEPROFILE" 	=>  buildUpdateProfile()
			case "UPDATEBANDWIDTH" 	=>  buildUpdateBandwidth()
			case "UPDATECONNECTION" =>  buildUpdateConnection()
 		   	case "STOP"	  			=>  jsonToKinesis = buildStop()
 			case _        			=>  jsonToKinesis = "ERROR"
 		}
		return jsonToKinesis
	}

	def buildPlay(resolution:java.util.List[Integer]) :  String = {

		val content : HashMap[String, Object] = new HashMap[String, Object]
		content.put("contentId", liveContent.content.contentId)
		content.put("title", liveContent.content.title)

		val channel : HashMap[String, Object] = new HashMap[String, Object]
		channel.put("serviceId", liveContent.channel.serviceId)
		channel.put("channelName", liveContent.channel.channelName)
		channel.put("channelNumber", liveContent.channel.channelNumber)

		val delivery : HashMap[String, Object] = new HashMap[String, Object]
		delivery.put("deliveryContext", liveContent.delivery.deliveryContext)
		delivery.put("serviceId", liveContent.delivery.serviceId)

		val tracks : HashMap[String, Object] = new HashMap[String, Object]
		tracks.put("type", trackContent.`type`)
		tracks.put("coding", trackContent.coding)
		if (trackContent.`type`=="video"){
			tracks.put("resolution", trackContent.resolution)
		}else{
			tracks.put("language", trackContent.language)
		}

		val streamingQuality : HashMap[String, Object] = new HashMap[String, Object]
		streamingQuality.put("bufferLengthTime", new Integer (1))

		var availableBitrates = new ArrayList[Int]()
		availableBitrates.add(1)
		availableBitrates.add(2)

		val profile : HashMap[String, Object] = new HashMap[String, Object]
		profile.put("bitrate", new Integer (profileContent.bitrate))
		profile.put("resolution", resolution)
		profile.put("frameRate", new Integer (profileContent.frameRate))

		val bandwidth : HashMap[String, Object] = new HashMap[String, Object]
		bandwidth.put("bandwidth", new Integer(1))

		val streaming : HashMap[String, Object] = new HashMap[String, Object]
		streaming.put("availableBitrates", availableBitrates)
		streaming.put("currentProfile", profile)
		streaming.put("currentBandwidth",bandwidth)

		val vst : HashMap[String, Object] = new HashMap[String, Object]
		vst.put("totalTime", new Integer (1))
		vst.put("ottProvisionTime", new Integer (2))
		vst.put("deeplinkTime", new Integer (3))
		vst.put("drmSetupTime", new Integer (4))
		vst.put("authoringTime", new Integer (5))


		val params : HashMap[String, Object] = new HashMap[String, Object]
		params.put("content", content)
		params.put("channel", channel)
		params.put("delivery", delivery)
		params.put("playtime", Instant.now().toString())
		params.put("tracks", tracks)
		params.put("streaming", streaming)
		params.put("vst", vst)
		
		val playback : HashMap[String, Object] = new HashMap[String, Object]
		playback.put("action", "new-playback")
		playback.put("params", params)

		var play = "" 
		try {
    		play = client.log(playback)
    		println("return de libreria : "+ play )
    	} catch {
    		case e: Exception => e.printStackTrace
  		}
		return play
	}

	def buildUpdatePlayback() :  String = {
		val updateParams : HashMap[String, Object] = new HashMap[String, Object]
    	updateParams.put("playtime",Instant.now().toString())

   		val updatePlayback : HashMap[String, Object] = new HashMap[String, Object]
    	updatePlayback.put("action", "update-playback")
    	updatePlayback.put("params", updateParams)
		println("UPDATE: "+updatePlayback)
    	

    	var update = "" 
		try {
    		update = client.log(updatePlayback)
    		println("return de libreria : "+ update )
    	} catch {
    		case e: Exception => e.printStackTrace
  		} 

		return update

	}

	def buildUpdateCodec() = {
		/*var renderedResolution = new ArrayList[String]()		
		for (i <- 0 until (vodContent.content.genres).length){
			renderedResolution.add(vodContent.content.genres(i))
		}*/

		val updateParams : HashMap[String, Object] = new HashMap[String, Object]
    	updateParams.put("renderedFrameRate", new Integer (1))
    	//updateParams.put("renderedResolution", renderedResolution)
    	updateParams.put("renderedFrames", new Integer (1))
    	updateParams.put("decodedFrames", new Integer (1))
    	updateParams.put("droppedFrames", new Integer (1))

   		val updateCodec : HashMap[String, Object] = new HashMap[String, Object]
    	updateCodec.put("action", "update-codec-quality")
    	updateCodec.put("params", updateParams)

    	try {
    		client.log(updateCodec)
    	} catch {
    		case e: Exception => e.printStackTrace
  		}
	}

	def buildUpdateProfile() = {
		/*var resolution = new ArrayList[String]()		
		for (i <- 0 until (vodContent.content.genres).length){
			resolution.add(vodContent.content.genres(i))
		}*/
		val updateParams : HashMap[String, Object] = new HashMap[String, Object]
    	updateParams.put("bitrate", new Integer (1))
    	//updateParams.put("resolution", resolution)
    	updateParams.put("frameRate", new Integer (1))

   		val updateProfile : HashMap[String, Object] = new HashMap[String, Object]
    	updateProfile.put("action", "update-profile")
    	updateProfile.put("params", updateParams)

    	try {
    		client.log(updateProfile)
    	} catch {
    		case e: Exception => e.printStackTrace
  		}
	}

	def buildUpdateBandwidth()  = {
		val updateParams : HashMap[String, Object] = new HashMap[String, Object]
    	updateParams.put("bandwidth",new Integer (1))

   		val updateBandwidth : HashMap[String, Object] = new HashMap[String, Object]
    	updateBandwidth.put("action", "update-bandwidth")
    	updateBandwidth.put("params", updateParams)

		try {
    		client.log(updateBandwidth)
    	} catch {
    		case e: Exception => e.printStackTrace
  		}
    }

	def buildUpdateConnection()  = {
		val updateParams : HashMap[String, Object] = new HashMap[String, Object]
    	updateParams.put("connectionType", "Eth")

   		val updateConnection : HashMap[String, Object] = new HashMap[String, Object]
    	updateConnection.put("action", "update-connection-type")
    	updateConnection.put("params", updateParams)

    	try {
    		client.log(updateConnection)
    	} catch {
    		case e: Exception => e.printStackTrace
  		}
    }

	def buildStop() :  String = {
		val stopParams : HashMap[String, Object] = new HashMap[String, Object]
    	stopParams.put("playtime", Instant.now().toString())

   		val stopPlayback : HashMap[String, Object] = new HashMap[String, Object]
    	stopPlayback.put("action", "stop-playback")
    	stopPlayback.put("params", stopParams)

    	var stop = client.log(stopPlayback)
    	println("return de libreria : "+ stop )

		return stop
	}
}