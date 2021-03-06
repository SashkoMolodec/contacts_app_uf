package com.example.contacts_app;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView textView;
    FloatingActionButton f_button_add;
    RadioGroup rg;
    RadioButton rb_name, rb_recently, rb_surname;

    int ID_contactPage = 1;
    int ID_addContact = 2;
    static final int DELETE_CONTACT_RES = 3;
    static final int UPDATE_CONTACT_RES = 4;

    ContactsAdapter ad;

    DBContactsHelper dbHelper;
    static SQLiteDatabase database;
    static ArrayList<Contact> contactsForList;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final byte[] bitmapSample = Contact.getBytesFromDrawable(getDrawable(R.drawable.sample));

        listView = findViewById(R.id.contacts_list);
        textView = findViewById(R.id.displayContact);
        f_button_add = findViewById(R.id.fab_add);

        rg = findViewById(R.id.radio_group);
        rb_name = findViewById(R.id.radio_name);
        rb_recently = findViewById(R.id.radio_recently);
        rb_surname = findViewById(R.id.radio_surname);



       contactsForList = new ArrayList<Contact>();


        dbHelper = DBContactsHelper.getInstance(this);
        contactsForList = dbHelper.getObjectListFromDB();

        ad = new ContactsAdapter(this, contactsForList);


        listView.setAdapter(ad);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact onClickedContact = contactsForList.get(position);

                Log.v("mLog","position in view: "+ position);

                Intent i = new Intent(MainActivity.this, ContactPage.class);
                i.putExtra(Contact.class.getSimpleName(),onClickedContact);
                startActivityForResult(i, ID_contactPage);

            }
        });
        f_button_add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(MainActivity.this, ContactAddActivity.class);
                //startActivityForResult(i, ID_addContact);
                //nameInput.setHint("Enter name");
               // surnameInput.setHint("Enter surname");
               // LinearLayout layout = new LinearLayout(MainActivity.this);
                //layout.setOrientation(LinearLayout.VERTICAL);
                //layout.addView(nameInput); layout.addView(surnameInput);

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                final View viewDialog = inflater.inflate(R.layout.add_contact_dialog,null);

                final EditText nameInput = (EditText) viewDialog.findViewById(R.id.edit_name);
                final EditText surnameInput = (EditText) viewDialog.findViewById(R.id.edit_surname);
                final EditText numberInput = (EditText) viewDialog.findViewById(R.id.edit_number);

                    builder
                        .setView(viewDialog)
                        .setTitle("Add contact")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper = DBContactsHelper.getInstance(MainActivity.this);

                                String name = nameInput.getText().toString();
                                String surname = surnameInput.getText().toString();
                                String number = numberInput.getText().toString();
                                Log.v("mLog",name + " " + surname + " " + number);
                                Contact c = new Contact(name,surname,number);
                                c.setId(dbHelper.getMaximumID()+1);
                                c.setAvatar(bitmapSample);
                                dbHelper.writeContactToDB(c);
                                contactsForList.add(c);
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();




            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.radio_name:
                        Log.v("radioButton","Name RadioButton");
                        //Collections.sort(contactsForList, new Contact.ComparatorByName());
                        //ArrayList<String>
                        //Collections.sort(contactsForList);
                        //contactsForList.sort(new Contact.ComparatorByName());
                        break;
                    case R.id.radio_surname:
                        Log.v("radioButton","Surname RadioButton");
                        break;
                    case R.id.radio_recently:
                        Log.v("radioButton","Recently RadioButton");

                       /* Collections.sort(contacts, new Comparator<Contact>(){
                            @Override
                            public int compare(Contact o1, Contact o2) {
                                return 0;
                            }
                        });*/
                        contactsForList.clear();
                       // contactsForList.addAll(Contact.getAllNames(contacts));
                        break;
                }
                ad.notifyDataSetChanged();
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

                dbHelper.logDB();
                ad.updateAdapter();
            }

            }
        if(resultCode == DELETE_CONTACT_RES){
            if (requestCode == ID_contactPage) {
                Bundle extras = data.getExtras();
                Contact c = extras.getParcelable(Contact.class.getSimpleName());

                Contact orig_c = Contact.getOrginialContactByID(contactsForList,c.getId());

                Log.v("mLog","deleteCont -> c == orig_c: "+ (c==orig_c)); //different references
                Log.v("mLog","deleteCont -> id == orig_id: "+ (c.getId()==orig_c.getId()));
                Log.v("mLog","index contact in list " + contactsForList.indexOf(orig_c));

                contactsForList.remove(orig_c);

                database = dbHelper.getWritableDatabase();
                database.delete(DBContactsHelper.TABLE_CONTACTS,"_id = " +orig_c.getId(),null);
                database.close();
            }
        }
        if(resultCode == UPDATE_CONTACT_RES){
            if(requestCode == ID_contactPage){
                Bundle extras = data.getExtras();
                Contact c = extras.getParcelable(Contact.class.getSimpleName());

                ContentValues cv = new ContentValues();
                cv.put(DBContactsHelper.KEY_NUMBER,c.getNumber());
                cv.put(DBContactsHelper.KEY_SURNAME,c.getSurname());
                cv.put(DBContactsHelper.KEY_NAME,c.getName());
                database = dbHelper.getWritableDatabase();
                database.update(DBContactsHelper.TABLE_CONTACTS,cv,"_id = " + c.getId(),null);
                database.close();
            }
        }
        listView.setAdapter(ad);
    }
}

