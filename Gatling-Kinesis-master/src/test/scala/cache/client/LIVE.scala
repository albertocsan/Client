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
	var indexAction : Int = 0
 	
 	val liveContent  = utils.getLive()
 	//val trackContent = utils.getTrack()
 	val profileContent = utils.getProfile()
 	val random = scala.util.Random

	def executeNextAction(resolution : List[Integer]): String ={
		val action = listActions(indexAction)
		println ("ACTION " + action)
		println("")

		this.indexAction +=1
 		var jsonToKinesis = ""
 		action match {
			case "PLAY"   		     =>  jsonToKinesis = buildPlay()
			case "UPDATE"			 =>  jsonToKinesis = buildUpdatePlayback()
			case "UPDATEOPERATIONAL" =>  buildUpdateOperational()
			case "SEEK"			 =>  jsonToKinesis = buildSeek()
			case "FW"			 =>  jsonToKinesis = buildFW()
			case "RW"			 =>  jsonToKinesis = buildRW()
			case "PAUSE"			 =>  jsonToKinesis = buildPause()
			case "STOP"	  			 =>  jsonToKinesis = buildStop()
			case _        			 =>  jsonToKinesis = ""
 		}
		return jsonToKinesis
	}

	def buildPlay() :  String = {

		val content : HashMap[String, Object] = new HashMap[String, Object]
		content.put("contentId", liveContent.content.contentId)
		content.put("title", liveContent.content.title)
		content.put("duration", liveContent.content.duration)

		val channel : HashMap[String, Object] = new HashMap[String, Object]
		channel.put("serviceId", liveContent.channel.serviceId)
		channel.put("channelName", liveContent.channel.channelName)
		channel.put("channelNumber", liveContent.channel.channelNumber)

		val delivery : HashMap[String, Object] = new HashMap[String, Object]
		delivery.put("deliveryContext", liveContent.delivery.deliveryContext)
		delivery.put("serviceId", liveContent.delivery.serviceId)

		/*val tracks : HashMap[String, Object] = new HashMap[String, Object]
		tracks.put("type", trackContent.`type`)
		tracks.put("coding", trackContent.coding)
		if (trackContent.`type`=="video"){
			tracks.put("resolution", trackContent.resolution)
		}else{
			tracks.put("language", trackContent.language)
		}*/

		/*val streamingQuality : HashMap[String, Object] = new HashMap[String, Object]
		streamingQuality.put("bufferLengthTime", new Integer (random.nextInt(1000)))*/

		var availableBitrates = new ArrayList[Int]()
		availableBitrates.add(random.nextInt(1000))
		availableBitrates.add(random.nextInt(1000))

		val bandwidth : HashMap[String, Object] = new HashMap[String, Object]
		bandwidth.put("bandwidth", new Integer(random.nextInt(1000)))

		val streaming : HashMap[String, Object] = new HashMap[String, Object]
		streaming.put("availableBitrates", availableBitrates)

		val vst : HashMap[String, Object] = new HashMap[String, Object]
		vst.put("totalTime", new Integer (random.nextInt(1000)))
		vst.put("ottProvisionTime", new Integer (random.nextInt(1000)))
		vst.put("deeplinkTime", new Integer (random.nextInt(1000)))
		vst.put("drmSetupTime", new Integer (random.nextInt(1000)))
		vst.put("authoringTime", new Integer (random.nextInt(1000)))
		vst.put("probeTime", new Integer (random.nextInt(1000)))
		vst.put("serviceAreaTime", new Integer (random.nextInt(1000)))
		vst.put("setupRTSPTime", new Integer (random.nextInt(1000)))
		vst.put("describeRTSPTime", new Integer (random.nextInt(1000)))
		vst.put("playRTSPTime", new Integer (random.nextInt(1000)))
		vst.put("bookmarkTime", new Integer (random.nextInt(1000)))
		vst.put("ottGetTime", new Integer (random.nextInt(1000)))
		vst.put("geoIP", new Integer (random.nextInt(1000)))
		vst.put("concurrencyTime", new Integer (random.nextInt(1000)))

		val cableModulation : HashMap[String, Object] = new HashMap[String, Object]
		cableModulation.put("dvbTriplet", "12.23.34")
		cableModulation.put("frequency", new Integer (1000))
		cableModulation.put("modulation", new Integer (1000))
		cableModulation.put("symbolRate", new Integer (1000))
		cableModulation.put("fecInner", new Integer (1000))
		cableModulation.put("fecOuter", new Integer (1000))

		val error : HashMap[String, Object] = new HashMap[String, Object]
		error.put("errorCode", "z-1520")
		error.put("reason", "cancel")

		val operational : HashMap[String, Object] = new HashMap[String, Object]
		//operational.put("tracks", trackContent)
		operational.put("streaming", streaming)
		//operational.put("vst", vst)
		//operational.put("cableModulation", cableModulation)

		val params : HashMap[String, Object] = new HashMap[String, Object]
		params.put("content", content)
		params.put("channel", channel)
		params.put("delivery", delivery)
		params.put("playtime", Instant.now().toString())
		
		//params.put("reason", "cancel")
		//params.put("error", error)
		//OPERACIONAL
		params.put("operational", operational)
		
		val playback : HashMap[String, Object] = new HashMap[String, Object]
		playback.put("action", "new-playback")
		playback.put("params", params)


		var play = "" 
		try {
    		play = client.log(playback)
    	} catch {
    		case e: Exception => e.printStackTrace
  		}
		return play
	}

	def buildUpdatePlayback() :  String = {
		val device : HashMap[String, Object] = new HashMap[String, Object]
		device.put("ip", "192.168.1.111")
		val trickplay : HashMap[String, Object] = new HashMap[String, Object]
		trickplay.put("trickplayMode", "PLAY")

		val updateParams : HashMap[String, Object] = new HashMap[String, Object]
		updateParams.put("playtime",Instant.now().toString())
		updateParams.put("trickplay", trickplay)
		updateParams.put("device", device)

   		val updatePlayback : HashMap[String, Object] = new HashMap[String, Object]
    	updatePlayback.put("action", "update-playback")
    	updatePlayback.put("params", updateParams)    	

    	var update = "" 
		try {
    		update = client.log(updatePlayback)
    	} catch {
    		case e: Exception => e.printStackTrace
  		} 

		return update

	}

	def buildPause() :  String = {

		val trickplay : HashMap[String, Object] = new HashMap[String, Object]
		trickplay.put("trickplayMode", "PAUSE")

		val updateParams : HashMap[String, Object] = new HashMap[String, Object]
		updateParams.put("playtime",Instant.now().toString())
		updateParams.put("trickplay", trickplay)

		val updatePlayback : HashMap[String, Object] = new HashMap[String, Object]
		updatePlayback.put("action", "update-playback")
		updatePlayback.put("params", updateParams)

		var update = ""
		try {
			update = client.log(updatePlayback)
		} catch {
			case e: Exception => e.printStackTrace
		}

		return update
	}

	def buildRW() :  String = {

		val trickplay : HashMap[String, Object] = new HashMap[String, Object]
		trickplay.put("trickplayMode", "RW")

		val updateParams : HashMap[String, Object] = new HashMap[String, Object]
		updateParams.put("playtime",Instant.now().toString())
		updateParams.put("trickplay", trickplay)

		val updatePlayback : HashMap[String, Object] = new HashMap[String, Object]
		updatePlayback.put("action", "update-playback")
		updatePlayback.put("params", updateParams)

		var update = ""
		try {
			update = client.log(updatePlayback)
		} catch {
			case e: Exception => e.printStackTrace
		}

		return update
	}

	def buildFW() :  String = {

		val trickplay : HashMap[String, Object] = new HashMap[String, Object]
		trickplay.put("trickplayMode", "FW")

		val updateParams : HashMap[String, Object] = new HashMap[String, Object]
		updateParams.put("playtime",Instant.now().toString())
		updateParams.put("trickplay", trickplay)

		val updatePlayback : HashMap[String, Object] = new HashMap[String, Object]
		updatePlayback.put("action", "update-playback")
		updatePlayback.put("params", updateParams)

		var update = ""
		try {
			update = client.log(updatePlayback)
		} catch {
			case e: Exception => e.printStackTrace
		}

		return update
	}

	def buildSeek() :  String = {

		val trickplay : HashMap[String, Object] = new HashMap[String, Object]
		trickplay.put("trickplayMode", "SEEK")

		val updateParams : HashMap[String, Object] = new HashMap[String, Object]
		updateParams.put("playtime",Instant.now().toString())
		updateParams.put("trickplay", trickplay)

		val updatePlayback : HashMap[String, Object] = new HashMap[String, Object]
		updatePlayback.put("action", "update-playback")
		updatePlayback.put("params", updateParams)

		var update = ""
		try {
			update = client.log(updatePlayback)
		} catch {
			case e: Exception => e.printStackTrace
		}

		return update
	}

	def buildUpdateOperational() = {

		val codecQuality : HashMap[String,Object] = new HashMap[String,Object]
		codecQuality.put("renderedFrameRate", new Integer (random.nextInt(1000)))
    	codecQuality.put("renderedResolution", "1080p")
    	codecQuality.put("renderedFrames", new Integer (random.nextInt(1000)))
    	codecQuality.put("decodedFrames", new Integer (random.nextInt(1000)))
    	codecQuality.put("droppedFrames", new Integer (random.nextInt(1000)))

    	val modulationQuality : HashMap[String,Object] = new HashMap[String,Object]
    	modulationQuality.put("powerLevel", new Integer (random.nextInt(1000)))
    	modulationQuality.put("BER", new Integer (random.nextInt(1000)))
    	modulationQuality.put("SNR", new Integer (random.nextInt(1000)))

    	val streamingQuality : HashMap[String,Object] = new HashMap[String,Object]
		streamingQuality.put("minBufferLengthBytes", new Integer (random.nextInt(1000)))
		streamingQuality.put("timeUnderrun", new Integer (random.nextInt(1000)))


    	val profile : HashMap[String,Object] = new HashMap[String,Object]
    	profile.put("bitrate", new Integer (profileContent.bitrate))
    	profile.put("resolution", profileContent.resolution)
    	profile.put("frameRate", new Integer (profileContent.frameRate))

    	val bandwidth : HashMap[String,Object] = new HashMap[String,Object]
    	bandwidth.put("bandwidth",new Integer (random.nextInt(1000)))

    	val streaming : HashMap[String,Object] = new HashMap[String,Object]
    	streaming.put("profile",profile)
		streaming.put("bandwidth",bandwidth)

    	val listConnectionType : List[String] = List("Eth","CM","Wifi","Mobile","Other")

		val updateParams : HashMap[String, Object] = new HashMap[String, Object]
		updateParams.put("codecQuality",codecQuality)
		updateParams.put("modulationQuality",modulationQuality)
		updateParams.put("streamingQuality",streamingQuality)
		updateParams.put("streaming", streaming)
		updateParams.put("connectionType",listConnectionType(random.nextInt(listConnectionType.length)))	


   		val updateOperational : HashMap[String, Object] = new HashMap[String, Object]
    	updateOperational.put("action", "update-operational")
    	updateOperational.put("params", updateParams)
 	
    	try {
    		client.log(updateOperational)
    	} catch {
    		case e: Exception => e.printStackTrace
  		}
	}

	def buildStop() :  String = {
		val error : HashMap[String, Object] = new HashMap[String, Object]
		error.put("errorCode", "z-1520")
		error.put("reason", "cancel")

		val stopParams : HashMap[String, Object] = new HashMap[String, Object]
    	stopParams.put("playtime", Instant.now().toString())
    	//stopParams.put("error", error)

   		val stopPlayback : HashMap[String, Object] = new HashMap[String, Object]
    	stopPlayback.put("action", "stop-playback")
    	stopPlayback.put("params", stopParams)

    	var stop = ""
    	try {
    		stop = client.log(stopPlayback)
    	} catch {
    		case e: Exception => e.printStackTrace
  		}

		return stop	
	}

}