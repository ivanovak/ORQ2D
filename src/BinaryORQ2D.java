import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BinaryORQ2D implements ORQ2D {
  P[] pts;
  Comparator<P> cmp = new Comparator<P>() {
    public int compare(P o1, P o2) {
      if (o1.x == o2.x) return o1.y - o2.y;
      return o1.x - o2.x;
    }
  };

  BinaryORQ2D(P[] points) {
    pts = points;
    Arrays.sort(pts, cmp);
  }

  public P[] query(int x0, int y0, int w, int h) {
    List<P> ans = new ArrayList<>();

    for (int i = Utils.lower(pts, x0, 0, pts.length, true); i < pts.length; ++i) {
      if (pts[i].x >= x0 + w) break;
      if (pts[i].x >= x0 && pts[i].x <= x0 + w - 1 && pts[i].y >= y0 && pts[i].y <= y0 + h - 1) {
        ans.add(pts[i]);
      }
    }

    return ans.toArray(new P[]{});
  }
}
