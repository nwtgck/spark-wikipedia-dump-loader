# wikipedia-dump-loader
[![Build Status](https://travis-ci.org/nwtgck/wikipedia-dump-loader-scala.svg?branch=develop)](https://travis-ci.org/nwtgck/wikipedia-dump-loader-scala)

A Loader for [Wikipedia Dump](https://dumps.wikimedia.org/) written in Scala


## How to import `wikipedia-dump-loader`

Add the following to your `build.sbt`.

```scala
// Add dependency of `wikipedia-dump-loader` in GitHub
// (from: https://github.com/sbt/sbt/issues/3489)
dependsOn(RootProject(uri("git://github.com/nwtgck/wikipedia-dump-loader-scala.git#6c63b83782f1e3249c001235d5e6057b98ecad5e")))
```

## Example Usage

Here is a complete code to use `wikipedia-dump-loader`.

```scala
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

    // Create classLoader for loading resources/
    val classLoader = this.getClass.getClassLoader

    // Create spark session
    val sparkSession: SparkSession = SparkSession
      .builder()
      .appName("Spark XML Practice [Spark session]")
      .getOrCreate()

    // Create page Dataset
    val pageDs: Dataset[Page] = WikipediaDumpLoader.readXmlFilePath(
      sparkSession,
      filePath = classLoader.getResource("wikidump.xml").getPath
    )

    // Print all pages
    for(page <- pageDs){
      println(page)
    }
  }
}

```

`wikidump.xml` above is found in [HERE](https://raw.githubusercontent.com/nwtgck/wikipedia-dump-loader-example-scala/master/src/main/resources/wikidump.xml).


## Example repository

[nwtgck/wikipedia-dump-loader-example-scala](https://github.com/nwtgck/wikipedia-dump-loader-example-scala)