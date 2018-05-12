import java.util.Scanner;

// http://www.morphoinc.com/careers/challengetest
// １～４のN乗まですべての数を４進数表記した際に使用される “３” の個数を、
// 標準出力に出力するプログラムを作成してください。
// 例：N=2 のとき、使用される “3” の個数は 8 になります
// (1, 2, 3, 10, 11, 12, 13, 20, 21, 22, 23, 30, 31, 32, 33)。
// ■入力 / Inputs
// 標準入力から 0≦N≦30 で与えられるものとします。
// ————
// A number N in the range 0≦N≦30, entered from the standard input (command line interface).
// ■ヒント / Hints
// Case 1
//  Input
//  2
//  Output
//  8
//
// Case 2
//  Input
//  30
//  Output
//  8646911284551352320
public class ProblemA {
    public static void main(String... args) {
        int input = new Scanner(System.in).nextInt();
        // long answer = java.util.stream.LongStream
        //     .range(0, (long) Math.pow(4, input))
        //     .map(l -> Long.toString(l, 4)
        //          .codePoints().filter(ch -> ch == '3').count())
        //     .sum();
        long answer = (long) Math.pow(4, input -1) * input;
        System.out.println(answer);
    }
}
// 1             4**0*1
// 8   4**2      4**1*2
// 48  4**2*3    4**2*3
// 256 4**4      4**3*4
// 1280 4**4*5   4**4*5
// 6144 4**5*3*2 4**5*6
// 28672 4**6*7  4**6*7
// 131072        4**7*8
// 589824        4**8*9
// 2621440       4**9*10
// 11534336      4**10*11
//   :
// 8646911284551352320 4**29*30
