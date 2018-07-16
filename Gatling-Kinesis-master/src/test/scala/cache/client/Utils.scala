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
		resolution: List[Integer],
		ip: String
	)

	case class Content (
		contentId: String,
		//genres: List[String],
		title: String,
		duration: Integer,
		parentalRating: String
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
		audioLanguages: List[String],
		//audioFormats: List[String],
		deliveryContext: String,
		provider: String
	)

	case class DeliveryLive (
		//deliveryId: String,
		//audioLanguages: List[String],
		//audioFormats: List[String],
		deliveryContext: String,
		serviceId: String
		//provider: String
	)

	case class DeliveryDVR (
		deliveryId: String,
		//audioLanguages: List[String],
		//audioFormats: List[String],
		deliveryContext: String,
		serviceId: String,
		provider: String
	)

	case class VOD (
		content: Content,
		product: Product,
		delivery: DeliveryVod,
		pageName: String
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

	case class DVR (
		content: Content,
		channel: Channel,
		delivery: DeliveryDVR
	)

	case class Track(
		`type`: String,
		coding: String,
		resolution: String,
		language: String
	)

	case class Profile(
		bitrate: Integer,
		resolution: String,
		frameRate: Integer
	)

	def getDevice() : Device = {

		implicit val formats = DefaultFormats
		val jsonDevices = parse(Source.fromFile("src/test/resources/device.json").mkString)
		var countDevices =  ((jsonDevices\"devices").children).size
		var indexDevice = random.nextInt(countDevices)
    val device  = (jsonDevices\"devices")(indexDevice).extract[Device]
		return device
	}

	def getVod(): VOD = {

		implicit val formats = DefaultFormats
		val jsonVod = parse(Source.fromFile("src/test/resources/vod.json").mkString)
		var countProducts =  ((jsonVod\"products").children).size
		var indexProduct = random.nextInt(countProducts)
		val vodContent  = (jsonVod\"products")(indexProduct).extract[VOD]
		
		return vodContent
	}

	def getLive(): Live = {

		implicit val formats = DefaultFormats
		val jsonLive = parse(Source.fromFile("src/test/resources/live.json").mkString)
		var countLives =  ((jsonLive\"lives").children).size
		var indexLive = random.nextInt(countLives)
		val liveContent  = (jsonLive\"lives")(indexLive).extract[Live]
		
		return liveContent
	}

	def getDVR(): DVR = {

		implicit val formats = DefaultFormats
		val jsonDVR = parse(Source.fromFile("src/test/resources/DVR.json").mkString)
		var countDVR =  ((jsonDVR\"DVR").children).size
		var indexDVR = random.nextInt(countDVR)
		val DVRContent  = (jsonDVR\"DVR")(indexDVR).extract[DVR]
		
		return DVRContent
	}

	/*def getTrack(): String = {
		
		val jsonTrack = Source.fromFile("src/test/resources/track.json").mkString
		println("TRACKS " +jsonTrack )	
		return jsonTrack
	}*/

	def getProfile(): Profile = {

		implicit val formats = DefaultFormats
		val jsonProfile = parse(Source.fromFile("src/test/resources/profile.json").mkString)
		var countProfiles =  ((jsonProfile\"profiles").children).size
		var indexProfile = random.nextInt(countProfiles)
		val profileContent  = (jsonProfile\"profiles")(indexProfile).extract[Profile]
		
		return profileContent
	}
	
		
	
}