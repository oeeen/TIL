package dev.smjeon.til.serialize;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    void serialize() {
        Member member = new Member("Martin", "oeeen3@gmail.com", 21);
        byte[] serializedMember;

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
                objectOutputStream.writeObject(member);
                serializedMember = byteArrayOutputStream.toByteArray();
                System.out.println(Base64.getEncoder().encodeToString(serializedMember));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}