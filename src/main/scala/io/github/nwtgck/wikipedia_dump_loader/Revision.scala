package io.github.nwtgck.wikipedia_dump_loader

import org.apache.commons.lang3.StringUtils

case class Revision(textOpt: Option[String]){
  override def toString: String = {
    s"""${productPrefix}(textOpt="${textOpt.map{text => StringUtils.abbreviate(text, 50).replace("\n", "")}}")"""
  }
}
