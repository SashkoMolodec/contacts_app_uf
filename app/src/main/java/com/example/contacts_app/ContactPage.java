package com.example.contacts_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ContactPage extends AppCompatActivity {
    TextView c_ID;
    EditText c_Name, c_Number;
    Button btn_delete;
    Button btn_update,btn_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        btn_update = findViewById(R.id.edit_btn);
        btn_confirm = findViewById(R.id.confirm_btn);

        c_ID = findViewById(R.id.contactID);
        c_Name = findViewById(R.id.contactName);
        c_Number = findViewById(R.id.contactNumber);
        btn_delete = findViewById(R.id.delete_btn);

        c_Name.setInputType(InputType.TYPE_NULL);
        c_Number.setInputType(InputType.TYPE_NULL);
        btn_confirm.setVisibility(View.INVISIBLE);

        //Extracting bundle
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("NAME");
        String number = extras.getString("NUMBER");
        String id = extras.getInt("ID_CONTACT") + "";
        Log.v("somee",name + " " + id);
        c_ID.setText(id);
        c_Number.setText(number);
        c_Name.setText(name);
        Log.v("id in contactPage",c_ID.getText().toString());

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("btn_pressed","btn_delete");
                i.putExtra("ID", Integer.parseInt(c_ID.getText().toString()));
                setResult(RESULT_OK,i);
                finish();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c_Name.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                c_Number.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                btn_confirm.setVisibility(View.VISIBLE);
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("btn_pressed","btn_update");
                i.putExtra("ID", Integer.parseInt(c_ID.getText().toString()));
                i.putExtra("NAME",c_Name.getText()+"");
                i.putExtra("NUMBER",c_Number.getText()+"");
                setResult(RESULT_OK,i);
                finish();
            }
        });

    }
}
