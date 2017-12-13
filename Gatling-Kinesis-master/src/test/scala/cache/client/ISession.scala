package cache.client

import java.util.HashMap

trait ISession {
	def executeNextAction(resolution : List[Integer]): String 
}