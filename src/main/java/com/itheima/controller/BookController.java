package com.itheima.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

//Rest模式
@RestController
@RequestMapping("/books")
public class BookController {

    @GetMapping
    public String getById(){
        System.out.println("springboot is running...");
        List<String> list = new ArrayList<>();
        Stream<String> stream = list.stream();
        //函数式编程
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程启动");
            }
        });

        new Thread(()->{
            System.out.println("匿名内部类可省，一样的");
        });
        return "springboot is running...";
    }

}
