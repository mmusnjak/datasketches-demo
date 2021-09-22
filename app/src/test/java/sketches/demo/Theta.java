package sketches.demo;

import static sketches.demo.UtilsKt.showThetaDetails;

import java.util.stream.IntStream;
import java.util.stream.LongStream;
import org.apache.datasketches.theta.SetOperation;
import org.apache.datasketches.theta.UpdateSketch;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Theta {

  @Test
  @DisplayName("Count to 5M and compare sketches of different sizes")
  void nominalSizeComparison() {
    var sketch2k = UpdateSketch.builder().setNominalEntries(2048).build();
    var sketch4k = UpdateSketch.builder().setNominalEntries(4096).build();
    var sketch8k = UpdateSketch.builder().setNominalEntries(8192).build();

    IntStream.range(0, 5_000_000)
        .mapToObj(it -> "user_" + it)
        .forEach(it -> {
          sketch2k.update(it);
          sketch4k.update(it);
          sketch8k.update(it);
        });
    showThetaDetails(sketch2k, 5_000_000);
    showThetaDetails(sketch4k, 5_000_000);
    showThetaDetails(sketch8k, 5_000_000);
  }

  @Test
  @DisplayName("Count to 300M and compare a small and large sketch")
  void extremeCount() {
    var small = UpdateSketch.builder().setNominalEntries(2048).build();
    var large = UpdateSketch.builder().setNominalEntries(8192).build();
    IntStream.range(0, 300_000_000).mapToObj(it -> "user_" + it)
        .forEach(it -> {
          small.update(it);
          large.update(it);
        });
    showThetaDetails(small, 300_000_000);
    showThetaDetails(large, 300_000_000);
  }

  @Test
  @DisplayName("Union between sketches")
  void unionExample() {

    var nominalEntries = 4096;
    var sketch0To75k = UpdateSketch.builder().setNominalEntries(nominalEntries).build();
    var sketch50kTo100k = UpdateSketch.builder().setNominalEntries(nominalEntries).build();

    LongStream.range(0, 75_000)
        .mapToObj(it -> "user_" + it)
        .forEach(sketch0To75k::update);
    LongStream.range(50_000, 100_000)
        .mapToObj(it -> "user_" + it)
        .forEach(sketch50kTo100k::update);

    showThetaDetails(sketch0To75k, 75_000);
    showThetaDetails(sketch50kTo100k, 50_000);

    var union = SetOperation.builder().buildUnion();
    union.union(sketch0To75k);
    union.union(sketch50kTo100k);

    showThetaDetails(union.getResult(), 100_000);
  }

  @Test
  @DisplayName("Intersection between sketches")
  void intersectionExample() {

    var nominalEntries = 4096;
    var sketch0To75k = UpdateSketch.builder().setNominalEntries(nominalEntries).build();
    var sketch50kTo100k = UpdateSketch.builder().setNominalEntries(nominalEntries).build();

    LongStream.range(0, 75_000)
        .mapToObj(it -> "user_" + it)
        .forEach(sketch0To75k::update);
    LongStream.range(50_000, 100_000)
        .mapToObj(it -> "user_" + it)
        .forEach(sketch50kTo100k::update);

    showThetaDetails(sketch0To75k, 75_000);
    showThetaDetails(sketch50kTo100k, 50_000);

    var intersection = SetOperation.builder().buildIntersection();
    intersection.intersect(sketch0To75k);
    intersection.intersect(sketch50kTo100k);

    showThetaDetails(intersection.getResult(), 25_000);
  }

  @Test
  @DisplayName("Intersection between unbalanced sketches")
  void unbalancedIntersectionExample() {

    var nominalEntries = 4096;
    var sketch0To100M = UpdateSketch.builder().setNominalEntries(nominalEntries).build();
    var sketch50kTo55k = UpdateSketch.builder().setNominalEntries(nominalEntries).build();

    LongStream.range(0, 100_000_000)
        .mapToObj(it -> "user_" + it)
        .forEach(sketch0To100M::update);
    LongStream.range(50_000, 55_000)
        .mapToObj(it -> "user_" + it)
        .forEach(sketch50kTo55k::update);

    var intersection = SetOperation.builder().buildIntersection();
    intersection.intersect(sketch0To100M);
    intersection.intersect(sketch50kTo55k);

    showThetaDetails(intersection.getResult(), 5_000);
  }
}
