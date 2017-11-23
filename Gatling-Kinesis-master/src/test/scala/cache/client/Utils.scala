package cache.client

import java.util.HashMap
import tvmetrix.client.java._

import com.fasterxml.jackson.core.JsonGenerationException
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper

class Utils  {
	val mapper = new ObjectMapper()
	

	def generateDevice() :HashMap[String, Object] = {

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

		val jsonPlayback : String = "{\"action\": \"new-playback\", \"params\": { \"content\": {\"contentId\": \"STND2374203619006837\", \"genres\": [\"Infantil\"], \"parentalRating\": \"13\", \"title\": \"George De La Selva\"},\"product\": {\"price\": 0, \"productId\": \"PROD9860312645007022\", \"productName\": \"HD\", \"system\": \"CMS\", \"commercialType\": \"SVOD\"},\"options\": {\"videoFormat\": \"HD\", \"audioFormat\": \"stereo\", \"audioMode\": \"decode\"}, \"playposition\": 0,\"delivery\": {\"deliveryId\": \"DLVY9860312487005679\", \"audioLanguages\": [\"QAA\", \"SPA\"], \"audioFormats\": [\"stereo\"],\"deliveryContext\": \"VOD\"},\"subscription\": {\"subscriptionPackageId\": \"1297969\", \"subscriptionServiceId\": \"173\", \"subscriptionServiceName\": \"urn:tve:hbo\", \"subscriptionPackageName\": \"SVOD - Full\"},\"pageName\": \"Kids Home|George De La Selva\", \"appSection\": \"catalogue\" }}"
		var playback : HashMap[String, Object] = new HashMap[String, Object]
		playback = mapper.readValue(jsonPlayback, new TypeReference[HashMap[String,Object]](){})

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
		val listActions : List[HashMap[String, Object]] = List(getSession())

		return listActions(index)
	}
}