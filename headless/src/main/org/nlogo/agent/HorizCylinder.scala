// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.agent

import org.nlogo.api.{ AgentException, AgentKind }

// imagine a cylinder lying on its side

@annotation.strictfp
class HorizCylinder(_world: World)
extends Topology(_world, xWraps = false, yWraps = true) {

  @throws(classOf[AgentException])
  override def wrapX(x: Double): Double = {
    val max = world.maxPxcor + 0.5
    val min = world.minPxcor - 0.5
    if (x >= max || x < min)
      throw new AgentException("Cannot move turtle beyond the world's edge.")
    x
  }

  override def wrapY(y: Double): Double =
    Topology.wrap(y, world.minPycor - 0.5, world.maxPycor + 0.5)

  override def distanceWrap(dx: Double, dy: Double, x1: Double, y1: Double, x2: Double, y2: Double): Double = {
    val dy2 =
      if (y1 > y2)
        y2 + world.worldHeight - y1
      else
        y2 - world.worldHeight - y1
    val dyMin =
      if (StrictMath.abs(dy2) < StrictMath.abs(dy))
        dy2
      else
        dy
    world.rootsTable.gridRoot(dx * dx + dyMin * dyMin)
  }

  override def towardsWrap(headingX: Double, headingY: Double): Double = {
    val headingY2 = Topology.wrap(
      headingY,
      world.worldHeight / -2.0,
      world.worldHeight / 2.0)
    if (headingY == 0)
      if (headingX > 0) 90 else 270
    else if (headingX == 0)
      if (headingY > 0) 0 else 180
    else ((270 + StrictMath.toDegrees (StrictMath.PI + StrictMath.atan2(-headingY, headingX)))
      % 360)
  }

  override def shortestPathX(x1: Double, x2: Double) = x2

  override def shortestPathY(y1: Double, y2: Double) = {
    val yprime =
      if (y1 > y2)
        y2 + world.worldHeight
      else
        y2 - world.worldHeight
    if (StrictMath.abs(y2 - y1) > StrictMath.abs(yprime - y1))
      yprime
    else
      y2
  }

  override def followOffsetX = 0.0

  override def getNeighbors(source: Patch): AgentSet =
    if (source.pxcor == world.maxPxcor) {
      if (source.pxcor == world.minPxcor) {
        if (source.pycor == world.maxPycor && source.pycor == world.minPycor) {
          world.noPatches
        } else {
          AgentSet.fromArray(AgentKind.Patch,
                                   Array[Agent](getPatchNorth(source),
                                               getPatchSouth(source)))
        }
      } else {
        if (source.pycor == world.maxPycor && source.pycor == world.minPycor) {
          AgentSet.fromArray(AgentKind.Patch,
                                   Array[Agent](getPatchWest(source)))
        } else {
          AgentSet.fromArray(AgentKind.Patch,
              Array[Agent](getPatchNorth(source), getPatchSouth(source),
                          getPatchWest(source), getPatchSouthWest(source),
                          getPatchNorthWest(source)))
        }
      }
    } else if (source.pxcor == world.minPxcor) {
      if (source.pycor == world.maxPycor && source.pycor == world.minPycor) {
        AgentSet.fromArray(AgentKind.Patch,
                                 Array[Agent](getPatchEast(source)))
      } else {
        AgentSet.fromArray(AgentKind.Patch,
            Array[Agent](getPatchNorth(source), getPatchEast(source),
                getPatchSouth(source), getPatchNorthEast(source),
                        getPatchSouthEast(source)))
      }
    } else {
      if (source.pycor == world.maxPycor && source.pycor == world.minPycor) {
        AgentSet.fromArray(AgentKind.Patch,
                                 Array[Agent](getPatchEast(source),
                                             getPatchWest(source)))
      } else {
        AgentSet.fromArray(AgentKind.Patch,
            Array[Agent](getPatchNorth(source), getPatchEast(source),
                        getPatchSouth(source), getPatchWest(source),
                        getPatchNorthEast(source), getPatchSouthEast(source),
                        getPatchSouthWest(source), getPatchNorthWest(source)))
      }
    }

  override def getNeighbors4(source: Patch): AgentSet =
    if (source.pxcor == world.maxPxcor) {
      if (source.pxcor == world.minPxcor) {
        if (source.pycor == world.maxPycor && source.pycor == world.minPycor) {
          world.noPatches
        } else {
          AgentSet.fromArray(AgentKind.Patch,
              Array[Agent](getPatchNorth(source),
                          getPatchSouth(source)))
        }
      } else {
        if (source.pycor == world.maxPycor && source.pycor == world.minPycor) {
          AgentSet.fromArray(AgentKind.Patch,
                                   Array[Agent](getPatchWest(source)))
        } else {
          AgentSet.fromArray(AgentKind.Patch,
                                   Array[Agent](getPatchNorth(source),
                                               getPatchSouth(source),
                                               getPatchWest(source)))
        }
      }
    } else if (source.pxcor == world.minPxcor) {
      if (source.pycor == world.maxPycor && source.pycor == world.minPycor) {
        AgentSet.fromArray(AgentKind.Patch,
                                 Array[Agent](getPatchEast(source)))
      } else {
        AgentSet.fromArray(AgentKind.Patch,
                                 Array[Agent](getPatchNorth(source),
                                             getPatchEast(source),
                                             getPatchSouth(source)))
      }
    } else {
      if (source.pycor == world.maxPycor && source.pycor == world.minPycor) {
        AgentSet.fromArray(AgentKind.Patch,
                                 Array[Agent](getPatchEast(source),
                                             getPatchWest(source)))
      } else {
        AgentSet.fromArray(AgentKind.Patch,
            Array[Agent](getPatchNorth(source), getPatchEast(source),
                        getPatchSouth(source), getPatchWest(source)))
      }
    }

  override def getPN(source: Patch) =
    getPatchNorth(source)
  override def getPE(source: Patch) =
    if (source.pxcor == world.maxPxcor)
      null
    else
      getPatchEast(source)
  override def getPS(source: Patch) =
    getPatchSouth(source)
  override def getPW(source: Patch) =
    if (source.pxcor == world.minPxcor)
      null
    else
      getPatchWest(source)
  override def getPNE(source: Patch) =
    if (source.pxcor == world.maxPxcor)
      null
    else
      getPatchNorthEast(source)
  override def getPSE(source: Patch) =
    if (source.pxcor == world.maxPxcor)
      null
    else
      getPatchSouthEast(source)
  override def getPSW(source: Patch) =
    if (source.pxcor == world.minPxcor)
      null
    else
      getPatchSouthWest(source)
  override def getPNW(source: Patch) =
    if (source.pxcor == world.minPxcor)
      null
    else
      getPatchNorthWest(source)

  @throws(classOf[AgentException])
  @throws(classOf[PatchException])
  override def diffuse(amount: Double, vn: Int) {
    val xx = world.worldWidth
    val yy = world.worldHeight
    val xx2 = xx * 2
    val yy2 = yy * 2
    val scratch = world.getPatchScratch
    val scratch2 = Array.ofDim[Double](xx, yy)
    val minx = world.minPxcor
    val miny = world.minPycor
    var x, y = 0
    try while(y < yy) {
      x = 0
      while (x < xx) {
        scratch(x)(y) =
          world.fastGetPatchAt(x + minx, y + miny)
            .getPatchVariable(vn)
            .asInstanceOf[java.lang.Double].doubleValue
        scratch2(x)(y) = 0
        x += 1
      }
      y += 1
    }
    catch { case _: ClassCastException =>
      throw new PatchException(
        world.fastGetPatchAt(wrapX(x).toInt, wrapY(y).toInt))
    }
    y = yy
    while (y < yy2) {
      x = xx
      while (x < xx2) {
        val diffuseVal = (scratch(x - xx)(y - yy) / 8) * amount
        if (x > xx && x < xx2 - 1) {
          scratch2(x - xx)(y - yy) += scratch(x - xx)(y - yy) - (8 * diffuseVal)
          scratch2((x - 1) % xx)((y - 1) % yy) += diffuseVal
          scratch2((x - 1) % xx)(y % yy) += diffuseVal
          scratch2((x - 1) % xx)((y + 1) % yy) += diffuseVal
          scratch2(x % xx)((y + 1) % yy) += diffuseVal
          scratch2(x % xx)((y - 1) % yy) += diffuseVal
          scratch2((x + 1) % xx)((y - 1) % yy) += diffuseVal
          scratch2((x + 1) % xx)(y % yy) += diffuseVal
          scratch2((x + 1) % xx)((y + 1) % yy) += diffuseVal
        } else if (x == xx) {
          scratch2(x - xx)(y - yy) += scratch(x - xx)(y - yy) - (5 * diffuseVal)
          scratch2(x % xx)((y + 1) % yy) += diffuseVal
          scratch2(x % xx)((y - 1) % yy) += diffuseVal
          scratch2((x + 1) % xx)((y - 1) % yy) += diffuseVal
          scratch2((x + 1) % xx)(y % yy) += diffuseVal
          scratch2((x + 1) % xx)((y + 1) % yy) += diffuseVal
        } else {
          scratch2(x - xx)(y - yy) += scratch(x - xx)(y - yy) - (5 * diffuseVal)
          scratch2(x % xx)((y + 1) % yy) += diffuseVal
          scratch2(x % xx)((y - 1) % yy) += diffuseVal
          scratch2((x - 1) % xx)((y - 1) % yy) += diffuseVal
          scratch2((x - 1) % xx)(y % yy) += diffuseVal
          scratch2((x - 1) % xx)((y + 1) % yy) += diffuseVal
        }
        x += 1
      }
      y += 1
    }
    y = 0
    while(y < yy) {
      x = 0
      while (x < xx) {
        if (scratch2(x)(y) != scratch(x)(y))
          world.getPatchAtWrap(x + minx, y + miny)
              .setPatchVariable(vn, Double.box(scratch2(x)(y)))
        x += 1
      }
      y += 1
    }
  }

  @throws(classOf[AgentException])
  @throws(classOf[PatchException])
  override def diffuse4(amount: Double, vn: Int) {
    val xx = world.worldWidth
    val yy = world.worldHeight
    val xx2 = xx * 2
    val yy2 = yy * 2
    val scratch = world.getPatchScratch
    val scratch2 = Array.ofDim[Double](xx, yy)
    val minx = world.minPxcor
    val miny = world.minPycor
    var x, y = 0
    try while(y < yy) {
      x = 0
      while (x < xx) {
        scratch(x)(y) =
          world.fastGetPatchAt(x + minx, y + miny)
            .getPatchVariable(vn)
            .asInstanceOf[java.lang.Double].doubleValue
        scratch2(x)(y) = 0
        x += 1
      }
      y += 1
    }
    catch { case _: ClassCastException =>
      throw new PatchException(
        world.fastGetPatchAt(wrapX(x).toInt, wrapY(y).toInt))
    }
    y = yy
    while (y < yy2) {
      x = xx
      while (x < xx2) {
        val diffuseVal = (scratch(x - xx)(y - yy) / 4) * amount
        if (x > 0 && x < xx - 1) {
          scratch2(x - xx)(y - yy) += scratch(x - xx)(y - yy) - (4 * diffuseVal)
          scratch2((x - 1) % xx)(y) += diffuseVal
          scratch2(x % xx)((y + 1) % yy) += diffuseVal
          scratch2(x % xx)((y - 1) % yy) += diffuseVal
          scratch2((x + 1) % xx)(y % yy) += diffuseVal
        } else if (x == xx) {
          scratch2(x - xx)(y - yy) += scratch(x - xx)(y - yy) - (3 * diffuseVal)
          scratch2(x % xx)((y + 1) % yy) += diffuseVal
          scratch2(x % xx)((y - 1) % yy) += diffuseVal
          scratch2((x + 1) % xx)(y % yy) += diffuseVal
        } else {
          scratch2(x - xx)(y - yy) += scratch(x - xx)(y - yy) - (3 * diffuseVal)
          scratch2(x % xx)((y + 1) % yy) += diffuseVal
          scratch2(x % xx)((y - 1) % yy) += diffuseVal
          scratch2((x - 1) % xx)(y % yy) += diffuseVal
        }
        x += 1
      }
      y += 1
    }
    y = 0
    while (y < yy) {
      x = 0
      while (x < xx) {
        if (scratch2(x)(y) != scratch(x)(y))
          world.getPatchAtWrap(x + minx, y + miny)
            .setPatchVariable(vn, Double.box(scratch2(x)(y)))
        x += 1
      }
      y += 1
    }
  }

}
