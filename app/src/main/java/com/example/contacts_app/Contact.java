package com.example.contacts_app;

import java.util.ArrayList;
import java.util.Comparator;

public class Contact {
    private int id;
    private String name, number;


    public Contact(int id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
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
