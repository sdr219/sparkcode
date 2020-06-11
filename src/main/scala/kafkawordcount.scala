import org.apache.spark._
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.kafka.KafkaUtils

object kafkawordcount {
 def main(args: Array[String]): Unit = {

  val conf = new SparkConf().setMaster("local[*]").setAppName("kafkawordcount")
  val ssc = new StreamingContext(conf,Seconds(10))
  val kafkaStream = KafkaUtils.createStream(ssc, "localhost:2181","spark-streaming-consumer-group", Map("sales" -> 5))
  //need to change the topic name and the port number accordingly
  val words = kafkaStream.flatMap(x =>  x._2.split(" "))
  val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)
  kafkaStream.print()  //prints the stream of data received
  wordCounts.print()   //prints the wordcount result of the stream
  ssc.start()
  ssc.awaitTermination()

 }






}
