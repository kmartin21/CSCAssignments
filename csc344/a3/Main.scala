

object Main {

  //booleans used to check if binding is number or not
var xbool = true
  var ybool = true
  var zbool = true
  var abool = true

  //bindings filled out by user
  var bindingX = 0
  var bindingY = 0
  var bindingZ = 0
  var bindingA = 0

  abstract class Tree

  //All cases overridden by toString
  case class Times(l: Tree, r: Tree) extends Tree {
    override def toString = "(" + l + "*" + r + ")"
  }

  case class Plus(l: Tree, r: Tree) extends Tree {
    override def toString = "(" + l + "+" + r + ")"
  }

  case class Minus(l: Tree, r: Tree) extends Tree {
    override def toString = "(" + l + "-" + r + ")"
  }

  case class Divide(l: Tree, r: Tree) extends Tree {
    override def toString = "(" + l + "/" + r + ")"
  }

  case class Var(n: String) extends Tree {
    override def toString = n
  }

  case class Const(v: Int) extends Tree {
    override def toString = v.toString
  }


  //simplify method used to simplify expression after bindings are created
  def simplify(t: Tree, recursive : Boolean = true): Tree = {
    t match {

      case Divide(Const(0), r)        => Const(0)
      case Divide(l, Const(1))        => simplify(l)
      case Divide(l, r) if l == r     => Const(1)
      case Divide(Const(l), Const(r)) => Const(l / r)
      case Divide(l, r) if recursive  => simplify(Divide(simplify(l), simplify(r)), recursive = false)

      case Minus(l, Const(0))         => simplify(l)
      case Minus(l, r) if l == r      => Const(0)
      case Minus(Const(l), Const(r))  => Const(l - r)
      case Minus(l, r) if recursive   => simplify(Minus(simplify(l), simplify(r)), recursive = false)

      case Times(Const(1), r)         => simplify(r)
      case Times(l, Const(1))         => simplify(l)
      case Times(Const(0), r)         => Const(0)
      case Times(l, Const(0))         => Const(0)
      case Times(Const(l), Const(r))  => Const(l * r)
      case Times(l, r) if recursive   => simplify(Times(simplify(l), simplify(r)), recursive = false)

      case Plus(Const(0), r)           => simplify(r)
      case Plus(l, Const(0))           => simplify(l)
      case Plus(Const(l), Const(r))    => Const(l + r)
      case Plus(l, r) if recursive     => simplify(Plus(simplify(l), simplify(r)), recursive = false)

      case _                          => t
    }
  }


  def replace(t: Tree): Tree = t match {
    case Times(l, r) => Times(replace(l), replace(r))
    case Plus(l, r) => Plus(replace(l), replace(r))
    case Minus(l, r) => Minus(replace(l), replace(r))
    case Divide(l, r) => Divide(replace(l), replace(r))

    case Var(n) if (n == "x") && (xbool == true) => Const(bindingX)
    case Var(n) if (n == "y") && (ybool == true) => Const(bindingY)
    case Var(n) if (n == "z") && (zbool == true) => Const(bindingZ)
    case Var(n) if (n == "a") && (abool == true) => Const(bindingA)

    case _                             => t
  }

  def main(args: Array[String]) {
   val expOne: Tree = Plus(Var("x"), (Times(Var("x"), (Minus(Var("y"), (Divide(Var("z"), Const(2))))))))
   val expTwo: Tree = Plus(Minus(Var("z"), Const(2)), (Times(Var("x"), (Const(5)))))
   val expThree: Tree = Plus(Const(1), Var("a"))

    println("Expression one simplified: " + expOne)
    println("Expression two simplified: " + expTwo)
    println("Expression three simplified: " + expThree)
    println("Type either 'expOne', 'expTwo', or 'expThree' depending on which simplified expression you would like to use. Otherwise, type 'quit' to exit the program.")

    var line = readLine()

    while (!line.equalsIgnoreCase("quit": String)) {
      xbool = true
      ybool = true
      zbool = true
      abool = true
      bindingX = 0
      bindingY = 0
      bindingZ = 0
      bindingA = 0

        if (line.equalsIgnoreCase("expOne": String)) {
        println("Enter your binding for 'x' or type nothing to keep it as 'x' ")

        try {
          bindingX = readInt()
        } catch {
          case e: Exception => xbool = false
        }

        println("Enter your binding for 'y' or type nothing to keep it as 'y' ")
        try {
          bindingY = readInt()
        } catch {
          case e: Exception => ybool = false
        }

        println("Enter your binding for 'z' or type nothing to keep it as 'z' ")

        try {
          bindingZ = readInt()
        } catch {
          case e: Exception => zbool = false
        }

        println("Original expression: " + expOne.toString)
        println("replaced: " + replace(expOne).toString)
        println("Expression one simplified with your bindings: " + simplify(replace(expOne), true).toString().stripPrefix("(").stripSuffix(")").trim())

        } else if (line.equalsIgnoreCase("expTwo": String)) {
          println("Enter your binding for 'x' or type nothing to keep it as 'x' ")

          try {
            bindingX = readInt()
          } catch {
            case e: Exception => xbool = false
          }

          println("Enter your number binding for 'z' or type nothing to keep it as 'z' ")

          try {
          bindingZ = readInt()
          } catch {
            case e: Exception => zbool = false
          }

          println("Original expression: " + expTwo.toString)
          println("replaced: " + replace(expTwo).toString)
          println("Expression two simplified with your bindings: " + simplify(replace(expTwo)).toString().stripPrefix("(").stripSuffix(")").trim())

        } else if (line.equalsIgnoreCase("expThree": String)) {
          println("Enter your binding for 'a' or type nothing to keep it as 'a'")
          try {
            bindingA = readInt()
          } catch {
            case e: Exception => abool = false
          }

          println("Original expression: " + expThree.toString)
          println("replaced: " + replace(expThree).toString)
          println("Expression three simplified with your bindings: " + simplify(replace(expThree)).toString().stripPrefix("(").stripSuffix(")").trim())
         } else {
          println("Expression not found try again")
        }

        println("Type either 'expOne', 'expTwo', or 'expThree' depending on which simplified expression you would like to use. Otherwise, type 'quit' to exit the program.")
        line = readLine()
      }
    }
  }


