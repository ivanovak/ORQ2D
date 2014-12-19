import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Tester {
  final int POINTS_FROM = 100;
  final int POINTS_TO = 1000;
  final int SPREAD_RANGE = 100;
  final int TESTS = 10;

  public static void main(String[] args) {
    new Tester().start();
  }

  void start() {
    /*P[] points = new P[]{
      new P(1, 1), new P(1, 2), new P(1, 3), new P(2, 3), new P(2, 4), new P(3, 1), new P(3, 5),
      new P(4, 4)
    };*/

    Random rand = new Random();
    for (int t = 0; t < TESTS; ++t) {
      Set<P> pts = new HashSet<>();
      int num = rand.nextInt(POINTS_TO - POINTS_FROM) + POINTS_FROM;
      while (pts.size() != num) {
        pts.add(new P(rand.nextInt(SPREAD_RANGE), rand.nextInt(SPREAD_RANGE)));
      }

      P[] points = pts.toArray(new P[]{});
      ORQ2D t1 = new FractionalCascadingORQ2D(points);
      ORQ2D t2 = new NaiveORQ2D(points);

      int x0 = rand.nextInt(SPREAD_RANGE);
      int y0 = rand.nextInt(SPREAD_RANGE);
      int w = rand.nextInt(SPREAD_RANGE) + 1;
      int h = rand.nextInt(SPREAD_RANGE) + 1;
//      int x0 = 1; int y0 = 3; int w = 2; int h = 5;
      System.out.println("q: " + x0 + " " +y0 + " " + w + " " + h);
      P[] res1 = t1.query(x0, y0, w, h);
      P[] res2 = t2.query(x0, y0, w, h);
      for (P p : res1) {
        System.out.print("(" + p.x + ", " + p.y + ") ");
      }
      System.out.println();
      for (P p : res2) {
        System.out.print("(" + p.x + ", " + p.y + ") ");
      }
      System.out.println();
    }
  }
}
