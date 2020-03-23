package com.zxy.demo;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class EntrySerializerTest {

    public static void main(String[] args) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //注册适配器
        gsonBuilder.registerTypeAdapter(Entry.class,new EntrySerializer())
                .setPrettyPrinting();//对结果格式化一下
        Gson gson = gsonBuilder.create();
        //我们去注册时，通常名字由两部分组成，fistName,lastName
        Entry entry = new Entry("xia","liang");
        User user = new User();
        user.setUserName(entry);
        user.setUserPwd("xl123");
        user.setAge(24);

        //序列化
        String json = gson.toJson(user);
        System.out.println("序列化结果：\n"+json);
        //反序列化
        user=gson.fromJson(json,User.class);
        System.out.println("反序列化结果：\n"+user);
    }


    public static class User{

        private Entry userName;
        private String userPwd;
        private int age;

        public Entry getUserName() {
            return userName;
        }

        public void setUserName(Entry userName) {
            this.userName = userName;
        }

        public String getUserPwd() {
            return userPwd;
        }

        public void setUserPwd(String userPwd) {
            this.userPwd = userPwd;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "userName=" + userName +
                    ", userPwd='" + userPwd + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    public static class Entry {
        private String firstName;
        private String lastName;

        public Entry(String firstName, String lastName) {
            super();
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        @Override
        public String toString() {
            return "Entry [firstName=" + firstName + ", lastName=" + lastName + "]";
        }
    }

    public static class EntrySerializer implements JsonSerializer<Entry>, JsonDeserializer<Entry> {
        /**
         * 序列化时调用
         */
        @Override
        public JsonElement serialize(Entry entry, Type typeOfSrc,
                                     JsonSerializationContext context) {
            if(entry == null){
                return null;
            }
            /*
             * 看下JsonElement类的实现类，
             * 他可以将entry转换为不同的类型例如数组，对象；
             * 我这里的需求是将firtName和lastName转换为firtName-lastName的格式，用下面这个实现类
             */
            JsonElement json = new JsonPrimitive(entry.getFirstName()+"-"+entry.getLastName());
            return json;
        }

        /**
         * 反序列化时调用
         */
        @Override
        public Entry deserialize(JsonElement json, Type typeOfT,
                                 JsonDeserializationContext context) throws JsonParseException {
            String[] names=json.toString().split("-");
            String firstName=names[0];
            String lastName=names[1];
            Entry entry = new Entry(firstName, lastName);
            return entry;
        }

    }
}
