package com.itheima;

import com.itheima.bean.Author;

public class TestPoJO {

    public static void main(String[] args) {
        Author author = new Author(1L, "姓名", 20, "简介", null);
        setParams(author);
        System.out.println(author.toString());
    }



    public static void setParams(Author author){
//        Author author = new Author(1L, "姓名", 20, "简介", null);
        author = new Author(2L, "作者2", 22, "简介2", null);
        author.setAge(313);
//        Author author3 = new Author(3L, "作者3", 23, "简介3", null);
//        Author author4 = new Author(4L, "作者4", 24, "简介4", null);
//        Author author5 = new Author(5L, "作者5", 25, "简介5", null);
    }
}
