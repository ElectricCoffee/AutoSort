name := "AutoSort"

version := "1.0"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq( "net.liftweb"       %% "lift-json"  % "2.5.1"
                           , "com.typesafe.akka" %% "akka-actor" % "2.3.3"
                           , "org.scalatest"     %% "scalatest"  % "2.2.0" % "test"
                           , "commons-io"        %  "commons-io" % "2.4"
                           )