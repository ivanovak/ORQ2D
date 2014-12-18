import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ORQ2DWithFractionalCascading implements ORQ2D {
  public static final class XNode {
    P p;
    XNode l, r;
    XNode(P root, XNode left, XNode right) {
      p = root; l = left; r = right;
    }
    P[] points_y;
  }

  XNode root;
  public ORQ2DWithFractionalCascading(P[] points) {
    P[] points_y = Arrays.copyOf(points, points.length);
    Arrays.sort(points, new Comparator<P>() {
      public int compare(P p1, P p2) {
        return p1.x - p2.x;
      }
    });
    Arrays.sort(points_y, new Comparator<P>() {
      public int compare(P p1, P p2) {
        return p1.y - p2.y;
      }
    });

    root = generate(points, 0, points.length, points_y);
  }

  XNode generate(P[] points_x, int l, int r, P[] points_y) {
    if (r - l == 0) return null;
    int idx = (l + r) / 2;
    P[] pyleft = new P[idx - l];
    P[] pyright = new P[r - idx - 1];
    int idx_l = 0;
    int idx_r = 0;

    for (P p : points_y) {
      if (p.x < points_x[idx].x) pyleft[idx_l++] = p; else pyright[idx_r++] = p;
    }

    XNode res = new XNode(points_x[idx],
      generate(points_x, l, idx - 1, pyleft),
      generate(points_x, idx + 1, r, pyright));
    res.points_y = points_y;
    return res;
  }

  public P[] query(int x1, int y1, int x2, int y2) {
    XNode n = root;
    while ((n.p.x != x1 && n.p.x != x2) &&
      ((n.l != null && x1 < n.p.x && x2 < n.p.x) || (n.r != null && x1 > n.p.x && x2 > n.p.x))) {
      if (n.l != null && x1 < n.p.x && x2 < n.p.x) n = n.l; else n = n.r;
    }
    //
  }
}
