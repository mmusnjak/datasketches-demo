package sketches.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;
import org.apache.datasketches.kll.KllFloatsSketch;
import org.junit.jupiter.api.Test;

public class Quantiles {

  @Test
  void quantilesExample() {
    var sketch1 = new KllFloatsSketch(); // default k=128

    IntStream.range(0, 50_000_000).forEach(sketch1::update);

    var firstRank = sketch1.getRank(50_000_000.0f) * 100;
    assertEquals(100.0, firstRank, 0.0001);
    System.out.println(
        "Rank: " + firstRank + ", serializedSize: " + sketch1.getSerializedSizeBytes());

    IntStream.range(51_000_000, 100_000_000).forEach(sketch1::update);
    var secondRank = sketch1.getRank(50_000_000.0f) * 100;
    assertEquals(50.0, secondRank, 0.8);
    System.out.println(
        "Rank: " + secondRank + ", serializedSize: " + sketch1.getSerializedSizeBytes());

    IntStream.range(51_000_000, 100_000_000).forEach(sketch1::update);
    var thirdRank = sketch1.getRank(50_000_000.0f) * 100;
    assertEquals(33.3, thirdRank, 0.5);
    System.out.println(
        "Rank: " + thirdRank + ", serializedSize: " + sketch1.getSerializedSizeBytes());

    IntStream.range(51_000_000, 100_000_000).forEach(sketch1::update);
    var fourthRank = sketch1.getRank(50_000_000.0f) * 100;
    assertEquals(25.0, fourthRank, 0.5);
    System.out.println(
        "Rank: " + fourthRank + ", serializedSize: " + sketch1.getSerializedSizeBytes());

    var median = sketch1.getQuantile(0.5);
    assertEquals(67_000_000.0, median, 300_000.0);
  }
}
