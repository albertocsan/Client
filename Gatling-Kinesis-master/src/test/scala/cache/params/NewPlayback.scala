package cache.params

import java.util.HashMap
import tvmetrix.client.java._

class NewPlayback {

	/*
	<---- LIVE ---->
	val channel : HashMap[String, Object] = new HashMap[String, Object]
	channel.put("serviceId", "97007")
	channel.put("channelName", "Canal de las Estrellas")
	channel.put("channelNumber", "116")

	val delivery : HashMap[String, Object] = new HashMap[String, Object]
	delivery.put("deliveryContext", "TV")
	delivery.put("serviceId", "97007")

	val newPlaybackParams : HashMap[String, Object] = new HashMap[String, Object]
	newPlaybackParams.put("channel", channel)
	newPlaybackParams.put("delivery", delivery)
	newPlaybackParams.put("playtime", "2017-09-01T09:44:14.012Z")
	*/
	val content : HashMap[String, Object] = new HashMap[String, Object]
	content.put("contentId", "STND2374203619006837")
	//content.put("genres", ARRAY)
	content.put("parentalRating", "13")
	content.put("title", "George De La Selva")

	val product : HashMap[String, Object] = new HashMap[String, Object]
	product.put("price", "0")
	product.put("productId", "PROD9860312645007022")
	product.put("productName", "HD")
	product.put("system", "CMS")
	product.put("commercialType", "SVOD")

	val options : HashMap[String, Object] = new HashMap[String, Object]
	options.put("videoFormat", "HD")
	options.put("audioFormat", "stereo")
	options.put("audioMode", "decode")

	val delivery : HashMap[String, Object] = new HashMap[String, Object]
	delivery.put("deliveryId", "DLVY9860312487005679")
	//delivery.put("audioLanguages", ARRAY)
	//delivery.put("audioFormats", ARRAY)
	delivery.put("deliveryContext", "VOD")

	val subscription : HashMap[String, Object] = new HashMap[String, Object]
	subscription.put("subscriptionPackageId", "1297969")
	subscription.put("subscriptionServiceId", "173")
	subscription.put("subscriptionServiceName", "urn:tve:hbo")
	subscription.put("subscriptionPackageName", "SVOD - Full")

	val newPlaybackParams : HashMap[String, Object] = new HashMap[String, Object]
	newPlaybackParams.put("content", content)
	newPlaybackParams.put("product", product)
	newPlaybackParams.put("options", options)
	newPlaybackParams.put("delivery", delivery)
	newPlaybackParams.put("subscription", subscription)
	newPlaybackParams.put("playposition", "0")
	newPlaybackParams.put("pageName", "Kids Home|George De La Selva")
	newPlaybackParams.put("appSection", "appSection")

	val newPlayback : HashMap[String, Object] = new HashMap[String, Object]
	newPlayback.put("action", "new-playback")
	newPlayback.put("params", newPlaybackParams)

	def getNewPlayback() : HashMap[String, Object] ={
		println("newPlayback " + newPlayback)
		return newPlayback
	}
}