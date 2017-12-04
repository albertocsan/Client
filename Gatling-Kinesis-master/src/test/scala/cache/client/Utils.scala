package cache.client

import java.util.HashMap
import tvmetrix.client.java._
import java.util.ArrayList
import scala.io.Source

import net.liftweb.json.DefaultFormats
import net.liftweb.json._

class Utils  {
	/*val random = scala.util.Random
		this.playposition = random.nextInt*/
	val indexDevice = 0 
	val indexProduct = 0 
	val indexLive = 0

	case class Device (
		`class`: String,
		platform: String,
		platformVersion: String,
		make: String,
		model: String,
		deviceId: String
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

	case class ProductContent (
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
	
	def getDevice() : Device = {

		implicit val formats = DefaultFormats
		val jsonDevices = parse(Source.fromFile("src/test/resources/devices/device.json").mkString)
		val device  = (jsonDevices\"devices")(indexDevice).extract[Device]

		return device
	}

	def getProduct(): ProductContent = {

		implicit val formats = DefaultFormats
		val jsonProduct = parse(Source.fromFile("src/test/resources/products/product.json").mkString)
		val product  = (jsonProduct\"products")(indexProduct).extract[ProductContent]
		
		return product
	}

	def getLive(): Live = {

		implicit val formats = DefaultFormats
		val jsonLive = parse(Source.fromFile("src/test/resources/live/live.json").mkString)
		val liveContent  = (jsonLive\"lives")(indexLive).extract[Live]
		
		return liveContent
	}
		
	
}