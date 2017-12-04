/*package cache.client

import java.util.HashMap
import tvmetrix.client.java._

import com.fasterxml.jackson.core.JsonGenerationException
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper

class UtilsOLD  {
	val mapper = new ObjectMapper()
	

	def getDevice() :HashMap[String, Object] = {

		//val devices : List[String]

		val jsonDevice : String = "{\"appName\": \"app\",\"appVersion\": \"app-1.0.0\",\"device\":{\"class\":\"MOBILE\",\"platform\":\"Android\",\"platformVersion\":\"6.0\",\"make\":\"Google, Inc.\",\"model\":\"Nexus 6\",\"deviceId\":\"somedeviceid\"}}"
		println("jsonDevice "+ jsonDevice)
		var device : HashMap[String, Object] = new HashMap[String, Object]
		device = mapper.readValue(jsonDevice, new TypeReference[HashMap[String,Object]](){})

		device.put("timeFn", new TvMetrixTimeProvider() {
			def getCurrentTime() : Long = {
				var now:Long = System.currentTimeMillis() 
				return now
			} 
		})
		device.put("putFn", new TvMetrixEventSink() {
			def put(action:Object , data:String ) {
			}
		})

		println("device "+ device)
		return device
	}
		
	def getSession() : HashMap[String, Object] ={
		// Create a hashmap representing a new-session action
		val jsonSession : String = "{\"action\" : \"new-session\", \"params\" : {\"language\":\"SPA\"}}"
		var session : HashMap[String, Object] = new HashMap[String, Object]
		session = mapper.readValue(jsonSession, new TypeReference[HashMap[String,Object]](){})
		println("sessionAction " + session)
		return session
	}

	def getPlayback() : HashMap[String,Object] = {

		val jsonPlayback : String = "{\"action\": \"new-playback\", \"params\": { \"content\": {\"contentId\": \"STND2374203619006837\", \"genres\": [\"Infantil\"], \"parentalRating\": \"13\", \"title\": \"George De La Selva\"},\"product\": {\"price\": 0, \"productId\": \"PROD9860312645007022\", \"productName\": \"HD\", \"system\": \"CMS\", \"commercialType\": \"SVOD\"},\"options\": {\"videoFormat\": \"HD\", \"audioFormat\": \"stereo\", \"audioMode\": \"decode\"}, \"playposition\": 666,\"delivery\": {\"deliveryId\": \"DLVY9860312487005679\", \"audioLanguages\": [\"QAA\", \"SPA\"], \"audioFormats\": [\"stereo\"],\"deliveryContext\": \"VOD\"},\"subscription\": {\"subscriptionPackageId\": \"1297969\", \"subscriptionServiceId\": \"173\", \"subscriptionServiceName\": \"urn:tve:hbo\", \"subscriptionPackageName\": \"SVOD - Full\"},\"pageName\": \"Kids Home|George De La Selva\", \"appSection\": \"catalogue\" }}"
		var playback : HashMap[String, Object] = new HashMap[String, Object]
		playback = mapper.readValue(jsonPlayback, new TypeReference[HashMap[String,Object]](){})
		var params = playback.get("params").asInstanceOf[HashMap[String, Object]]
		var playpos = params.get("playposition")
		params.put("playposition", new Integer(6789))
		println("playpos is:"+playpos)
		println("action:"+playback.get("action"))
		println("test class."+(new Integer(3)).getClass)
		var ptype = playpos.getClass.getName
		println("instancia de:"+ ptype)
		println("newPlayback "+playback)		
		return playback

	}
	def getUpdatePlayback() : HashMap[String,Object] = {

		val jsonUpdatePlayback : String = "{\"action\":\"update-playback\",\"params\":{\"playtime\":\"2017-09-01T19:44:14.012Z\"}}"
		var updatePlayback : HashMap[String, Object] = new HashMap[String, Object]
		updatePlayback = mapper.readValue(jsonUpdatePlayback, new TypeReference[HashMap[String,Object]](){})

		println("updatePlayback "+updatePlayback)		
		return updatePlayback
	}

	def getVODAction(index:Int): HashMap[String, Object] = {
		val listActions : List[HashMap[String, Object]] = List(getSession(),getPlayback())

		return listActions(index)
	}
}
package cache.client

import java.util.HashMap
import tvmetrix.client.java._
import java.util.ArrayList
import scala.io.Source
//Quitar
import com.fasterxml.jackson.core.JsonGenerationException
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper

import scala.collection.mutable.ArrayBuffer
//<--- Hasta aqui --->
import net.liftweb.json.DefaultFormats
import net.liftweb.json._

class Utils  {
	val indexDevice = 0 //demomento no se muy bien para que
	val indexProduct = 0 //demomento no se muy bien para que

	val mapper = new ObjectMapper()
	
	def getDevice() : HashMap[String, Object] = {

		/*implicit val formats = DefaultFormats
		val json = parse(Source.fromFile("src/test/resources/devices/device.json").mkString)

		case class Device (
			`class`: String,
			platform: String,
			platformVersion: String,
			make: String,
			model: String,
			deviceId: String
		)
		case class Config (
			appName: String,
			appVersion: String,
			device: Device
		)
		
		val d = (json\"devices")(indexDevice).extract[Config]
		println("MMMM " + d.device.`class`)*/
		
		//ESTO FUNCIONA
		/*Source.fromFile("src/test/resources/devices/device.json").getLines.foreach { x => mapDevices += x }
		var device : HashMap[String, Object] = new HashMap[String, Object]
		device = mapper.readValue(mapDevices(indexDevice), new TypeReference[HashMap[String,Object]](){})*/
		//HASTA AQUI

		/*val device : HashMap[String, Object] = new HashMap[String, Object]
		device.put("class", d.device.`class`)
		device.put("platform", d.device.platform)
		device.put("platformVersion", d.device.platformVersion)
		device.put("make", d.device.make)
		device.put("model", d.device.model)
		device.put("deviceId", d.device.deviceId)  

		val configLib : HashMap[String, Object] = new HashMap[String, Object]
		configLib.put("appName", d.appName)
		configLib.put("appVersion", d.appVersion)
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

		println("device "+ configLib)
		return configLib

	}

	def getProduct(): HashMap[String, Object] = {

		val mapProducts = ArrayBuffer[String]()
		Source.fromFile("src/test/resources/products/product.txt").getLines.foreach { x => mapProducts += x }
		//println("product 1: "+ mapProducts(indexProduct))

		var product : HashMap[String, Object] = new HashMap[String, Object]
		product = mapper.readValue(mapProducts(indexProduct), new TypeReference[HashMap[String,Object]](){})

		println("product "+ product)
		return product

		
	}
		
	
}
*/

