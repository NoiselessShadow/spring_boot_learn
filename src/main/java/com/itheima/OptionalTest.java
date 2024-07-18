package com.itheima;

import com.itheima.bean.Author;

import java.util.Optional;
import java.util.function.Supplier;

public class OptionalTest {

    public static void main(String[] args) {
        Author author = getAuthor();
        Optional<Author> author1 = Optional.ofNullable(author);
//        author1.ifPresent(item->System.out.println(item.getName()));
//
//        //安全的获取值，有值就返回，若没值则使用我们默认设定的值
//        Author author2 = author1.orElseGet(() -> new Author(2L,"姓名",21,"简历",null));
//        System.out.println(author2);
//
//
//        try {
//            Author author3 = author1.orElseThrow(new Supplier<Throwable>() {
//                @Override
//                public Throwable get() {
//                    return new RuntimeException("当为空时返回异常！");
//                }
//            });
//        } catch (Throwable e) {
//            throw new RuntimeException("orElseThrow  方法本身需要catch");
//        }
//
//        author1.orElseThrow(()->new RuntimeException("当为空时返回异常！"));

        author1.filter(item -> item.getAge()>2).ifPresent(System.out::println);

        author1.map(item -> item.getName()).ifPresent(System.out::println);

    }

   public static Author getAuthor(){
        Author author = new Author(1L,"姓名",11,"简历",null);

        return author;
   }
}
