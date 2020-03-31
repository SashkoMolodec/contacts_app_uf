package com.example.contacts_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView textView;
    FloatingActionButton f_button_add;

    int ID_contactPage = 1;
    int ID_addContact = 2;

    ArrayList<Contact> contacts; // List = ArrayList

    ArrayList<String> contactsForList;
    ArrayAdapter<String> ad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.contacts_list);
        textView = findViewById(R.id.displayContact);
        f_button_add = findViewById(R.id.fab_add);

        contacts = new ArrayList<>();
        contacts.add(new Contact(1, "Sasha", "+3580545"));
        contacts.add(new Contact(2, "Andreiko", "+3809285828"));
        contacts.add(new Contact(3, "Sonya", "+3222002545"));
        contacts.add(new Contact(4, "Volodya", "+785454555"));


        contactsForList = Contact.getAllNames(contacts);

        ad = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, contactsForList);


        listView.setAdapter(ad);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String onClickedName = contactsForList.get(position);
                textView.setText(onClickedName);


                Intent i = new Intent(MainActivity.this, ContactPage.class);

                Log.v("some", contacts.get(position) + " " + position);

                // Ми витягуємо власну айдішку з контакту за допомогою імені контакту
                int idContact = Contact.getIDbyListName(contacts, onClickedName);

                // Отримуємо об'єкт контакту за допомогою айдішки (Ми присвоїмо той об'єкт,
                // айді якого буде дорівнювати idContact)
                Contact c = Contact.getContactByID(contacts, idContact);

                i.putExtra("NAME", c.getName());
                i.putExtra("ID_CONTACT", idContact);
                i.putExtra("NUMBER", c.getNumber());

                startActivityForResult(i, ID_contactPage);

            }
        });
        f_button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ContactAddActivity.class);
                startActivityForResult(i, ID_addContact);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            if (requestCode == ID_contactPage) {
                Toast alert = Toast.makeText(this, "Exit contact page", Toast.LENGTH_SHORT);
                alert.show();
            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == ID_addContact) {
                Bundle extras = data.getExtras();

                String name = extras.getString("NAME");
                String number = extras.getString("NUMBER");

                Contact c = new Contact(contacts.size() + 1, name, number); // Якщо ми створюємо 5-ий контакт, то
                // його айді = 4(розмір масиву)+1 = 5
                contacts.add(c);
                contactsForList.add(name);
                listView.setAdapter(ad);

            }
            if (requestCode == ID_contactPage) {
                Log.v("requestCode", "ID_contactPage");
                Bundle extras = data.getExtras();

                int id = extras.getInt("ID");

                if (extras.getString("btn_pressed").equals("btn_delete")) {

                    Log.v("id", id + "");

                    //Витягуємо контакт, які ми повинні видалити
                    Contact contact_to_delete = Contact.getContactByID(contacts, id);

                    //Видаляємо з ListView наший контакт
                    contactsForList.remove(contact_to_delete.getName());

                    //Видаляємо з самого ArrayList'а контакт
                    contacts.remove(contact_to_delete);

                    //Обновляємо ListView
                    listView.setAdapter(ad);

                } else if (extras.getString("btn_pressed").equals("btn_update")) {
                    Contact contact_to_update = Contact.getContactByID(contacts, id);
                    String newName = extras.getString("NAME");
                    String newNumber = extras.getString("NUMBER");

                    contactsForList.set(contactsForList.indexOf(contact_to_update.getName()), newName);
                    contact_to_update.setName(newName);
                    contact_to_update.setNumber(newNumber);

                    listView.setAdapter(ad);

                }


            }

        }
    }
}
