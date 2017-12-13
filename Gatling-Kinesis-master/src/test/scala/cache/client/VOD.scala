package cache.client

//Libreria
import tvmetrix.client.java._
import java.util.HashMap
import org.json._

import java.util.ArrayList

class VOD(client : TvMetrixClient) extends ISession{

	val utils =  new Utils()
 	var indexAction : Int = 0
 	val listActions : List[String] = List("PLAY","UPDATE") //leer de fichero de configuracion
 	var playposition : Int = 0
 	val vodContent  = utils.getVod()
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
	    	case "UPDATECODEC" 		=>  jsonToKinesis = buildUpdateCodec()
    		case "UPDATEPROFILE" 	=>  jsonToKinesis = buildUpdateProfile()
			case "UPDATEBANDWIDTH" 	=>  jsonToKinesis = buildUpdateBandwidth()
			case "UPDATECONNECTION" =>  jsonToKinesis = buildUpdateConnection()
 		   	case "STOP"	  			=>  jsonToKinesis = buildStop()
 			case _        			=>  jsonToKinesis = "ERROR"
 		}
		return jsonToKinesis
	}

	def buildPlay(resolution : List[Integer]) :  String = {

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
		

		val streamingQuality : HashMap[String, Object] = new HashMap[String, Object]
		streamingQuality.put("bufferLengthTime", new Integer (1))

		var availableBitrates = new ArrayList[Int]()
		availableBitrates.add(1)
		availableBitrates.add(2)

		val profile : HashMap[String, Object] = new HashMap[String, Object]
		profile.put("bitrate", new Integer (profileContent.bitrate))
		profile.put("resolution", resolution)
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
		//params.put("appSection", "catalogue")
		
		val playback : HashMap[String, Object] = new HashMap[String, Object]
		playback.put("action", "new-playback")
		playback.put("params", params)
		println("LLAMADA LIBRERIA")
		var play = client.log(playback)
		println("return from lib: "+ play )

		return play
	}

	def buildUpdatePlayback() :  String = {
		val updateParams : HashMap[String, Object] = new HashMap[String, Object]
    	updateParams.put("playposition", new Integer(generatePlayposition()))

   		val updatePlayback : HashMap[String, Object] = new HashMap[String, Object]
    	updatePlayback.put("action", "update-playback")
    	updatePlayback.put("params", updateParams)

    	var update = client.log(updatePlayback)
    	println("return de libreria : "+ update )

		return update
	}

	def buildUpdateCodec() :  String = {
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

    	var update = client.log(updateCodec)
    	println("return de libreria : "+ update )

		return update
	}

	def buildUpdateProfile() :  String = {
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

    	var update = client.log(updateProfile)
    	println("return de libreria : "+ update )

		return update
	}

	def buildUpdateBandwidth() :  String = {
		val updateParams : HashMap[String, Object] = new HashMap[String, Object]
    	updateParams.put("bandwidBandwidth",new Integer (1))

   		val updateBandwidth : HashMap[String, Object] = new HashMap[String, Object]
    	updateBandwidth.put("action", "update-bandwidth")
    	updateBandwidth.put("params", updateParams)

    	var update = client.log(updateBandwidth)
    	println("return de libreria : "+ update )

		return update
	}

	def buildUpdateConnection() :  String = {
		val updateParams : HashMap[String, Object] = new HashMap[String, Object]
    	updateParams.put("connectionType", "Eth")

   		val updateConnection : HashMap[String, Object] = new HashMap[String, Object]
    	updateConnection.put("action", "update-connection-type")
    	updateConnection.put("params", updateParams)

    	var update = client.log(updateConnection)
    	println("return de libreria : "+ update )

		return update
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
		
		val random = scala.util.Random
		this.playposition = random.nextInt(100000)
		println("RANDOM: " + playposition)
		return playposition
	}

}