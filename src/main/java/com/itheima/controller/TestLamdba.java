package com.itheima.controller;

public class TestLamdba {


    public static void main(String[] args) {

        //函数式编程
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("线程启动");
//            }
//        }).start();

        new Thread(()->{
            System.out.println("匿名内部类可省，一样的");
        }).start();
    }
}
