package com.example.contacts_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.contacts_app.MainActivity.database;
import static com.example.contacts_app.MainActivity.contactsForList;
//import static com.example.contacts_app.MainActivity.ad;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class ContactAddActivity extends AppCompatActivity {
    EditText name_input, number_input, surname_input;
    Button btn_add;
    DBContactsHelper dbHelper;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        number_input = findViewById(R.id.inputNumber);
        name_input = findViewById(R.id.inputName);
        surname_input = findViewById(R.id.inputSurname);
        btn_add = findViewById(R.id.add_contact_ok_button);

        dbHelper = DBContactsHelper.getInstance(this);



        final byte[] bitmapSample = Contact.getBytesFromDrawable(getDrawable(R.drawable.sample));
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name_input.getText().length()!=0){

                    String name = name_input.getText().toString();
                    String surname = surname_input.getText().toString();
                    String number = number_input.getText().toString();

                    Contact c = new Contact(name,surname,number);
                    c.setId(dbHelper.getMaximumID()+1);
                    c.setAvatar(bitmapSample);
                    dbHelper.writeContactToDB(c);
                    contactsForList.add(c);

                    Log.v("mLog","max id = " + dbHelper.getMaximumID());
                    setResult(RESULT_OK);
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
