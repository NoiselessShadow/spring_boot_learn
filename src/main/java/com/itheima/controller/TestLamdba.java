package com.itheima.controller;

import com.itheima.bean.Author;
import com.itheima.bean.Book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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


    public static List<Author> getAuthors(){
//        private Long id;
//        private String name;
//        private Integer age;
//        private String intro;
//        private List<Book> books;
        Author author = new Author(1L, "姓名", 20, "简介", null);
        Author author2 = new Author(2L, "作者2", 22, "简介2", null);
        Author author3 = new Author(3L, "作者3", 23, "简介3", null);
        Author author4 = new Author(4L, "作者4", 24, "简介4", null);
        Author author5 = new Author(5L, "作者5", 25, "简介5", null);


        List<Book> bookList = new ArrayList<>();
        List<Book> bookList2 = new ArrayList<>();
        List<Book> bookList3 = new ArrayList<>();
        List<Book> bookList4 = new ArrayList<>();

        bookList.add(new Book(11L,"书名11", 85,"第11本书的内容"));
        bookList.add(new Book(12L,"书名12", 87,"第12本书的内容"));
        bookList.add(new Book(13L,"书名13", 88,"第13本书的内容"));

        bookList2.add(new Book(21L,"书名21", 285,"第21本书的内容"));
        bookList2.add(new Book(22L,"书名22", 287,"第22本书的内容"));
        bookList2.add(new Book(23L,"书名23", 288,"第23本书的内容"));


        bookList3.add(new Book(31L,"书名31", 385,"第31本书的内容"));
        bookList3.add(new Book(32L,"书名32", 387,"第32本书的内容"));
        bookList3.add(new Book(33L,"书名33", 388,"第33本书的内容"));

        bookList4.add(new Book(41L,"书名31", 485,"第41本书的内容"));
        bookList4.add(new Book(42L,"书名32", 487,"第42本书的内容"));
        bookList4.add(new Book(43L,"书名33", 488,"第43本书的内容"));

        author.setBooks(bookList);
        author2.setBooks(bookList2);
        author3.setBooks(bookList3);
        author4.setBooks(bookList4);

        List<Author> authorList = new ArrayList<>(Arrays.asList(author,author2,author3,author4));
        return authorList;
    }



















}
