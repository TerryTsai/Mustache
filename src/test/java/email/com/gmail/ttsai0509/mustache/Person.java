package email.com.gmail.ttsai0509.mustache;

import java.util.*;

class Person {

    String name;
    int age;
    String address;
    List<Map<String, String>> notes;

    String firstname;
    String lastname;

    Person(String name, int age, String address, String... notes) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.notes = new ArrayList<>();
        for (String note : notes)
            this.notes.add(Collections.singletonMap("note", note));
    }

    Person(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    String fullname(String firstname, String lastname) {
        return firstname + " " + lastname;
    }
}
