package com.example.contacts_app;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class Contact implements Parcelable {
    private int id;
    private String name;
    private String surname;
    private String number;
    private byte[] avatar;

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\''+
                ", number='" + number + '\'' +
                ", avatar='" + avatar + '\'' + '}';
    }



    public Contact(int id, String name, String surname, String number, byte[] avatar) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.number = number;
        this.avatar = avatar;

    }

    public Contact(String name, String surname,  String number) {
        this.name = name;
        this.surname = surname;
        this.number = number;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
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


    public static Contact getOrginialContactByID(ArrayList<Contact> c, int id){
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

        dest.writeInt(avatar.length);
        dest.writeByteArray(avatar);
    }

    protected Contact(Parcel in) {
        id = in.readInt();
        name = in.readString();
        surname = in.readString();
        number = in.readString();
        avatar = new byte[in.readInt()];
        in.readByteArray(avatar);

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

    public static class ComparatorByName implements Comparator<Contact>{
        @Override
        public int compare(Contact c1, Contact c2) {
            return c1.getName().compareTo(c2.getName());
        }
    }
    public static class ComparatorById implements Comparator<Contact>{
        @Override
        public int compare(Contact c1, Contact c2) {
            return c2.getId() - c1.getId();
        }
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static byte[] getBytesFromDrawable(Drawable d){
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapSample = stream.toByteArray();
        return bitmapSample;
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

}
