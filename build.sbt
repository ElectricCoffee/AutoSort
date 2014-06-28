name := "AutoSort"

version := "1.0"

//scalaVersion := "2.11.1" // 2.11.1 not supported with lift-json

libraryDependencies ++= Seq( "net.liftweb" %% "lift-json"  % "2.5.1"
                           , "commons-io"  %  "commons-io" % "2.4"
                           )