public class Tester {
  public static void main(String[] args) {
    P[] points = new P[]{
      new P(1, 1), new P(1, 2), new P(1, 3), new P(2, 3), new P(2, 4), new P(3, 1), new P(3, 5)
    };
    ORQ2D t = new ORQ2DWithFractionalCascading(points);
    int i = 1;
  }
}
