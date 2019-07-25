/*
 *  Copyright (C) 2017 OrionStar Technology Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.ainirobot.sdk_demo.model.bean;

import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

public class PersonInfo {
    private static final String SHORT_REGEX = "^(R|L)(-)(.+)(-)(.+)?";

    private String registerName;
    private String name;
    private String id;
    private String gender;
    private int age;
    private int type;
    private int role;
    private int faceId;

    private PersonInfo(Builder builder) {
        this.registerName = builder.registerName;
        this.name = builder.name;
        this.id = builder.id;
        this.gender = builder.gender;
        this.age = builder.age;
        this.type = builder.type;
        this.role = builder.role;
        this.faceId = builder.faceId;
    }

    private String getShortName(String name) {
        if (matches(SHORT_REGEX, name)) {
            Pattern pattern = compile("[^-]+$");
            Matcher matcher = pattern.matcher(name);
            if (matcher.find()) {
                return matcher.group();
            }
            return name;
        }
        return name;
    }

    public String getRegisterName() {
        return registerName;
    }

    public String getName() {
//        if (TextUtils.isEmpty(name) && !TextUtils.isEmpty(registerName))
//            return getShortName(registerName);
        return name;
    }

    public String getId() {
        return id;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public int getRole() {
        return role;
    }

    public int getType() {
        return type;
    }

    public int getFaceId() {
        return faceId;
    }

    @Override
    public String toString() {
        return "id:" + id + ", name:" + name + ", role:" + role + ", type:" + type + ", registerName:" + registerName;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static class Builder {
        private String registerName;
        private String name;
        private String id;
        private String gender;
        private int age;
        private int type;
        private int role;
        private int faceId;

        public Builder registerName(String registerName) {
            this.registerName = registerName;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public Builder role(int role) {
            this.role = role;
            return this;
        }

        public Builder faceId(int faceId) {
            this.faceId = faceId;
            return this;
        }

        public PersonInfo build() {
            return new PersonInfo(this);
        }
    }
}
