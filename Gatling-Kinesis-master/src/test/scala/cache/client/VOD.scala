package cache.client

//Libreria
import tvmetrix.client.java._
import java.util.HashMap
import org.json._

import java.util.ArrayList

import scala.collection.JavaConversions._

class VOD(client : TvMetrixClient, listActions: List[String]) extends ISession{

	val utils =  new Utils()
 	var indexAction : Int = 0
 	var playposition : Int = 0
 	val vodContent  = utils.getVod()
 	val trackContent = utils.getTrack()
 	val profileContent = utils.getProfile()
 	val random = scala.util.Random
 	
	def executeNextAction(resolution : List[Integer]): String ={
		val action = listActions(indexAction)
		println ("ACTION " + action)
		println("")

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
 			case _        			=>  jsonToKinesis = ""
 		}
		return jsonToKinesis
	}

	def buildPlay(resolution :java.util.List[Integer]) :  String = {

		/*var genres = new ArrayList[String]()		
		for (i <- 0 until (vodContent.content.genres).length){
			genres.add(vodContent.content.genres(i))
		}*/

		/*var audioLanguages = new ArrayList[String]()
		for (i <- 0 until (vodContent.delivery.audioLanguages).length){
			audioLanguages.add(vodContent.delivery.audioLanguages(i))
		}

		var audioFormats = new ArrayList[String]()
		for (i <- 0 until (vodContent.delivery.audioFormats).length){
			audioFormats.add(vodContent.delivery.audioFormats(i))
		}*/

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
		if (trackContent.`type`=="video"){
			tracks.put("resolution", trackContent.resolution)
		}else{
			tracks.put("language", trackContent.language)
		}
		
		var availableBitrates = new ArrayList[Int]()
		availableBitrates.add(random.nextInt(1000))
		availableBitrates.add(random.nextInt(1000))

		val profile : HashMap[String, Object] = new HashMap[String, Object]
		profile.put("bitrate", new Integer (profileContent.bitrate))
		profile.put("resolution", resolution)
		profile.put("frameRate", new Integer (profileContent.frameRate))

		val bandwidth : HashMap[String, Object] = new HashMap[String, Object]
		bandwidth.put("bandwidth", new Integer(random.nextInt(1000)))

		val streaming : HashMap[String, Object] = new HashMap[String, Object]
		streaming.put("availableBitrates", availableBitrates)
		streaming.put("currentProfile", profile)
		streaming.put("currentBandwidth",bandwidth)

		val vst : HashMap[String, Object] = new HashMap[String, Object]
		vst.put("totalTime", new Integer (random.nextInt(1000)))
		vst.put("ottProvisionTime", new Integer (random.nextInt(1000)))
		vst.put("deeplinkTime", new Integer (random.nextInt(1000)))
		vst.put("drmSetupTime", new Integer (random.nextInt(1000)))
		vst.put("authoringTime", new Integer (random.nextInt(1000)))

		val params : HashMap[String, Object] = new HashMap[String, Object]
		params.put("content", content)
		params.put("product", productParams)
		//params.put("options", options)
		params.put("delivery", delivery)

		//OPERACIONAL
		params.put("tracks", tracks)
		params.put("streaming", streaming)
		params.put("vst", vst)

		//params.put("subscription", subscription)
		params.put("playposition", new Integer(generatePlayposition()))
		params.put("pageName", vodContent.pageName)
		//params.put("appSection", "catalogue")
		
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
		val updateParams : HashMap[String, Object] = new HashMap[String, Object]
    	updateParams.put("playposition", new Integer(generatePlayposition()))

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

	def buildUpdateCodec() = {
		/*var renderedResolution = new ArrayList[String]()		
		for (i <- 0 until (vodContent.content.genres).length){
			renderedResolution.add(vodContent.content.genres(i))
		}*/

		val updateParams : HashMap[String, Object] = new HashMap[String, Object]
    	updateParams.put("renderedFrameRate", new Integer (random.nextInt(1000)))
    	//updateParams.put("renderedResolution", renderedResolution)
    	updateParams.put("renderedFrames", new Integer (random.nextInt(1000)))
    	updateParams.put("decodedFrames", new Integer (random.nextInt(1000)))
    	updateParams.put("droppedFrames", new Integer (random.nextInt(1000)))

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
    	updateParams.put("bitrate", new Integer (random.nextInt(1000)))
    	//updateParams.put("resolution", resolution)
    	updateParams.put("frameRate", new Integer (random.nextInt(1000)))

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
    	updateParams.put("bandwidth",new Integer (random.nextInt(1000)))

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
		val listConnectionType : List[String] = List("Eth","CM","Wifi","Mobile","Other")
    	updateParams.put("connectionType", listConnectionType(random.nextInt(listConnectionType.length)))

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
    	stopParams.put("playposition", new Integer(generatePlayposition()))

   		val stopPlayback : HashMap[String, Object] = new HashMap[String, Object]
    	stopPlayback.put("action", "stop-playback")
    	stopPlayback.put("params", stopParams)

    	var stop = client.log(stopPlayback)
    	println("return de libreria : "+ stop )

		return stop
	}

	private def generatePlayposition() : Int = {
		
		this.playposition = random.nextInt(100000)
		return playposition
	}

}