package com.itheima.controller;

import com.itheima.bean.Author;
import com.itheima.bean.Book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.*;
import java.util.stream.Stream;

public class TestLamdba {


//    public static void main(String[] args) {
//
//        //函数式编程
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                System.out.println("线程启动");
////            }
////        }).start();
//
////        new Thread(()->{
////            System.out.println("匿名内部类可省，一样的");
////        }).start();
//
////        int i1 = testAnonymous(
////                new IntBinaryOperator() {
////                    @Override
////                    public int applyAsInt(int left, int right) {
////                        return 0;
////                    }
////                }
////        );
////        int i = testAnonymous(
////                (left, right) -> {
////                    return left + right;
////                }
////        );
////        System.out.println(i1+"========"+i);
//
////        printNum(
////                new IntPredicate() {
////                    @Override
////                    public boolean test(int value) {
////                        return  value % 2 == 0;
////                    }
////                }
////        );
////
////        printNum((item)->{
////            return item%2 == 0;
////        });
//
////        Integer i = typeConver(new Function<String, Integer>() {
////            public Integer apply(String s) {
////                return Integer.valueOf(s) + 100;
////            }
////        });
////
////        String s1 = typeConver((String s) -> {
////            return s + "我TM来啦";
////        });
////        System.out.println(s1);
////        System.out.println(i);
//    printNum(new IntConsumer() {
//        @Override
//        public void accept(int value) {
//            System.out.println("打一个 "+ value);
//        }
//    });
//
//        printNum( (int value)->{
//                System.out.println("打第二次 "+ value);
//            }
//        );
//
//        printNum((IntConsumer) value->  System.out.println("打第二次 "+ value));
//
//    }


    public static void printNum(IntConsumer intConsumer) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        for (int i : arr) {
            intConsumer.accept(i);
        }
    }

    public static <R> R typeConver(Function<String, R> f) {
        String str = "123";
        R result = f.apply(str);
        return result;
    }

    public static int testAnonymous(IntBinaryOperator operator) {
        int a = 10;
        int b = 20;
        return operator.applyAsInt(a, b);

    }

    public static void printNum(IntPredicate predicate) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        for (int i = 0; i < arr.length; i++) {
            if (predicate.test(arr[i])) {
                System.out.print(arr[i] + " ");
            }
        }
    }


    public static void main(String[] args) {
        List<Author> authors = getAuthors();
        //将对象某个属性获取出来当做单独的集合：
        //第一个属性为入参，第二个属性是泛型，用作方法的返回值
//        authors.stream().map(new Function<Author, String>() {
//            @Override
//            public String apply(Author author) {
//                return  author.getName();
//            }
//        }).forEach(new Consumer<String>() {
//            @Override
//            public void accept(String s) {
//                System.out.println(s);
//            }
//        });
//        authors.stream().map(author -> author.getName()).forEach(System.out::println);
//        authors.stream().map(author -> author.getId()).forEach(System.out::println);
//
//        authors.stream().sorted().forEach(author -> System.out.println(author.getName()+":"+author.getAge()));
//        authors.stream().sorted().forEach(System.out::println);
//
//        authors.stream().sorted(
//                new Comparator<Author>() {
//                    @Override
//                    public int compare(Author o1, Author o2) {
//                        return o1.getAge() - o2.getAge();
//                    }
//                }
//        ).forEach(System.out::println);
//        authors.stream().sorted((o1,o2)->o2.getAge()-o1.getAge()).forEach(System.out::println);
//        //截取前N个元素
//        authors.stream().sorted((o1,o2)->o2.getAge()-o1.getAge()).limit(2).forEach(item -> System.out.println(item.getName()));
//        System.out.println("=================");
//        //跳过前N个元素
//        authors.stream().sorted((o1,o2)->o2.getAge()-o1.getAge()).skip(2).forEach(item -> System.out.println(item.getName()));
        authors.stream().map(new Function<Author, List<Book>>() {
            @Override
            public List<Book> apply(Author author) {
                return author.getBooks();
            }
        }).forEach(new Consumer<List<Book>>() {
            @Override
            public void accept(List<Book> list) {
                System.out.println("集合列表");
                System.out.println(list);
            }
        });

        authors.stream().flatMap((Function<Author, Stream<Book>>) author -> author.getBooks().stream()).forEach(book ->System.out.println(book));


    }

    public static List<Author> getAuthors() {
        Author author = new Author(1L, "姓名", 20, "简介", null);
        Author author2 = new Author(2L, "作者2", 22, "简介2", null);
        Author author3 = new Author(3L, "作者3", 23, "简介3", null);
        Author author4 = new Author(4L, "作者4", 24, "简介4", null);
        Author author5 = new Author(5L, "作者5", 25, "简介5", null);


        List<Book> bookList = new ArrayList<>();
        List<Book> bookList2 = new ArrayList<>();
        List<Book> bookList3 = new ArrayList<>();
        List<Book> bookList4 = new ArrayList<>();

        bookList.add(new Book(11L, "书名11", 85, "分类1", "第11本书的内容"));
        bookList.add(new Book(12L, "书名12", 87, "分类1", "第12本书的内容"));
        bookList.add(new Book(13L, "书名13", 88, "分类1", "第13本书的内容"));

        bookList2.add(new Book(21L, "书名21", 285, "分类2", "第21本书的内容"));
        bookList2.add(new Book(22L, "书名22", 287, "分类2", "第22本书的内容"));
        bookList2.add(new Book(23L, "书名23", 288, "分类2", "第23本书的内容"));


        bookList3.add(new Book(31L, "书名31", 385, "分类3", "第31本书的内容"));
        bookList3.add(new Book(32L, "书名32", 387, "分类3", "第32本书的内容"));
        bookList3.add(new Book(33L, "书名33", 388, "分类3", "第33本书的内容"));

        bookList4.add(new Book(41L, "书名31", 485, "分类4", "第41本书的内容"));
        bookList4.add(new Book(42L, "书名32", 487, "分类4", "第42本书的内容"));
        bookList4.add(new Book(43L, "书名33", 488, "分类4", "第43本书的内容"));

        author.setBooks(bookList);
        author2.setBooks(bookList2);
        author3.setBooks(bookList3);
        author4.setBooks(bookList4);

        List<Author> authorList = new ArrayList<>(Arrays.asList(author, author2, author3, author4));
        return authorList;
    }


}
