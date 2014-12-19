import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ORQ2DWithFractionalCascading implements ORQ2D {
  int x1, x2, y1, y2; // query parameters

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

  int lowerx(P[] pts, int val) {
    int l = 0;
    int r = pts.length;
    if (r == 0) return 0;
    while (l < r) {
      int m = (l + r) / 2;
      if (pts[m].x < val) {
        l = m + 1;
      } else {
        r = m;
      }
    }

    if (l == pts.length - 1)
      return pts[l].x >= val ? l : l + 1;
    else if (l == 0)
      return pts[l].x >= val ? 0 : 1;

    return pts[l].x >= val ? l : l + 1;
  }

  XNode generate(P[] points_x, int l, int r, P[] points_y) {
    if (r - l == 0) return null;
    int idx = (l + r) / 2;
    int xelemsleft = idx - lowerx(points_x, points_x[idx].x);
    P[] pyleft = new P[idx - l];
    P[] pyright = new P[r - idx - 1];
    int idx_l = 0;
    int idx_r = 0;

    for (P p : points_y) {
      if (p.x == points_x[idx].x && p.y == points_x[idx].y)
        continue;
      if (p.x == points_x[idx].x) {
        if (xelemsleft != 0) {
          xelemsleft--;
          pyleft[idx_l++] = p;
        } else {
          pyright[idx_r++] = p;
        }
      } else if (p.x < points_x[idx].x) {
        pyleft[idx_l++] = p;
      } else {
        pyright[idx_r++] = p;
      }
    }

    XNode res = new XNode(points_x[idx],
      generate(points_x, l, idx, pyleft),
      generate(points_x, idx + 1, r, pyright));
    res.points_y = points_y;
    return res;
  }

  /*List<P> addLeft(XNode l) {

  }

  List<P> getRight(XNode r) {
    List<P> res = new ArrayList<>();
    if (r == null) return res;
    if (r.r != null && r.r.p.x >= x2) {
      res.addAll(addLeft(r.l));
      res.addAll(getRight(r.r));
    } else {
      res.addAll(getRight(r.l));
    }

    return res;
  }*/

  public P[] query(int x0, int y0, int w, int h) {
    x1 = x0;
    y1 = y0;
    x2 = x0 + w - 1;
    y2 = y0 + h - 1;
    XNode n = root;
    while ((n.p.x != x1 && n.p.x != x2) &&
      ((n.l != null && x1 < n.p.x && x2 < n.p.x) || (n.r != null && x1 > n.p.x && x2 > n.p.x))) {
      if (n.l != null && x1 < n.p.x && x2 < n.p.x) n = n.l; else n = n.r;
    }
//    List<P> res = getRight(n.r).addAll(getLeft(n.l));
//    return res.toArray(new P[]{});
    return null;
  }
}
