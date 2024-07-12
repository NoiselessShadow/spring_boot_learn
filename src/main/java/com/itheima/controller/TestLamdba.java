package com.itheima.controller;

import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

public class TestLamdba {


    public static void main(String[] args) {

        //函数式编程
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("线程启动");
//            }
//        }).start();

//        new Thread(()->{
//            System.out.println("匿名内部类可省，一样的");
//        }).start();

//        int i1 = testAnonymous(
//                new IntBinaryOperator() {
//                    @Override
//                    public int applyAsInt(int left, int right) {
//                        return 0;
//                    }
//                }
//        );
//        int i = testAnonymous(
//                (left, right) -> {
//                    return left + right;
//                }
//        );
//        System.out.println(i1+"========"+i);

//        printNum(
//                new IntPredicate() {
//                    @Override
//                    public boolean test(int value) {
//                        return  value % 2 == 0;
//                    }
//                }
//        );
//
//        printNum((item)->{
//            return item%2 == 0;
//        });

//        Integer i = typeConver(new Function<String, Integer>() {
//            public Integer apply(String s) {
//                return Integer.valueOf(s) + 100;
//            }
//        });
//
//        String s1 = typeConver((String s) -> {
//            return s + "我TM来啦";
//        });
//        System.out.println(s1);
//        System.out.println(i);
    printNum(new IntConsumer() {
        @Override
        public void accept(int value) {
            System.out.println("打一个 "+ value);
        }
    });

        printNum( (int value)->{
                System.out.println("打第二次 "+ value);
            }
        );

        printNum((IntConsumer) value->  System.out.println("打第二次 "+ value));

    }


    public static void printNum(IntConsumer intConsumer){
        int[] arr = {1,2,3,4,5,6,7,8,9};

        for (int i : arr) {
            intConsumer.accept(i);
        }
    }

    public static <R> R typeConver(Function<String,R> f){
            String str = "123";
            R result = f.apply(str);
            return result;
    }

    public  static int testAnonymous(IntBinaryOperator operator){
        int a = 10;
        int b = 20;
        return operator.applyAsInt(a,b);

    }

    public static void printNum(IntPredicate predicate){
        int[] arr = {1,2,3,4,5,6,7,8,9};

        for (int i = 0; i < arr.length; i++) {
            if(predicate.test(arr[i])){
                System.out.print(arr[i]+" ");
            }
        }
    }


















}
