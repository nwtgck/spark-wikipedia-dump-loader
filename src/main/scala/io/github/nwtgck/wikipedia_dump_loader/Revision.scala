package io.github.nwtgck.wikipedia_dump_loader

import org.apache.commons.lang3.StringUtils

case class Revision(text: String){
  override def toString: String = {
    s"""${productPrefix}(text="${StringUtils.abbreviate(text, 50).replace("\n", "")}")"""
  }
}
