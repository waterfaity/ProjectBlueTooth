package com.waterfairy.tool.databinding;

/**
 * Created by water_fairy on 2017/2/20.
 */

public class User {
    private String name;
    private int age;

    public User(String xiaoming) {
        this.name=xiaoming;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
