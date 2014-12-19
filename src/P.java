public final class P {
  int x, y;
  P(int ax, int ay) { x = ax; y = ay; }
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  @Override
  public int hashCode() {
    return x ^ y;
  }

  @Override
  public boolean equals(Object obj) {
    return ((P)obj).x == x && ((P)obj).y == y;
  }
}
