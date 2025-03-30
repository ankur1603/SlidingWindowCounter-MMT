package org.example

import java.time.Instant
import scala.collection.mutable
import scala.util.Try

/**
 * A Sliding Window Counter that keeps track of the frequency of numbers within a given time window.
 * It supports inserting new numbers and querying the N-th most frequent numbers within the window.
 *
 * @param windowSize The size of the sliding window in seconds.
 */
case class SlidingWindowCounter(windowSize: Long) {
  private val queue = mutable.Queue[(Int, Long)]() // Stores (number, timestamp)
  private val state = mutable.HashMap[Int, Int]() // Stores (number -> frequency)
  private var orderedByFrequency: Seq[(Int, Iterable[Int])] = Seq.empty // Stores sorted frequency map

  /**
   * Inserts a new number with its timestamp into the sliding window.
   * Also removes expired entries and updates frequency counts.
   *
   * @param number The number to insert.
   * @param timestampEpoch The epoch timestamp associated with the number.
   */
  def insert(number: Int, timestampEpoch: Long): Unit = {
    val streamItem = (number, timestampEpoch)
    queue.enqueue(streamItem)
    state(number) = state.getOrElse(number, 0) + 1 // Increment frequency count

    // Remove expired elements (outside the sliding window)
    val dequeuedItems = queue.dequeueAll(timestampEpoch - _._2 > windowSize)
    dequeuedItems.foreach(item => state(item._1) = state.getOrElse(item._1, 0) - 1) // Update frequency counts

    // Reorder state by frequency in descending order
    orderedByFrequency = state
      .dropWhile(_._2 <=0)
      .groupBy(_._2) // Group by frequency
      .map(item => (item._1, item._2.keys)) // Convert to (frequency, set of numbers)
      .toSeq
      .sortBy(_._1)(Ordering.Int.reverse) // Sort by frequency in descending order
  }

  /**
   * Queries the N-th most frequent numbers within the sliding window.
   *
   * @param nth The rank of frequency to fetch.
   * @return Iterable of numbers with the N-th highest frequency.
   */
  def query(nth: Int): Iterable[Int] = {
    if (nth - 1 < orderedByFrequency.length) orderedByFrequency(nth - 1)._2
    else Iterable.empty // Return empty if nth is out of bounds
  }

  /**
   * Processes input commands: inserting numbers, querying top-N elements, or exiting.
   *
   * @param input The user input (a number, "query N", or "exit").
   * @param currentTimestampEpoch The current epoch timestamp (defaults to system time).
   */
  def performTask(input: String, currentTimestampEpoch: Long = Instant.now.getEpochSecond): Unit = {
    input match {
      case "exit" =>
        println("Exiting program.")
        System.exit(0)
      case q if q.startsWith("query") =>
        val nth = Try(q.split(" ")(1).toInt).getOrElse(1)
        println(s"Top Nth($nth) item(s) by frequencies in last $windowSize seconds: ${query(nth).mkString(",")}")
      case num if num.matches("\\d+") =>
        insert(num.toInt, currentTimestampEpoch)
        println(s"Inserted: $num")
      case _ => println("Invalid input. Enter a number or a query.")
    }
  }
}