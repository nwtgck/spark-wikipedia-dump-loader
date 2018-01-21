import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}
import io.github.nwtgck.wikipedia_dump_loader.{Page, Redirect, Revision, WikipediaDumpLoader}

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

      // Create page Dataset
      val pageDs: Dataset[Page] = WikipediaDumpLoader.readXmlFilePath(
        sparkSession,
        filePath = classLoader.getResource("wikidump.xml").getPath
      )

      pageDs.foreach{page => println(page)}

    }

  }
}
