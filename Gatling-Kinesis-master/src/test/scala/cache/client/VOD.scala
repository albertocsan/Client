package cache.client

import cache.params._
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
 	val productContent  = utils.getProduct()
 	
	def executeNextAction(): String ={
		println("indexAction: " +indexAction)
		println("this:"+this+", index:"+indexAction)
		val action = listActions(indexAction)
		println ("action " + action)

		this.indexAction +=1
 		var jsonToKinesis = ""
 		action match {
 			case "PLAY"   =>  jsonToKinesis = buildPlay()
 		    case "UPDATE" =>  jsonToKinesis = buildUpdate()
 		   	case "STOP"	  =>  jsonToKinesis = buildStop()
 			case _        =>  jsonToKinesis = "ERROR"
 		}
		return jsonToKinesis
	}

	def buildPlay() :  String = {

		/*var genres = new ArrayList[String]()		
		for (i <- 0 until (productContent.content.genres).length){
			genres.add(productContent.content.genres(i))
		}*/

		/*var audioLanguages = new ArrayList[String]()
		for (i <- 0 until (productContent.delivery.audioLanguages).length){
			audioLanguages.add(productContent.delivery.audioLanguages(i))
		}

		var audioFormats = new ArrayList[String]()
		for (i <- 0 until (productContent.delivery.audioFormats).length){
			audioFormats.add(productContent.delivery.audioFormats(i))
		}*/

		val content : HashMap[String, Object] = new HashMap[String, Object]
		content.put("contentId", productContent.content.contentId)
		//content.put("genres",genres)
		//content.put("parentalRating", productContent.content.parentalRating)
		content.put("title", productContent.content.title)

		val productParams : HashMap[String, Object] = new HashMap[String, Object]
		productParams.put("price",new Integer (productContent.product.price))
		productParams.put("productId", productContent.product.productId)
		productParams.put("productName", productContent.product.productName)
		productParams.put("system", productContent.product.system)
		productParams.put("commercialType", productContent.product.commercialType)

		/*val options : HashMap[String, Object] = new HashMap[String, Object]
		options.put("videoFormat", "HD")
		options.put("audioFormat", "stereo")
		options.put("audioMode", "decode")*/

		val delivery : HashMap[String, Object] = new HashMap[String, Object]
		/*delivery.put("deliveryId", productContent.delivery.deliveryId)
		delivery.put("audioLanguages",audioLanguages)
		delivery.put("audioFormats",audioFormats)*/
		delivery.put("deliveryContext", productContent.delivery.deliveryContext)

		/*val subscription : HashMap[String, Object] = new HashMap[String, Object]
		subscription.put("subscriptionPackageId", "1297969")
		subscription.put("subscriptionServiceId", "173")
		subscription.put("subscriptionServiceName", "urn:tve:hbo")
		subscription.put("subscriptionPackageName", "SVOD - Full")*/

		val params : HashMap[String, Object] = new HashMap[String, Object]
		params.put("content", content)
		params.put("product", productParams)
		//params.put("options", options)
		params.put("delivery", delivery)
		//params.put("subscription", subscription)
		params.put("playposition", new Integer(generatePlayposition()))
		params.put("pageName", "Kids Home|George De La Selva")
		//params.put("appSection", "catalogue")
		
		val playback : HashMap[String, Object] = new HashMap[String, Object]
		playback.put("action", "new-playback")
		playback.put("params", params)

		var play = client.log(playback)
		println("return from lib: "+ play )

		return play
	}

	def buildUpdate() :  String = {
		val updateParams : HashMap[String, Object] = new HashMap[String, Object]
    	updateParams.put("playposition", new Integer(generatePlayposition()))

   		val updatePlayback : HashMap[String, Object] = new HashMap[String, Object]
    	updatePlayback.put("action", "update-playback")
    	updatePlayback.put("params", updateParams)

    	var update = client.log(updatePlayback)
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
		this.playposition = random.nextInt
		println("RANDOM: " + playposition)
		return playposition
	}

}