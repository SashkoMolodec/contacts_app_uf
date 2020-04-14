package com.example.contacts_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends BaseAdapter{
    private Context mContext;
    private List<Contact> contactList = new ArrayList<>();

    //public constructor
    public ContactsAdapter(Context context, ArrayList<Contact> items) {
        this.mContext = context;
        this.contactList = items;
    }

    @Override
    public int getCount() {
        return contactList.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position); //returns list item at the specified position
    }
    public Object getItem(Object object){
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.list_item, parent, false);
        }

        // get current item to be displayed
        Contact currentItem = (Contact) getItem(position);

        // get the TextView for item name and item description
        TextView nameSurname = (TextView)
                convertView.findViewById(R.id.list_name);

        //sets the text for item name and item description from the current item object
        String toDisplayElement = currentItem.getName() + " " + currentItem.getSurname();
        nameSurname.setText(toDisplayElement);
        return convertView;
    }

    public void updateAdapter() {
        notifyDataSetChanged();
    }

}
