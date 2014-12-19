import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Tester {
  final int POINTS_FROM = 100000;
  final int POINTS_TO = 1000000;
  final int SPREAD_RANGE = 1000000;
  final int TESTS = 10;
  final int div = 1000000;
  public static void main(String[] args) {
    new Tester().start();
  }

  void start() {
    Random rand = new Random();
    for (int FROM = 10; FROM <= 1000000; FROM *= 2) {
      long acct1 = 0, acct2 = 0, accnum = 0;
      int TO = FROM * 10;
      for (int t = 0; t < TESTS; ++t) {
        Set<P> pts = new HashSet<>();
        int num = rand.nextInt(TO - FROM) + FROM;
        accnum += num;
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
        long time = System.nanoTime();
        P[] res1 = t1.query(x0, y0, w, h);
        long time1 = System.nanoTime();
        P[] res2 = t2.query(x0, y0, w, h);
        long time2 = System.nanoTime();
        acct1 += time1 - time;
        acct2 += time2 - time1;
        if (res1.length != res2.length) throw new RuntimeException("Wrong result");
//      System.out.println("FC: " + ((time1 - time) / (double)div) + "; Naive: " + ((time2 - time1) / (double)div));
      }
      System.out.println("AvgNum: " + (accnum / TESTS) + "; AvgFC: " + (acct1 / (double)TESTS / div) +
        "; AvgNaive: " + (acct2 / (double)TESTS / div));
    }

  }
}
