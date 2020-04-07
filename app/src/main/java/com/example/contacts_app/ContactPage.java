package com.example.contacts_app;

import android.app.Activity;
import android.content.Intent;
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

public class ContactPage extends AppCompatActivity {
    TextView c_ID;
    EditText c_Name, c_Number, c_Surname;
    Button btn_delete;
    Button btn_update,btn_confirm;
    ImageView imgAva;
    Contact c;
    static final int PICK_IMAGE = 1;

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
        c = (Contact) extras.getParcelable(Contact.class.getSimpleName());

        /*String name = extras.getString("NAME");
        String number = extras.getString("NUMBER");
        String id = extras.getInt("ID_CONTACT") + "";
        String surname = extras.getString("SURNAME");*/
        //Log.v("somee",name + " " + id);

        imgAva.setImageDrawable(getDrawable(R.drawable.sample));

        c_ID.setText(c.getId()+"");
        c_Number.setText(c.getNumber());
        c_Name.setText(c.getName());
        c_Surname.setText(c.getSurname());
        Log.v("id in contactPage",c_ID.getText().toString());

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("btn_pressed","btn_delete");
                i.putExtra(Contact.class.getSimpleName(),c);
                setResult(RESULT_OK,i);
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
                Intent i = new Intent();
                i.putExtra("btn_pressed","btn_update");

                c.setName(c_Name.getText().toString());
                c.setSurname(c_Surname.getText().toString());
                c.setNumber(c_Number.getText().toString());

                i.putExtra(Contact.class.getSimpleName(), c);

               /* i.putExtra("ID", Integer.parseInt(c_ID.getText().toString()));
                i.putExtra("NAME",c_Name.getText()+"");
                i.putExtra("NUMBER",c_Number.getText()+"");
                i.putExtra("SURNAME",c_Surname.getText()+"");*/
                setResult(RESULT_OK,i);
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
                    //c.setBackground(sselectedImage);
                }
            }
        }
