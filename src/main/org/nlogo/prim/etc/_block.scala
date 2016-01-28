package org.nlogo.prim.etc

import org.nlogo.api.{Syntax, Argument}
import org.nlogo.nvm.{Reporter, Pure, Context}
import scala.collection.JavaConverters._

class _block extends Reporter with Pure {
  override def syntax: Syntax = Syntax.reporterSyntax(Array(Syntax.CodeBlockType), Syntax.StringType)
  override def report(context: Context): AnyRef = argEvalCodeBlock(context, 0).asScala.map(_.name).mkString(" ")
}
