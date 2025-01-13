package com.stenmartin.secret_contacts_backend.api.dto;

public class ContactDto {
    private String name;
    private String codeName;
    private String phoneNumber;

    public ContactDto() {
    }

    public ContactDto(String name, String codeName, String phoneNumber) {
        this.name = name;
        this.codeName = codeName;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
