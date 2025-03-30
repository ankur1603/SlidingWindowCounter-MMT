package org.example

import org.scalatest.{FunSuite, Matchers}

class SlidingWindowCounterTest extends FunSuite with Matchers {

  test("should return") {
    val app = SlidingWindowCounter(10)

    app.insert(1,1)
    app.insert(1,2)
    app.insert(2,3)
    assert(app.query(1).head == 1)
    assert(app.query(2).head == 2)
    assert(app.query(3).toList == List.empty)

    //Crossing the Window size
    app.insert(4,11)
    app.insert(5,12)
    app.insert(5,13)
    app.insert(5,14)
    app.insert(6,15)
    app.insert(6,16)

    assert(app.query(1).head == 5)
    assert(app.query(2).head == 6)

    app.insert(6,17)

    assert(app.query(1).toList == List(5,6))
    assert(app.query(2).head == 4)


  }

}
