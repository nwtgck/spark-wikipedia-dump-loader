import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

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


      df.foreach{row =>
        println(row.get(0).getClass, row.get(1).getClass, row.get(3).getClass, row.get(4).getClass)
        val genericRow = row.get(3).asInstanceOf[GenericRowWithSchema]
        println(genericRow.schema)
        // => StructType(StructField(comment,StringType,true), StructField(contributor,StructType(StructField(id,LongType,true), StructField(ip,StringType,true), StructField(username,StringType,true)),true), StructField(format,StringType,true), StructField(id,LongType,true), StructField(minor,StringType,true), StructField(model,StringType,true), StructField(parentid,LongType,true), StructField(sha1,StringType,true), StructField(text,StructType(StructField(_VALUE,StringType,true), StructField(_space,StringType,true)),true), StructField(timestamp,StringType,true))

        // NOTE: This is content of article
        println(genericRow.getAs[String]("text"))
      }
    }

  }
}
