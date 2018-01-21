import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}
import io.github.nwtgck.wikipedia_dump_loader.{Page, Revision, Redirect}

object Main {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName( "Spark Practice" )
      .setMaster("local[*]" )
      .set("spark.executor.memory", "1g")
    val sc   = new SparkContext( conf )

    val classLoader = this.getClass.getClassLoader

    // Load DataFrame from Wikipedia Dump XML
    {
      val sparkSession: SparkSession = SparkSession
        .builder()
        .appName("Spark XML Practice [Spark session]")
        .getOrCreate()

      val df = sparkSession.read
        .format("xml")
        .option("rowTag", "page")
        .load(classLoader.getResource("wikidump.xml").getPath)

      df.printSchema()
      df.show()

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
            val text       : String               = textRow.getAs("_VALUE") // NOTE: "_VALUE" is like innerText (<text> tag has attribute)
            // NOTE: revisionRow has other many fields! (they may be useful)
            Revision(text=text)
          }
          // Redirect information
          val redirectOpt: Option[Redirect] = for {
            redirectRow: GenericRowWithSchema <- Option(row.getAs[GenericRowWithSchema]("redirect"))
            title      : String               = redirectRow.getAs("_title") // NOTE: "_title" is an attribute in tag
          } yield Redirect(title=title)

          Page(
            title       = title,
            revision    = revision,
            redirectOpt =redirectOpt
          )
        }

      pageDs.foreach{page => println(page)}


//      df.foreach{row =>
//        val title   : String =
//          row.getAs("title")
//        val revisionRow: GenericRowWithSchema =
//          row.getAs("revision")
//        val redirectRowOpt: Option[GenericRowWithSchema] =
//          Option(row.getAs("redirect"))
//
//        redirectRowOpt match {
//          case Some(r) => println(r.schema)
//          case _ => ()
//        }
//
////        println(redirectRowOpt)
//
////        println(row)
////        println(row.get(0).getClass, row.get(1).getClass, row.get(3).getClass, row.get(4).getClass)
////        val genericRow = row.get(3).asInstanceOf[GenericRowWithSchema]
////        println(revisionRow.schema)
//
////        val text = revisionRow.getAs[GenericRowWithSchema]("text")
////        println(text.schema)
////        println(text.getAs[String]("_VALUE"))
//
////        // => StructType(StructField(comment,StringType,true), StructField(contributor,StructType(StructField(id,LongType,true), StructField(ip,StringType,true), StructField(username,StringType,true)),true), StructField(format,StringType,true), StructField(id,LongType,true), StructField(minor,StringType,true), StructField(model,StringType,true), StructField(parentid,LongType,true), StructField(sha1,StringType,true), StructField(text,StructType(StructField(_VALUE,StringType,true), StructField(_space,StringType,true)),true), StructField(timestamp,StringType,true))
////
////        // NOTE: This is content of article
//////        println(genericRow.getAs[String]("text"))
//      }

    }

  }
}
