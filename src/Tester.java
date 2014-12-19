import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Tester {
  final int POINTS_FROM = 10;
  final int POINTS_TO = 100000000;
  final int SPREAD_RANGE = 1000000;
  final int QUERIES = 200;
  final int div = 1000000;
  public static void main(String[] args) {
    new Tester().start();
  }

  void start() {
    Random rand = new Random();
    for (int PTS = POINTS_FROM; PTS <= POINTS_TO; PTS *= 2) {
      long acct1 = 0, acct2 = 0;
      Set<P> pts = new HashSet<>();
      while (pts.size() != PTS) {
        pts.add(new P(rand.nextInt(SPREAD_RANGE), rand.nextInt(SPREAD_RANGE)));
      }

      P[] points = pts.toArray(new P[]{});
      ORQ2D t1 = new FractionalCascadingORQ2D(points);
      ORQ2D t2 = new NaiveORQ2D(points);

      for (int t = 0; t < QUERIES; ++t) {
        int x0 = rand.nextInt(SPREAD_RANGE);
        int y0 = rand.nextInt(SPREAD_RANGE);
        int w = rand.nextInt(SPREAD_RANGE) + 1;
        int h = rand.nextInt(SPREAD_RANGE) + 1;
        long time = System.nanoTime();
        P[] res1 = t1.query(x0, y0, w, h);
        long time1 = System.nanoTime();
        P[] res2 = t2.query(x0, y0, w, h);
        long time2 = System.nanoTime();
        acct1 += time1 - time;
        acct2 += time2 - time1;
        if (res1.length != res2.length) {
          System.err.println("REQ: " + x0 + " " + y0 + " " + w + " " + h);
          System.err.print("fc:");
          for (P p : res1) System.err.print(p + " ");
          System.err.print("\nnaive: ");
          for (P p : res2) System.err.print(p + " ");
          System.err.println();
          throw new RuntimeException("Wrong result");
        }
      }
      System.out.println("PTS: " + PTS + "; AvgFC: " + (acct1 / (double)QUERIES / div) +
        "; AvgNaive: " + (acct2 / (double)QUERIES / div));
    }

  }
}
