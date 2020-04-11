package com.example.contacts_app;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class Contact implements Parcelable {
    private int id;
    private String name;
    private String number;
    private String surname;

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }



    public Contact(int id, String name, String surname, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.surname = surname;

    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public static ArrayList<String> getAllNames(ArrayList<Contact> c){
        ArrayList<String> res = new ArrayList<>();
        for(Contact temp:c){
            res.add(temp.getName());
        }
        return res;
    }

    public static int getIDbyListName(ArrayList<Contact> c, String name){
        // Проганяємо через цикл усі контакти і порівнюємо шукане ім'я з іменем
        // кожного об'єкту
        for(Contact temp:c){
            if(temp.getName().equals(name))
                return temp.getId(); // Вертаємо айді цього об'єкту
        }
        return 0; // Якщо нічого не знайде, то поверне 0
    }

    public static Contact getContactByID(ArrayList<Contact> c, int id){
        // Проганяємо через цикл усі контакти і шукаємо наший об'єкт
        // контакту за допомогою айдішки (Тобто мають дві збігатись)
        for(Contact temp:c){
            if(temp.getId() == id)
                return temp; // вертаємо об'єкт контакту
        }
        return null; // якщо нічого не знайде, то вертаєм ніц
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(number);
    }

    protected Contact(Parcel in) {
        id = in.readInt();
        name = in.readString();
        number = in.readString();
        surname = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };




    public static class ComparatorByName implements Comparator<String>{
        @Override
        public int compare(String c1, String c2) {
            return c1.compareTo(c2);
        }
    }
    public static class ComparatorById implements Comparator<Contact>{
        @Override
        public int compare(Contact c1, Contact c2) {
            return c2.getId() - c1.getId();
        }
    }


}
