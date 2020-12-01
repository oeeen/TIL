package dev.smjeon.til.serialize;

import java.io.Serializable;

public class Member {
    private String name;
    private String email;
    private int age;

    public Member(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
}
