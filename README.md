# spark-wikipedia-dump-loader
[![Build Status](https://travis-ci.org/nwtgck/wikipedia-dump-loader-scala.svg?branch=develop)](https://travis-ci.org/nwtgck/wikipedia-dump-loader-scala)

A [Wikipedia Dump](https://dumps.wikimedia.org/) Loader for Spark in Scala


## How to import `spark-wikipedia-dump-loader`

Add the following to your `build.sbt`.

```scala
// Add dependency of `spark-wikipedia-dump-loader` in GitHub
dependsOn(RootProject(uri("https://github.com/nwtgck/spark-wikipedia-dump-loader.git#e6e358dd8cdd5b6200b89f5d2aa76c74b5c1d0d7")))
```
(from: <https://github.com/sbt/sbt/issues/3489>)


## Example Usage

Here is a complete code to use `spark-wikipedia-dump-loader`.

```scala
package io.github.nwtgck.spark_wikipedia_dump_loader_example

import org.apache.spark.sql.{Dataset, SparkSession}
import io.github.nwtgck.spark_wikipedia_dump_loader.{Page, Redirect, Revision, WikipediaDumpLoader}

object Main {
  def main(args: Array[String]): Unit = {
    // Create spark session
    val sparkSession: SparkSession = SparkSession
      .builder()
      .appName("Wikipedia Dump Loader Test [Spark session]")
      .master("local[*]")
      .config("spark.executor.memory", "1g")
      .getOrCreate()

    // Create page Dataset
    val pageDs: Dataset[Page] = WikipediaDumpLoader.readXmlFilePath(
      sparkSession,
      filePath = "./wikidump.xml"
    )

    // Print all pages
    for (page <- pageDs) {
      println(page)
    }
  }
}
```

`wikidump.xml` above is found in [HERE](https://raw.githubusercontent.com/nwtgck/spark-wikipedia-dump-loader-example/master/wikidump.xml).


## Example Repositories

* [spark-wikipedia-dump-loader-example](https://github.com/nwtgck/spark-wikipedia-dump-loader-example)
* [wikipedia-word2vec-playground-spark](https://github.com/nwtgck/wikipedia-word2vec-playground-spark)