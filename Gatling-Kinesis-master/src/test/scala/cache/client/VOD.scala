package cache.client

import cache.params._
//Libreria
import tvmetrix.client.java._
import java.util.HashMap
import org.json._


class VOD(deviceInfo : HashMap[String, Object]) extends ISession{

	val utils =  new Utils()
 	var index = 0
 	val client  = TvMetrix.create(deviceInfo)

	def executeISession(): String ={
		println("INDEX: " +index)
		println("this:"+this+", index:"+index)
		val VODAction = utils.getVODAction(index)
 		this.index +=1
		var libreria = client.log(VODAction)
		println ("libreria " + libreria)
		return libreria
	}

}