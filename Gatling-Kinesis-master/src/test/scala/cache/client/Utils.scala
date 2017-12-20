package cache.client

import java.util.HashMap
import tvmetrix.client.java._
import java.util.ArrayList
import scala.io.Source

import net.liftweb.json.DefaultFormats
import net.liftweb.json._

class Utils  {
	val random = scala.util.Random
		
	case class Device (
		`class`: String,
		platform: String,
		platformVersion: String,
		make: String,
		model: String,
		deviceId: String,
		resolution: List[Integer]
	)

	case class Content (
		contentId: String,
		//genres: List[String],
		//parentalRating: String,
		title: String
	)

	case class Product (
		price: Int,
		productId: String,
		productName: String,
		system: String,
		commercialType: String
	)

	case class DeliveryVod (
		//deliveryId: String,
		//audioLanguages: List[String],
		//audioFormats: List[String],
		deliveryContext: String
	)

	case class DeliveryLive (
		//deliveryId: String,
		//audioLanguages: List[String],
		//audioFormats: List[String],
		deliveryContext: String,
		serviceId: String
	)

	case class VOD (
		content: Content,
		product: Product,
		delivery: DeliveryVod
	)

	case class Channel (
		serviceId: String,
		channelName: String,
		channelNumber: String
	)

	case class Live (
		content: Content,
		channel: Channel,
		delivery: DeliveryLive
	)

	case class Track(
		`type`: String,
		coding: String,
		resolution: String,
		language: String
	)
	
	case class Profile(
		bitrate: Integer,
		resolution: List[Integer],
		frameRate: Integer
	)

	def getDevice() : Device = {

		implicit val formats = DefaultFormats
		var indexDevice = 0 
		indexDevice = random.nextInt(1)
		val jsonDevices = parse(Source.fromFile("src/test/resources/device.json").mkString)
		val device  = (jsonDevices\"devices")(indexDevice).extract[Device]
		//val cosa = (jsonDevices\"devices")
		//println("CLASEE: " +(cosa))
		//println("NUMERO DE Device: " + (jsonDevices\"devices"))
		return device
	}

	def getVod(): VOD = {

		implicit val formats = DefaultFormats
		var indexProduct = 0 
		indexProduct = random.nextInt(1)
		val jsonVod = parse(Source.fromFile("src/test/resources/vod.json").mkString)
		val vodContent  = (jsonVod\"products")(indexProduct).extract[VOD]
		
		return vodContent
	}

	def getLive(): Live = {

		implicit val formats = DefaultFormats
		var indexLive = 0 
		indexLive = random.nextInt(1)
		val jsonLive = parse(Source.fromFile("src/test/resources/live.json").mkString)
		val liveContent  = (jsonLive\"lives")(indexLive).extract[Live]
		
		return liveContent
	}

	def getTrack(): Track = {

		implicit val formats = DefaultFormats
		var indexTrack = 0 
		indexTrack = random.nextInt(1)
		val jsonTrack = parse(Source.fromFile("src/test/resources/track.json").mkString)
		val trackContent  = (jsonTrack\"tracks")(indexTrack).extract[Track]
		
		return trackContent
	}

	def getProfile(): Profile = {

		implicit val formats = DefaultFormats
		var indexProfile = 0 
		indexProfile = random.nextInt(1)
		val jsonProfile = parse(Source.fromFile("src/test/resources/profile.json").mkString)
		val profileContent  = (jsonProfile\"profiles")(indexProfile).extract[Profile]
		
		return profileContent
	}
	
		
	
}