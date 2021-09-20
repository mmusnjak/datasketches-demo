package sketches.demo

import org.apache.datasketches.hll.HllSketch
import org.apache.datasketches.theta.Sketch
import kotlin.math.abs

fun showHLLDetails(sketch: HllSketch, expected: Int) {
    val diff = abs(sketch.estimate.toInt() - expected)
    val err = if (expected == 0) 0.0 else 100.0 * diff / expected
    val compactSize = sketch.compactSerializationBytes
    val updatableSize = sketch.updatableSerializationBytes
    val lgK = sketch.lgConfigK
    print("estimate: ${sketch.estimate.toInt()}, true value: $expected\n")
    print("difference: $diff, pct: $err\n")
    print("lgK: $lgK, updatable size: $updatableSize, compact size: $compactSize\n")
    print("68%: lower bound: ${sketch.getLowerBound(1).toLong()}, upper bound: ${sketch.getUpperBound(1).toLong()}\n")
    print("95%: lower bound: ${sketch.getLowerBound(2).toLong()}, upper bound: ${sketch.getUpperBound(2).toLong()}\n")
    print("99%: lower bound: ${sketch.getLowerBound(3).toLong()}, upper bound: ${sketch.getUpperBound(3).toLong()}\n")
    print("===\n\n")
}

fun showThetaDetails(sketch: Sketch, expected: Int) {
    val diff = abs(sketch.estimate.toInt() - expected)
    val err = if (expected == 0) 0.0 else 100.0 * diff / expected
    val compactSize = sketch.compactBytes
    val updatableSize = sketch.currentBytes
    val lowerBound = sketch.getLowerBound(1).toLong()
    val upperBound = sketch.getUpperBound(1).toLong()
    print("estimate: ${sketch.estimate.toInt()}, true value: $expected\n")
    print("difference: $diff, pct: $err\n")
    print("updatable size: $updatableSize, compact size: $compactSize\n")
    print("68%: lower bound: ${sketch.getLowerBound(1).toLong()}, upper bound: ${sketch.getUpperBound(1).toLong()}\n")
    print("95%: lower bound: ${sketch.getLowerBound(2).toLong()}, upper bound: ${sketch.getUpperBound(2).toLong()}\n")
    print("99%: lower bound: ${sketch.getLowerBound(3).toLong()}, upper bound: ${sketch.getUpperBound(3).toLong()}\n")
    print("===\n\n")
}

