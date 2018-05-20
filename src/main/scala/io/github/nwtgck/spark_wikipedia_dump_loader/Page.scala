package io.github.nwtgck.spark_wikipedia_dump_loader

case class Page(title: String, revision: Revision, redirectOpt: Option[Redirect])

