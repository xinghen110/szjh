package com.magustek.szjh;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestStream {
    public static void main(String[] args) {

        A a = new A();
        a.setId(1);
        a.setAge(22);
        a.setName("a");

        A a1 = new A();
        a1.setId(2);
        a1.setAge(22);
        a1.setName("a1");

        A a2 = new A();
        a2.setId(3);
        a2.setAge(22);
        a2.setName("a2");

        A a3 = new A();
        a3.setId(4);
        a3.setAge(22);
        a3.setName("a3");

        List<A> list = new ArrayList<>();
        list.add(a3);
        list.add(a2);
        list.add(a1);
        list.add(a);

        List<Integer> list1 = list.stream().map(A::getId).collect(Collectors.toList());
        List<Integer> list2 = list.stream().map(A::getAge).collect(Collectors.toList());//未去重
        List<Integer> list3 = list.stream().map(A::getAge).distinct().collect(Collectors.toList());//已去重
        System.out.println(list1);
        System.out.println(list2);
        System.out.println(list3);
    }

    static class A{
        private Integer id;
        private Integer age;
        private String name;
        public Integer getId() {
            return id;
        }
        public void setId(Integer id) {
            this.id = id;
        }
        public Integer getAge() {
            return age;
        }
        public void setAge(Integer age) {
            this.age = age;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

    static void b(){
        //Mono
    }
}
