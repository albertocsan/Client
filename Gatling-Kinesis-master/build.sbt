enablePlugins(GatlingPlugin)
scalaVersion := "2.11.8"
name := "ClientKinesis"

libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.2" % "test"
libraryDependencies += "io.gatling"            % "gatling-test-framework"    % "2.2.2" % "test"
libraryDependencies += "joda-time" % "joda-time" % "2.9.6"
libraryDependencies += "com.amazonaws"  % "aws-java-sdk"    % "1.11.220"
libraryDependencies += "net.liftweb" %% "lift-json" % "3.2.0-M3"


