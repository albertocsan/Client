package cache.client

import cache.params._
//Libreria
import tvmetrix.client.java._
import java.util.HashMap
import org.json._

import java.util.ArrayList
import java.time.Instant
class LIVE(client : TvMetrixClient) extends ISession{
	
	val utils =  new Utils()	
 	var indexAction = 0
 	val listActions : List[String] = List("PLAY","UPDATE") //leer de fichero de configuracion
 
 	val liveContent  = utils.getLive()

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


		val params : HashMap[String, Object] = new HashMap[String, Object]
		params.put("content", content)
		params.put("channel", channel)
		params.put("delivery", delivery)
		params.put("playtime", Instant.now().toString())
		
		val playback : HashMap[String, Object] = new HashMap[String, Object]
		playback.put("action", "new-playback")
		playback.put("params", params)
		println(playback)

		var play = client.log(playback)
		println("return from lib: "+ play )

		return play
	}

	def buildUpdate() :  String = {
		val updateParams : HashMap[String, Object] = new HashMap[String, Object]
    	updateParams.put("playtime",Instant.now().toString())

   		val updatePlayback : HashMap[String, Object] = new HashMap[String, Object]
    	updatePlayback.put("action", "update-playback")
    	updatePlayback.put("params", updateParams)

    	var update = client.log(updatePlayback)
    	println("return de libreria : "+ update )

		return update
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