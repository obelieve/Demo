package com.zxy.demo.test;

import com.google.gson.Gson;

public class Test {

    public static void main(String[] args) {
        Gson gson = new Gson();
        User user = gson.fromJson("{'name':'你','age':12}",User.class);
        System.out.println(user);
    }


    public static class User{

        @Override
        public String toString() {
            return "User{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    '}';
        }

        private int age;
        private String name;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
