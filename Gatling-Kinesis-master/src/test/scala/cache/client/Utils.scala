package cache.client

import java.util.HashMap
import tvmetrix.client.java._

class Utils{


	def generateDevice() :HashMap[String, Object] = {
		val device : HashMap[String, Object] = new HashMap[String, Object]
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
		return configLib
	}
	def getNewSession() : HashMap[String, Object] ={
		// Create a hashmap representing a new-session action
		val sessionParams : HashMap[String, Object] = new HashMap[String, Object]
		sessionParams.put("language", "SPA")

		val sessionAction : HashMap[String, Object] = new HashMap[String, Object]
		sessionAction.put("action", "new-session")
		sessionAction.put("params", sessionParams)
		//println("sessionAction " + sessionAction)
		return sessionAction
	}
	/*def getProduct()
	def getDelivery()*/
	def getVODAction(index:Int): HashMap[String, Object] = {
		val listActions : List[HashMap[String, Object]] = List(getNewSession(),getNewSession())

		return listActions(index)
	}
}