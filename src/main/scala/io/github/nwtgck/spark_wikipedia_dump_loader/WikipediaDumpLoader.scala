package io.github.nwtgck.spark_wikipedia_dump_loader

import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.{Dataset, SparkSession}

object WikipediaDumpLoader {

  def readXmlFilePath(sparkSession: SparkSession, filePath: String): Dataset[Page] = {
    val df = sparkSession.read
      .format("xml")
      .option("rowTag", "page")
      .load(filePath)
    import sparkSession.implicits._

    // Create page Dataset
    val pageDs: Dataset[Page] = for(row <- df)
      yield {
        // Get title of page
        val title   : String =
          row.getAs("title")
        // Get revision containing the content of page
        val revision: Revision = {
          val revisionRow: GenericRowWithSchema = row.getAs("revision")
          val textRow    : GenericRowWithSchema = revisionRow.getAs("text")
          val textOpt    : Option[String]       = Option(textRow.getAs[String]("_VALUE")) // NOTE: "_VALUE" is like innerText (<text> tag has attribute)
          // NOTE: revisionRow has other many fields! (they may be useful)
          Revision(textOpt=textOpt)
        }
        // Redirect information
        val redirectOpt: Option[Redirect] = for {
          redirectRow: GenericRowWithSchema <- Option(row.getAs[GenericRowWithSchema]("redirect"))
          title      : String               = redirectRow.getAs("_title") // NOTE: "_title" is an attribute in tag
        } yield Redirect(title=title)

        Page(
          title       = title,
          revision    = revision,
          redirectOpt = redirectOpt
        )
      }

    // Return pageDs
    pageDs
  }
}
