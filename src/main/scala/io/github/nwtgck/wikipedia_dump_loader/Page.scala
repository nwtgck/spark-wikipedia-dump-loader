package io.github.nwtgck.wikipedia_dump_loader

case class Page(title: String, revision: Revision, redirectOpt: Option[Redirect])

