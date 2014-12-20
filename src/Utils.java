public class Utils {

  static final int lower(P[] pts, int val, int l, int r, boolean is_x) {
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
}
