// Consider the following snippet of code. Does it compile? If not, how would you fix it?

class A {
  def test(implicit n: Int): String = n.toString
}

object A {
  implicit val n: Int = 2
}

(new A()).test

// ANSWER

// The compiler fails in finding an implicit value for the parameter n:

// <console>:13: error: could not find implicit value for parameter n: Int
//  (new A()).test
//   ^
//  First, it searches in the current scope: this is the code accessible from (new A()).test and
// from def test(implicit n: Int): String. It fails to find a good match, so it looks at the
// companion object of Int, which has no accessible implicit value of type IntThe compiler stops
// the implicit resolution, and it fails the compilation. You need to include the implicit value
// A.n into the current scope using an import to fix the compilation error:

  import A._
  (new A()).test

  // alternatively…

//  class A {
//  import A._
//  def test(implicit n: Int): String = n.toString
//  }
