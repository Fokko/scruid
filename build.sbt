/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
lazy val unusedWarnings = Seq("-Ywarn-unused-import", "-Ywarn-unused")

lazy val commonSettings: Seq[Setting[_]] = Seq(
  bintrayOrganization := Some("ing-bank"),
  bintrayRepository := "maven-releases",
  bintrayPackage := "scruid",
  organization in ThisBuild := "ing.wbaa.druid",
  homepage in ThisBuild := Some(url(s"https://github.com/${bintrayOrganization.value.get}/${name.value}/#readme")),
  licenses in ThisBuild := Seq(("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))),
  description in ThisBuild := "Scala library for composing Druid queries",
  bintrayReleaseOnPublish in ThisBuild := false,
  developers in ThisBuild := List(
    Developer("fokko", "Fokko Driesprong", "@fokkodriesprong", url("https://github.com/fokko")),
    Developer("bjgbeelen", "Bas Beelen", "", url("https://github.com/bjgbeelen"))
  ),
  scmInfo in ThisBuild := Some(
    ScmInfo(
      url(s"https://github.com/${bintrayOrganization.value.get}/${name.value}"),
      s"git@github.com:${bintrayOrganization.value.get}/${name.value}.git"
    )
  ),
  crossScalaVersions in ThisBuild := Seq("2.11.8", "2.12.3"),
  scalaVersion in ThisBuild := "2.12.3",
  scalafmtOnCompile in ThisBuild := true,
  scalacOptions ++= Seq(Opts.compile.deprecation, "-Xlint", "-feature"),
  scalacOptions ++= PartialFunction.condOpt(CrossVersion.partialVersion(scalaVersion.value)) {
    case Some((2, v)) if v >= 11 => unusedWarnings
  }.toList.flatten,
  publishArtifact in Test := false
) ++ Seq(Compile, Test).flatMap(c =>
  scalacOptions in (c, console) --= unusedWarnings
)


lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(
    name := "scruid",
    version := "0.1.0.1",
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.1",

      "org.json4s" %% "json4s-jackson" % "3.5.2",
      "org.json4s" %% "json4s-ext" % "3.5.2",

      "com.typesafe.akka" %% "akka-http" % "10.0.9",

      "ch.qos.logback" % "logback-classic" % "1.1.3",

      "org.scalactic" %% "scalactic" % "3.0.1",
      "org.scalatest" %% "scalatest" % "3.0.1" % "test"
    ),
    resolvers += Resolver.sonatypeRepo("releases")
  )