/*val device : HashMap[String, Object] = new HashMap[String, Object]
		device.put("class", "MOBILE")
		device.put("platform", "Android")
		device.put("platformVersion", "6.0")
		device.put("make", "Google, Inc.")
		device.put("model", "Nexus 6")
		device.put("deviceId", "somedeviceid")  

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

		println("DEVICE: "+ configLib)
		return configLib*/


		/*var genres = new ArrayList[String]()
		genres.add("Infantil")

		var audioLanguages = new ArrayList[String]()
		audioLanguages.add("QAA")
		audioLanguages.add("SPA")

		var audioFormats = new ArrayList[String]()
		audioFormats.add("stereo")

		val content : HashMap[String, Object] = new HashMap[String, Object]
		content.put("contentId", "STND2374203619006837")
		content.put("genres", genres )
		content.put("parentalRating", "13")
		content.put("title", "George De La Selva")

		val productParams : HashMap[String, Object] = new HashMap[String, Object]
		productParams.put("price", new Integer(10))
		productParams.put("productId", "PROD9860312645007022")
		productParams.put("productName", "HD")
		productParams.put("system", "CMS")
		productParams.put("commercialType", "SVOD")

		val options : HashMap[String, Object] = new HashMap[String, Object]
		options.put("videoFormat", "HD")
		options.put("audioFormat", "stereo")
		options.put("audioMode", "decode")

		val delivery : HashMap[String, Object] = new HashMap[String, Object]
		delivery.put("deliveryId", "DLVY9860312487005679")
		delivery.put("audioLanguages", audioLanguages)
		delivery.put("audioFormats",  audioFormats)
		delivery.put("deliveryContext", "VOD")

		val subscription : HashMap[String, Object] = new HashMap[String, Object]
		subscription.put("subscriptionPackageId", "1297969")
		subscription.put("subscriptionServiceId", "173")
		subscription.put("subscriptionServiceName", "urn:tve:hbo")
		subscription.put("subscriptionPackageName", "SVOD - Full")

		val product : HashMap[String, Object] = new HashMap[String, Object]
		product.put("content", content)
		product.put("product", productParams)
		product.put("options", options)
		product.put("delivery", delivery)
		product.put("subscription", subscription)
		product.put("playposition", new Integer(50))
		product.put("pageName", "Kids Home|George De La Selva")
		product.put("appSection", "catalogue")

		println("PRODUCT: "+ product)
		return product*/
		*/