import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FractionalCascadingORQ2D implements ORQ2D {
  int x1, x2, y1, y2; // Параметры запроса (x2 = x1 + w - 1, y2 = y1 + h - 1).
  int lptr, rptr;     // Индексы с которых мы начинаем считывать массив
                      // (для левого и правого спуска соответственно).
                      // Когда нам нужно выбрать все элементы попадающие в интервал по y.

  // Вершина бинарного дерева.
  // Значение в вершине соответствует какой-то точке исходного множества, а ключ - ее x координата.
  public static final class XNode {
    P p; // Значение.
    XNode l, r; // Левое и правое поддерево.
    XNode(P root, XNode left, XNode right) {
      p = root; l = left; r = right;
    }
    P[] points_y; // Массив отсортированных по y вершин принадлежащих дереву с корнем в нашей вершине.
    int rlink[], llink[]; // Массивы ссылок на позиции в r.points_y и l.points_y.
  }

  XNode root;
  public FractionalCascadingORQ2D(P[] points) {
    P[] points_y = Arrays.copyOf(points, points.length);

    Arrays.sort(points, new Comparator<P>() {
      public int compare(P p1, P p2) {
        if (p1.x == p2.x) return p1.y - p2.y;
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

  /**
   * Строит дерево из списка вершин `points_x` лежащих в полуинтервале [l, r).
   * Инвариант дерева: l.x <= root.x, r.x >= root.x.
   * (Следует отметить что нестрогое неравенство с двух сторон не поменяет асимптотики).
   *
   * @param points_x вершины отсортированные по x, а имеющие одинаковый x сортируются по y
   *                 (тоже самое, что сортировка сначала по y и затем стабильная по x).
   * @param l левая граница, включительно.
   * @param r правая граница, исключительно.
   * @param points_y массив того же множества точек что и `points_x`, но упорядоченный по y.
   * @return корень дерева.
   */
  XNode generate(P[] points_x, int l, int r, P[] points_y) {
    if (r - l == 0) return null;
    int idx = l + (r - l) / 2;
    // Здесь idx может попасть в какую-то из подряд идущих точек имеющих одинаковый x.
    // Тогда слева от idx будет xelemsleft точек с таким же x.
    // Это значение нам нужно, чтобы решать в какое поддерево отправлять точки
    // из массива `points_y` при его обходе.
    int xelemsleft = idx - Utils.lower(points_x, points_x[idx].x, l, r, true);
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

  /**
   * Возвращает подмножество искомых вершин содержащихся в поддереве.
   * @param n корень поддерева.
   * @param ptr индекс в `points_y` с которого мы начинаем итерироваться.
   */
  List<P> collectFiltered(XNode n, int ptr) {
    List<P> res = new ArrayList<>();
    if (n == null) return res;
    while (ptr < n.points_y.length && n.points_y[ptr].y <= y2) {
      res.add(n.points_y[ptr]);
      ptr++;
    }
    return res;
  }

  /**
   * Рекурсивная null-safe ф-ия запускаемая в вершинах правого пути спуска и
   * возвращающая список вершин входящих в искомое множество.
   */
  List<P> collectFromTheRight(XNode n) {
    List<P> res = new ArrayList<>();
    if (n == null) return res;

    if (n.p.x <= x2) {
      // Если наш x <= x2 (правой границе), то все левое поддерево должно
      // пойти в ответ (collectFiltered), и мы переходим в превое поддерево рекурсивным
      // вызовом.
      if (rptr < n.rlink.length) {
        res = collectFiltered(n.l, n.llink[rptr]);
        rptr = n.rlink[rptr];
      }
      res.addAll(collectFromTheRight(n.r));
      // Проверяем точку в нашей вершине.
      if (n.p.y <= y2 && n.p.y >= y1)
        res.add(n.p);
    } else {
      // Если же наш x больше правой границы, то просто вызываемся рекурсивно от
      // левого поддерева.
      if (rptr < n.rlink.length)
        rptr = n.llink[rptr];
      res.addAll(collectFromTheRight(n.l));
    }

    return res;
  }

  // Аналогично `collectFromTheRight`.
  List<P> collectFromTheLeft(XNode n) {
    List<P> res = new ArrayList<>();
    if (n == null) return res;

    if (n.p.x >= x1) {
      if (lptr < n.llink.length) {
        res.addAll(collectFiltered(n.r, n.rlink[lptr]));
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

  /**
   * Выдает все точки из прямоугольника (x0, y0, x0 + w - 1, y0 + h - 1).
   * @param x0 координата x нижней левой вершины.
   * @param y0 координата y нижней левой вершины.
   * @param w ширина прямоугольника.
   * @param h высота прямоугольника.
   * @return массив P[] точек из вышеописанного прямоугольника.
   */
  public P[] query(int x0, int y0, int w, int h) {
    x1 = x0;
    y1 = y0;
    x2 = x0 + w - 1;
    y2 = y0 + h - 1;
    XNode n = root;
    // Спускаемся до точки содержащейся в нашем интервале по x.
    while ((n.p.x != x1 && n.p.x != x2) &&
      ((n.l != null && x1 < n.p.x && x2 < n.p.x) || (n.r != null && x1 > n.p.x && x2 > n.p.x))) {
      if (n.l != null && x1 < n.p.x && x2 < n.p.x) n = n.l; else n = n.r;
    }

    // За O(2log(n)) находим в левом и правом массивах стартовые индексы.
    if (n.r != null)
      rptr = Utils.lower(n.r.points_y, y0, 0, n.r.points_y.length, false);
    if (n.l != null)
      lptr = Utils.lower(n.l.points_y, y0, 0, n.l.points_y.length, false);

    List<P> res = collectFromTheRight(n.r); // Обход справа.
    res.addAll(collectFromTheLeft(n.l));    // Обход слева.
    if (n.p.y <= y2 && n.p.y >= y1 && n.p.x <= x2 && n.p.x >= x1) // Возможно стоит добавить корень.
      res.add(n.p);
    return res.toArray(new P[]{});
  }
}
