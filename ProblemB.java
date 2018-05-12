import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/***
http://www.morphoinc.com/careers/challengetest
 xy平面上にn個の長方形が配置されています。
できるだけ多くの長方形を通過するように直線を引いたときの、
通過する長方形の数を標準出力に出力するプログラムを書いてください。
■入力 / Inputs
長方形の数をn、i番目の長方形の左上座標をx[i], y[i], 幅、高さをw[i], h[i]とすると、以下の順に標準入力から与えられます。
n
x[0] y[0] w[0] h[0]
x[1] y[1] w[1] h[1]
・
・
x[n-1] y[n-1] w[n-1] h[n-1]
■注意点 / Notes
・0 < n ≦ 1000
・各長方形は0 ≦ x ≦ 10000, 0 ≦ y ≦ 10000に存在
・各長方形の座標は整数、幅、高さは1以上の整数
・長方形は互いに重なることもあります
・長方形の頂点を通る場合も、通過したとみなします
■ヒント / Hints
Case1
　Input
　4
　0    0    1    1
　9999 0    1    1
　0    9999 1    1
　9999 9999 1    1
　Output
　2
Case2
　Input
　6
　2    1    4    3
　1   10    1    3
　5    7    5    4
　8    8    3    2
　13   4    3    1
　17   1    1   14
　Output
　5
Case3
　Input
input3.txt
　Output
　509

https://stackoverflow.com/questions/49333960/maximum-possible-number-of-rectangles-that-can-be-crossed-with-a-single-straight?newreg=f5b0b1d3260f4da7831fbf3681355bee
***/
public class ProblemB {
    private static double minX = 0, maxX = 10000, minY = 0, maxY = 10000;

    public static void main(String... args) {
        Scanner sc = new Scanner(System.in);
        List<Rectangle2D.Double> rectangles = IntStream.range(0, sc.nextInt())
            .mapToObj(i -> new Rectangle2D.Double(sc.nextDouble(),
                                                  sc.nextDouble(),
                                                  sc.nextDouble(),
                                                  sc.nextDouble()))
            .collect(Collectors.toList());
        // int intersects = rectangles
        //     .stream()
        //     .mapToInt(r1 -> rectangles
        //               .stream()
        //               .filter(r2 -> r1 != r2)
        //               .mapToInt(r2 -> countIntersects(rectangles, r1, r2))
        //               .max()
        //               .orElse(0))
        //     .max()
        //     .orElse(0);
        int intersects = IntStream.range(0, rectangles.size())
            .parallel()
            .map(i1 -> IntStream.range(i1 + 1, rectangles.size())
                 .map(i2 -> countIntersects(rectangles, rectangles.get(i1),
                                            rectangles.get(i2)))
                 .max()
                 .orElse(0))
            .max()
            .orElse(0);
        System.out.println(intersects);
    }

    private static int countIntersects(List<Rectangle2D.Double> rectangles,
                                       Rectangle2D.Double rectangle1,
                                       Rectangle2D.Double rectangle2) {
        // System.err.printf("r1(%s,%s,%s,%s),r2(%s,%s,%s,%s)%n",
        //                   rectangle1.x, rectangle1.y,
        //                   rectangle1.width, rectangle1.height,
        //                   rectangle2.x, rectangle2.y,
        //                   rectangle2.width, rectangle2.height);
        return getVertices(rectangle1)
            .stream()
            .parallel()
            .mapToInt(p1 -> getVertices(rectangle2)
                      .stream()
                      .parallel()
                      .mapToInt(p2 -> countIntersects(rectangles,
                                                      rectangle1, p1,
                                                      rectangle2, p2))
                      .max()
                      .orElse(0))
            .max()
            .orElse(0);
    }

    private static List<Point2D.Double> getVertices(Rectangle2D.Double rectangle) {
        return List.of(new Point2D.Double(rectangle.x, rectangle.y),
                       new Point2D.Double(rectangle.x + rectangle.width,
                                          rectangle.y),
                       new Point2D.Double(rectangle.x,
                                          rectangle.y + rectangle.height),
                       new Point2D.Double(rectangle.x + rectangle.width,
                                          rectangle.y + rectangle.height));
    }

    private static int countIntersects(List<Rectangle2D.Double> rectangles,
                                       Rectangle2D.Double rectangle1,
                                       Point2D.Double point1,
                                       Rectangle2D.Double rectangle2,
                                       Point2D.Double point2) {
        if (rectangle1.contains(rectangle2)
            || rectangle2.contains(rectangle1))  {
            return 2;
        }
        Line2D.Double line = getLine(point1, point2);
        // System.err.printf("   %s,%s%n", intersects(rectangle1, line),
        //                   intersects(rectangle2, line));
        if (intersects(rectangle1, line) || intersects(rectangle2, line)) {
            return 2;
        }
        int result = (int) rectangles.stream()
            .filter(r -> r.intersectsLine(line))
            .count();
        // System.err.printf("  result:%s%n", result);
        return result;
    }

    private static Line2D.Double getLine(Point2D.Double point1,
                                         Point2D.Double point2) {
        Line2D.Double line = null;
        if (point1.x == point2.x) {
            line = new Line2D.Double(point1.x, minY, point2.x, maxY);
        } else {
            double gradient = (point2.y - point1.y) / (point2.x - point1.x);
            line = new Line2D.Double(minX,
                                     gradient * (minX - point1.x) + point1.y,
                                     maxX,
                                     gradient * (maxX - point1.x) + point1.y);
        }
        // System.err.printf("  line:(%s,%s)-(%s,%s) (%s,%s)-(%s,%s)%n",
        //                   line.x1, line.y1, line.x2, line.y2,
        //                   point1.x, point1.y, point2.x, point2.y);
        return line;
    }

    static double delta = 0.00001f;
    private static boolean intersects(Rectangle2D.Double rectangle,
                                      Line2D.Double line) {
        Rectangle2D.Double shrinkedRectangle =
            (Rectangle2D.Double) rectangle.clone();
        shrinkedRectangle.setRect(rectangle.x + delta, rectangle.y + delta,
                                  rectangle.width - 2 * delta,
                                  rectangle.height - 2 * delta);
        return shrinkedRectangle.intersectsLine(line);
    }
}

// Rectangle2D.Float is faster but couldn't avoid rounding errors...
/*
$ echo '4
0    0    1    1
9999 0    1    1
0    9999 1    1
9999 9999 1    1'|java -cp ~/tmp/morpho/ ProblemB
2

$ echo '6
2    1    4    3
1   10    1    3
5    7    5    4
8    8    3    2
13   4    3    1
17   1    1   14'|java -cp ~/tmp/morpho/ ProblemB
5

$ time cat ~/tmp/morpho/input3.txt|java -cp ~/tmp/morpho/ ProblemB
509

real    0m17.689s
user    0m50.771s
sys     0m0.306s

(before parallelized)
real    0m38.904s
user    0m39.914s
sys     0m0.097s

$ echo '13
 80  65  52  25
420  60  48  23
675  51 127  42
328 121  90  41
813 137  71 100
485 195  35  24
615 180 105  40
225 205  35  23
386 256  90  41
144 283  21  98
260 297  90  40
713 305  50  25
385 400  50  25'|java -cp ~/tmp/morpho/ ProblemB
5

$ echo '5
813 137  71 100
615 180 105  40
386 256  90  41
260 297  90  40
145 283  20  98'|java -cp ~/tmp/morpho/ ProblemB
5
 */
