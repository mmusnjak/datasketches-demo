package sketches.demo;

import static sketches.demo.UtilsKt.showHLLDetails;

import java.util.stream.IntStream;
import java.util.stream.LongStream;
import org.apache.datasketches.hll.HllSketch;
import org.apache.datasketches.hll.Union;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HyperLogLog {

  @Test
  @DisplayName("Count to 5M and compare sketches of different sizes")
  void lgkComparison() {
    var hllSketch12 = new HllSketch(12);
    var hllSketch16 = new HllSketch(16);
    var hllSketch20 = new HllSketch(20);

    showHLLDetails(hllSketch12, 0);
    showHLLDetails(hllSketch16, 0);
    showHLLDetails(hllSketch20, 0);

    IntStream.range(0, 5_000_000)
        .mapToObj(it -> "user_" + it)
        .forEach(it -> {
          hllSketch12.update(it);
          hllSketch16.update(it);
          hllSketch20.update(it);
        });

    showHLLDetails(hllSketch12, 5_000_000);
    showHLLDetails(hllSketch16, 5_000_000);
    showHLLDetails(hllSketch20, 5_000_000);
  }

  @Test
  @Disabled
  @DisplayName("Count to 300M and compare a small and large sketch")
  void extremeCount() {
    var small = new HllSketch(10);
    var large = new HllSketch(20);
    IntStream.range(0, 300_000_000)
        .mapToObj(it -> "user_" + it)
        .forEach(it -> {
          small.update(it);
          large.update(it);
        });

    showHLLDetails(small, 300_000_000);
    showHLLDetails(large, 300_000_000);
  }

  @Test
  @DisplayName("Union between sketches")
  void unionExample() {
    var lgK = 12; // varying lgK changes size and precision of sketches+unions
    var sketch0To75k = new HllSketch(lgK);
    var sketch50kTo100k = new HllSketch(lgK);

    // Use different items/types to get different accuracy
    LongStream.range(0, 75_000)
//        .mapToObj(it -> "user_" + it)
        .forEach(sketch0To75k::update);
    LongStream.range(50_000, 100_000)
//        .mapToObj(it -> "user_" + it)
        .forEach(sketch50kTo100k::update);

    showHLLDetails(sketch0To75k, 75_000);
    showHLLDetails(sketch50kTo100k, 50_000);

    var union = new Union(lgK);
    union.update(sketch0To75k);
    union.update(sketch50kTo100k);

    showHLLDetails(union.getResult(), 100_000);
  }

  @Test
  @DisplayName("Serialize and deserialize sketch")
  void serializationExample() {
    var sketch0To75k = new HllSketch();
    LongStream.range(0, 75_000).mapToObj(it -> "user_" + it).forEach(sketch0To75k::update);

    showHLLDetails(sketch0To75k, 75_000);

    var firstSketchBytes = sketch0To75k.toCompactByteArray();
    Assertions.assertEquals(sketch0To75k.getCompactSerializationBytes(), firstSketchBytes.length);
    var reconstructed0to75kSketch = HllSketch.heapify(firstSketchBytes);
    showHLLDetails(reconstructed0to75kSketch, 75_000);

    reconstructed0to75kSketch.update("another value");
  }
}
