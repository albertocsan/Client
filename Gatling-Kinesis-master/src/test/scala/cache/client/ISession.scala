package cache.client

import java.util.HashMap

trait ISession {
	def executeNextAction(): String 
}