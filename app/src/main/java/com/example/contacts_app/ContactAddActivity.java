package com.example.contacts_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.contacts_app.MainActivity.database;
import static com.example.contacts_app.MainActivity.contactsForList;
import androidx.appcompat.app.AppCompatActivity;

public class ContactAddActivity extends AppCompatActivity {
    EditText name_input, number_input, surname_input;
    Button btn_add;
    DBContactsHelper dbHelper = new DBContactsHelper(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        number_input = findViewById(R.id.inputNumber);
        name_input = findViewById(R.id.inputName);
        surname_input = findViewById(R.id.inputSurname);
        btn_add = findViewById(R.id.add_contact_ok_button);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name_input.getText().length()!=0){
                    String name = name_input.getText().toString();
                    String surname = surname_input.getText().toString();
                    String number = number_input.getText().toString();

                    database = dbHelper.getWritableDatabase();
                    Contact c = new Contact(dbHelper.getContactsCount()+1,name,surname,number);
                    dbHelper.writeContactToDB(database, c);
                    dbHelper.logDB(database);

                    contactsForList.add(name);
                    /*
                    i.putExtra("NAME",name_input.getText()+"");
                    i.putExtra("SURNAME", surname_input.getText()+"");
                    i.putExtra("NUMBER",number_input.getText()+"");

                */ Intent i = new Intent();
                    setResult(RESULT_OK,i);
                    finish();
                }
                else{
                    Toast badInput = Toast.makeText(getApplicationContext(), "Input some name", Toast.LENGTH_SHORT);
                    badInput.show();
                }
            }
        });


    }
}
