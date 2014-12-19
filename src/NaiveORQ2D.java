import java.util.ArrayList;
import java.util.List;

public class NaiveORQ2D implements ORQ2D {
  P[] pts;
  NaiveORQ2D(P[] points) {
    pts = points;
  }

  public P[] query(int x0, int y0, int w, int h) {
    List<P> ans = new ArrayList<>();
    for (P p : pts) {
      if (p.x >= x0 && p.x <= x0 + w - 1 && p.y >= y0 && p.y <= y0 + h - 1) {
        ans.add(p);
      }
    }
    return ans.toArray(new P[]{});
  }
}
