package com.github.nkmrs.sbtRoutesYml

import java.io.File

import org.apache.commons.io.FileUtils
import org.scalatest.{ FlatSpec, Matchers }

class GenerateRoutesTest extends FlatSpec with Matchers {

  def getResourceFile(file: String): String = {
    val path = getClass.getResource(file).getPath
    val f = new File(path)
    FileUtils.readFileToString(f, "UTF-8")
  }

  "routes.ymlのテキスト変換" should "準備していたファイルと一致すること" in {
    val yml = getResourceFile("/routes.yml")
    val actual = GenerateRoutes.fromYml(yml)
    val expected = getResourceFile("/routes")
    actual should be(expected)
  }
}
