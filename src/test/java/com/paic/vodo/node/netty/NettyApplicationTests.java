package com.paic.vodo.node.netty;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class NettyApplicationTests {

    @Test
    public void contextLoads() {

//        int i = maxPoints(new Point[]{new Point(1, 1), new Point(3, 2),
//                 new Point(5, 3), new Point(4, 1),
//                new Point(2, 3), new Point(1, 4)});
       int i= maxPoints(new Point[]{new Point(3,1),new Point(3,1),new Point(12,3),new Point(-6,-1)});
        System.out.println(i);
    }


    class Point {
        int x;
        int y;

        Point() {
            x = 0;
            y = 0;
        }

        Point(int a, int b) {
            x = a;
            y = b;
        }
    }
    public class Func{
        BigDecimal k;
        BigDecimal c;

        public Func(BigDecimal k, BigDecimal c) {
            this.k = k;
            this.c = c;
        }
    }

    public int maxPoints(Point[] points) {
        Set<Func> set = new HashSet();
        Set<String> tsset = new HashSet();
        if (points.length <= 2) {
            return points.length;
        }

        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (i == j) {
                    continue;
                }
                if(points[i].y == points[j].y){
                    tsset.add("y="+points[j].y);
                    continue;
                }
                if(points[i].x == points[j].x){
                    tsset.add("x="+points[j].x);
                    continue;
                }

                BigDecimal k = new BigDecimal(points[i].y - points[j].y).divide(new BigDecimal(points[i].x - points[j].x),10,ROUND_HALF_DOWN);
                BigDecimal c=new BigDecimal(points[i].y).subtract(new BigDecimal(points[i].x).multiply(k));
                set.add(new Func(k,c));
            }
        }
        int max = 0;
        for (String s : tsset) {
            int n=0;
            String[] split = s.split("=");
            Double c = Double.parseDouble(split[1]);
            for (int i = 0; i < points.length; i++) {
                if("x".equals(split[0])){
                    if(c==points[i].x){
                        n++;
                    }
                }
                if("y".equals(split[0])){
                    if(c==points[i].y){
                        n++;
                    }
                }
            }
            if(n>max){
                max=n;
            }
        }
        for (Func s : set) {
            int n=0;
            for (int i = 0; i < points.length; i++) {
               BigDecimal p1=s.k,
                p2=s.c;
                if(p1.divide(new BigDecimal(points[i].x)).add(p2).equals(new BigDecimal(points[i].y))){
                    n++;
                }
            }
            if(n>max){
                max=n;
            }
        }
        return max;
    }


}

