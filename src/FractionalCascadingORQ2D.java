import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FractionalCascadingORQ2D implements ORQ2D {
  int x1, x2, y1, y2; // query parameters
  int lptr, rptr;

  public static final class XNode {
    P p;
    XNode l, r;
    XNode(P root, XNode left, XNode right) {
      p = root; l = left; r = right;
    }
    P[] points_y;
    int rlink[], llink[];
  }

  XNode root;
  public FractionalCascadingORQ2D(P[] points) {
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

  int lower(P[] pts, int val, int l, int r, boolean is_x) {
    int cmpval;
    while (l < r) {
      int m = l + (r - l) / 2;
      if (is_x) cmpval = pts[m].x; else cmpval = pts[m].y;
      if (cmpval < val) {
        l = m + 1;
      } else {
        r = m;
      }
    }

    return r;
  }

  XNode generate(P[] points_x, int l, int r, P[] points_y) {
    if (r - l == 0) return null;
    int idx = l + (r - l) / 2;
    int xelemsleft = idx - lower(points_x, points_x[idx].x, l, r, true);
    P[] pyleft = new P[idx - l];
    P[] pyright = new P[r - idx - 1];
    int llink[] = new int[r - l];
    int rlink[] = new int[r - l];
    int idx_l = 0;
    int idx_r = 0;
    int i = 0;

    for (P p : points_y) {
      llink[i] = idx_l;
      rlink[i++] = idx_r;
      if (points_x[idx].equals(p)) continue;
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
    res.llink = llink;
    res.rlink = rlink;
    return res;
  }

  List<P> addLeft(XNode n, int ptr) {
    List<P> res = new ArrayList<>();
    if (n == null) return res;
    while (ptr < n.points_y.length && n.points_y[ptr].y <= y2) {
      res.add(n.points_y[ptr]);
      ptr++;
    }
    return res;
  }

  List<P> collectFromTheRight(XNode n) {
    List<P> res = new ArrayList<>();
    if (n == null) return res;

    if (n.p.x <= x2) {
      if (rptr < n.rlink.length) {
        res = addLeft(n.l, n.llink[rptr]);
        rptr = n.rlink[rptr];
      }
      res.addAll(collectFromTheRight(n.r));
      if (n.p.y <= y2 && n.p.y >= y1)
        res.add(n.p);
    } else {
      if (rptr < n.rlink.length)
        rptr = n.llink[rptr];
      res.addAll(collectFromTheRight(n.l));
    }

    return res;
  }

  List<P> addRight(XNode n, int ptr) {
    List<P> res = new ArrayList<>();
    if (n == null) return res;
    while (ptr < n.points_y.length && n.points_y[ptr].y <= y2) {
      res.add(n.points_y[ptr]);
      ptr++;
    }

    return res;
  }

  List<P> collectFromTheLeft(XNode n) {
    List<P> res = new ArrayList<>();
    if (n == null) return res;

    if (n.p.x >= x1) {
      if (lptr < n.llink.length) {
        res.addAll(addRight(n.r, n.rlink[lptr]));
        lptr = n.llink[lptr];
      }
      res.addAll(collectFromTheLeft(n.l));
      if (n.p.y <= y2 && n.p.y >= y1)
        res.add(n.p);
    } else {
      if (lptr < n.llink.length)
        lptr = n.rlink[lptr];
      res.addAll(collectFromTheLeft(n.r));
    }

    return res;
  }

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

    if (n.r != null)
      rptr = lower(n.r.points_y, y0, 0, n.r.points_y.length, false);
    if (n.l != null)
      lptr = lower(n.l.points_y, y0, 0, n.r.points_y.length, false);

    List<P> res = collectFromTheRight(n.r);
    res.addAll(collectFromTheLeft(n.l));
    if (n.p.y <= y2 && n.p.y >= y1)
      res.add(n.p);
    return res.toArray(new P[]{});
  }
}
