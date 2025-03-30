package org.example

import scala.io.StdIn
import scala.util.Try


object SlidingWindowApp extends App {

  println("Enter <windowSize> in seconds: ")
  private val windowSizeInput = StdIn.readLine()
  private val windowSize = Try(windowSizeInput.toInt).getOrElse({
    println(s"Invalid window size: $windowSizeInput, defaulting to 5 minutes (300 seconds)")
    300
  })

  println("Enter numbers (type 'query <top-Nth>' to get Nth highest frequency, 'exit' to stop):")
  val counter = SlidingWindowCounter(windowSize)
  while (true) {
    val input = StdIn.readLine()
    counter.performTask(input)
  }
}