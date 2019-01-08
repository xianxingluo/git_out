package com.ziyun.net.tools;

/**
 * @author admin
 */
public class GCD {
    /**
     * 求最大公约数
     *
     * @param a
     * @param b
     * @return
     */
    public static int gcd(int a, int b) {
        int r;
        while (b != 0) {
            r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    public static void main(String[] args) {
        System.out.println(gcd(2, 3));
    }
}
