package com.github.nkmrs.sbtRoutesYml

import java.io.PrintWriter
import java.util

import org.yaml.snakeyaml.Yaml
import sbt.Keys._
import sbt.{ AutoPlugin, _ }

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

object SbtRoutesYmlPlugin extends AutoPlugin {
  override lazy val projectSettings = Seq(commands += routesYml)

  lazy val routesYml = Command.command("routesYml") { (state: State) =>
    val routesYml = Source.fromFile("conf/routes.yml").getLines().mkString("\n")
    val routesText = GenerateRoutes.fromYml(routesYml)
    val f = new PrintWriter("conf/routes")
    f.write(routesText)
    f.close()
    state
  }
}

object GenerateRoutes {

  val yaml = new Yaml()

  def fromYml(src: String): String = {

    val data = yaml.load(src)

    val maxUriLength = toMap(data).flatMap { kv =>
      toMap(kv._2).map { kv =>
        val uri = kv._1
        uri.length
      }
    }.max
    val uriLength = (maxUriLength / 4 + 1) * 4

    toMap(data).flatMap { kv =>
      val controller = kv._1

      val controllerGroup = toMap(kv._2).flatMap { kv =>
        val uri = kv._1

        toMap(kv._2).map { kv =>
          val method = kv._1

          val params = kv._2 match {
            case null => ""
            case _ =>
              val s = toMap(kv._2).map { kv =>
                val param = kv._1
                val value = kv._2
                value match {
                  case null => param
                  case _ => "%s: %s".format(param, value)
                }
              }
              "(%s)".format(s.mkString(", "))
          }
          "%s %s.%s%s".format(uri, controller, method, params)
        }
      }
      // Insert blank row between controllers
      controllerGroup ++ ArrayBuffer("")
    }.map(formatRoute(_, uriLength))
      .mkString("\n")
  }

  def formatRoute(str: String, uriLength: Int): String = {
    val arr = str.split(" ")
    arr.length match {
      case 0 | 1 | 2 =>
        ""
      case _ =>
        val s1 = arr(0).padTo(4, " ").mkString
        val s2 = arr(1).padTo(uriLength, " ").mkString
        val s3 = arr.tail.tail.mkString(" ")
        "%s %s %s".format(s1, s2, s3)
    }
  }

  def toMap(data: AnyRef): util.LinkedHashMap[String, AnyRef] = {
    data match {
      case null => new util.LinkedHashMap[String, AnyRef]()
      case _ => data.asInstanceOf[util.LinkedHashMap[String, AnyRef]]
    }
  }
}
