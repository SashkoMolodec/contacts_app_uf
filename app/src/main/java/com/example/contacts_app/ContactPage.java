package com.example.contacts_app;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static com.example.contacts_app.MainActivity.UPDATE_CONTACT_RES;
//import static com.example.contacts_app.MainActivity.ad;
import static com.example.contacts_app.MainActivity.contactsForList;
import static com.example.contacts_app.MainActivity.database;
import static com.example.contacts_app.MainActivity.DELETE_CONTACT_RES;

public class ContactPage extends AppCompatActivity {
    TextView c_ID;
    EditText c_Name, c_Number, c_Surname;
    Button btn_delete;
    Button btn_update,btn_confirm;
    ImageView imgAva;
    Contact contactOrig;
    static final int PICK_IMAGE = 1;

    DBContactsHelper dbHelper;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        btn_update = findViewById(R.id.edit_btn);
        btn_confirm = findViewById(R.id.confirm_btn);

        c_Surname = findViewById(R.id.contactSurname);
        c_ID = findViewById(R.id.contactID);
        c_Name = findViewById(R.id.contactName);
        c_Number = findViewById(R.id.contactNumber);
        btn_delete = findViewById(R.id.delete_btn);
        imgAva = findViewById(R.id.img_avatar);

        c_Name.setInputType(InputType.TYPE_NULL);
        c_Number.setInputType(InputType.TYPE_NULL);
        c_Surname.setInputType(InputType.TYPE_NULL);
        btn_confirm.setVisibility(View.INVISIBLE);

        //Extracting bundle
        Bundle extras = getIntent().getExtras();
        Contact contactCopy = (Contact) extras.getParcelable(Contact.class.getSimpleName());
        contactOrig = Contact.getOrginialContactByID(contactsForList,contactCopy.getId());

        c_ID.setText(contactOrig.getId()+"");
        c_Number.setText(contactOrig.getNumber());
        c_Name.setText(contactOrig.getName());
        c_Surname.setText(contactOrig.getSurname());

        imgAva.setImageBitmap(BitmapFactory.decodeByteArray(contactOrig.getAvatar(),
                    0, contactOrig.getAvatar().length));
            Log.v("mLog","c.getAvatar() is not null!");



        Log.v("id in contactPage",c_ID.getText().toString());

        dbHelper = DBContactsHelper.getInstance(this);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra(Contact.class.getSimpleName(),contactOrig);
                setResult(DELETE_CONTACT_RES,i);
                finish();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c_Name.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                c_Number.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                c_Surname.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                btn_confirm.setVisibility(View.VISIBLE);
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = c_Name.getText().toString();
                String newSurname = c_Surname.getText().toString();
                String newNumber = c_Number.getText().toString();

                contactOrig.setName(newName);
                contactOrig.setSurname(newSurname);
                contactOrig.setNumber(newNumber);

                Intent i = new Intent();
                i.putExtra(Contact.class.getSimpleName(),contactOrig);
                setResult(UPDATE_CONTACT_RES,i);
                finish();
            }
        });
        imgAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_IMAGE);
            }
        });


    }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == PICK_IMAGE) {
                    Uri selectedImage = data.getData();
                    Log.v("image", "IMAGE CHOSEN!");
                    imgAva.setImageURI(selectedImage);
                    InputStream iStream = null;

                    try {
                        iStream = getContentResolver().openInputStream(selectedImage);
                        byte[] inputData = Contact.getBytes(iStream);

                        contactOrig.setAvatar(inputData);
                        database = dbHelper.getWritableDatabase();
                        ContentValues cv = new ContentValues();
                        cv.put(DBContactsHelper.KEY_AVATAR, inputData);
                        database.update(DBContactsHelper.TABLE_CONTACTS,cv,"_id = " + contactOrig.getId(),null);
                        database.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
