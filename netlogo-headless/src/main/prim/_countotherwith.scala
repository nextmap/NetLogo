// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.prim

import org.nlogo.agent.AgentSet
import org.nlogo.api.Dump
import org.nlogo.core.I18N
import org.nlogo.nvm.{ Context, Reporter }
import org.nlogo.nvm.RuntimePrimitiveException

class _countotherwith extends Reporter {

  override def report(context: Context): java.lang.Double =
    Double.box(
      report_1(context, argEvalAgentSet(context, 0), args(1)))

  def report_1(context: Context, sourceSet: AgentSet, reporterBlock: Reporter): Double = {
    val freshContext = new Context(context, sourceSet)
    var result = 0
    reporterBlock.checkAgentSetClass(sourceSet, context)
    val iter = sourceSet.iterator
    while(iter.hasNext) {
      val tester = iter.next()
      if (tester ne context.agent)
        freshContext.evaluateReporter(tester, reporterBlock) match {
          case b: java.lang.Boolean =>
            if (b.booleanValue)
              result += 1
          case x =>
            throw new RuntimePrimitiveException(
              context, this, I18N.errors.getN(
                "org.nlogo.prim.$common.expectedBooleanValue",
                displayName, Dump.logoObject(tester), Dump.logoObject(x)))
        }
    }
    result
  }

}
