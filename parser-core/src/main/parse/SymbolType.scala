// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.parse

import scala.collection.immutable.HashMap

sealed trait SymbolType

object SymbolType {
  type SymbolTable = HashMap[String, SymbolType]
  trait Variable
  case object PrimitiveCommand extends SymbolType
  case object PrimitiveReporter extends SymbolType
  case object GlobalVariable extends SymbolType with Variable
  case object LocalVariable extends SymbolType with Variable
  case object TurtleBreed extends SymbolType
  case object TurtleBreedSingular extends SymbolType
  case object LinkBreed extends SymbolType
  case object LinkBreedSingular extends SymbolType
  case object PatchVariable extends SymbolType with Variable
  case object TurtleVariable extends SymbolType with Variable
  case object LinkVariable extends SymbolType with Variable
  case object BreedCommand extends SymbolType
  case object BreedReporter extends SymbolType
  case object ProcedureSymbol extends SymbolType
  case class BreedVariable(breedName: String) extends SymbolType with Variable
  case class LinkBreedVariable(breedName: String) extends SymbolType with Variable

  def typeName(symbolType: SymbolType): String =
    symbolType match {
      case GlobalVariable       => "global variable"
      case TurtleVariable       => "turtle variable"
      case PatchVariable        => "patch variable"
      case LinkVariable         => "link variable"
      case TurtleBreed          => "breed"
      case TurtleBreedSingular  => "singular breed name"
      case LinkBreed            => "link breed"
      case LinkBreedSingular    => "singular link breed name"
      case BreedVariable(n)     => s"$n-OWN variable"
      case LinkBreedVariable(n) => s"$n-OWN variable"
      case ProcedureSymbol      => "procedure"
      case PrimitiveCommand     => "primitive command"
      case PrimitiveReporter    => "primitive reporter"
      case BreedCommand         => "breed command"
      case BreedReporter        => "breed reporter"
      case LocalVariable        => "local variable here"
    }

  implicit class RichHashMap(l: HashMap[String, SymbolType]) {
    def addSymbols(symbols: Iterable[String], tpe: SymbolType): SymbolTable = {
      l ++ symbols.map(_.toUpperCase -> tpe).toMap
    }
  }

  implicit object SymbolTypeOrdering extends Ordering[SymbolType] {
    // defines which symbols should be authoritative in saying "there is already an..."
    def relativeWeight(s: SymbolType): Int =
      s match {
        case PrimitiveCommand     => 0
        case PrimitiveReporter    => 1
        case BreedCommand         => 2
        case BreedReporter        => 3
        case (TurtleBreed | TurtleBreedSingular) => 4
        case (LinkBreed | LinkBreedSingular) => 4
        case GlobalVariable       => 5
        case TurtleVariable       => 6
        case PatchVariable        => 6
        case LinkVariable         => 6
        case BreedVariable(n)     => 7
        case LinkBreedVariable(n) => 7
        case ProcedureSymbol      => 9
        case LocalVariable        => 10
      }

    override def compare(s1: SymbolType, s2: SymbolType): Int = {
      if (s1 == s2)
        0
      else
        implicitly[Ordering[Int]].compare(relativeWeight(s1), relativeWeight(s2))
    }
  }

  def emptySymbolTable = new HashMap[String, SymbolType]()
}