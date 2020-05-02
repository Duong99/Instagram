package com.example.instagram.model;

public class Person {
    private String idPerson;
    private String username;
    private String fullName;
    private String imvPeron;


    public Person(String idPerson, String username, String fullName, String imvPeron) {
        this.idPerson = idPerson;
        this.username = username;
        this.fullName = fullName;
        this.imvPeron = imvPeron;
    }

    public Person(String idPerson, String fullName, String imvPeron) {
        this.idPerson = idPerson;
        this.fullName = fullName;
        this.imvPeron = imvPeron;
    }

    public String getIdPerson() {
        return idPerson;
    }

    public String getFullName() {
        return fullName;
    }

    public String getImvPeron() {
        return imvPeron;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person){
            Person p = new Person(((Person) obj).getIdPerson(), ((Person) obj).getUsername(),
                    ((Person) obj).getFullName(), ((Person) obj).getImvPeron());

            if (p.getIdPerson() == this.idPerson){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
}
