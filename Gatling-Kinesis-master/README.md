# Client

Requirement
=========================
1. SBT : Version 1.0.3 (Gatling-Kinesis-master/project/build.properties)
2. AWS_CLI (Gatling-Kinesis-master/build.sbt) // The credentials used will be those marked in the default profile
3. SCALA: Version 2.11.8 (Gatling-Kinesis-master/build.sbt)
4. Gatling Plugin SBT: Version 2.2.2 (Gatling-Kinesis-master/project/plugins.sbt)
5. tvmetrix-client-0.1.17-standalone.jar (Gatling-Kinesis-master/lib)
6. json-20171018.jar (Gatling-Kinesis-master/lib)


File Config
---------
AnalyticsClient.scala (Gatling-Kinesis-master/src/test/scala) 

	- sessionType: VOD or LIVE	
	- listActions: PLAY - UPDATE - UPDATECODEC - UPDATEPROFILE - UPDATEBANDWIDTH - UPDATECONNECTION - STOP	
	- users: Number of threads	
	- testScenario: Same number of  'exec(clientAction)' that length listActions/ value of pause same that keepalive
	- Kekeepaliveep: Seconds to ALIVE event

Start SBT
---------

```bash
$ sbt
```

Run a simulation
-----------------------

```bash
> gatling:testOnly AnalyticsClient 
```


