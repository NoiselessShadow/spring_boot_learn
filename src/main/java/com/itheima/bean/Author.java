package com.itheima.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author  implements Comparable<Author> {
    private Long id;
    private String name;
    private Integer age;
    private String intro;
    private List<Book> books;

    @Override
    public int compareTo(Author o) {
        return  o.getAge() -this.age;
    }
}
