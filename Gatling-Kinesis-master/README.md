# Client

ToDo List:
============



Requirement
=========================
1. SBT : Version 1.0.3
2. AWS_CLI 
3. SCALA: Version 2.11.8
4. Gatling Plugin SBT: Version 2.2.2


File Config
---------
AnalyticsClient.scala

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

Run all simulations
-------------------

```bash
> gatling:test
```

Run a single simulation
-----------------------

```bash
> gatling:testOnly AnalyticsClient 
```

