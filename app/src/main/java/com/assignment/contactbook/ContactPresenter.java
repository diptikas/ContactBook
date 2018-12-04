package com.assignment.contactbook;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContactPresenter {

    private final Context context;

    public ContactPresenter(Context c) {
        this.context = c;
    }

    public ArrayList<Contact> fetchAllContacts() {
        String[] projectionFields = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
        };
        ArrayList<Contact> listContacts = new ArrayList<>();
        CursorLoader cursorLoader = new CursorLoader(context,
                ContactsContract.Contacts.CONTENT_URI,
                projectionFields, // the columns to retrieve
                null, // the selection criteria (none)
                null, // the selection args (none)
                null // the sort order (default)
        );

        Cursor c = cursorLoader.loadInBackground();

        final Map<String, Contact> contactsMap = new HashMap<>(c.getCount());

        if (c.moveToFirst()) {

            int idIndex = c.getColumnIndex(ContactsContract.Contacts._ID);
            int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

            do {
                String contactId = c.getString(idIndex);
                String contactDisplayName = c.getString(nameIndex);
                Contact contact = new Contact(contactId, contactDisplayName);
                contactsMap.put(contactId, contact);
                listContacts.add(contact);
            } while (c.moveToNext());
        }

        c.close();

        listContacts = matchContactNumbers(contactsMap);

        return listContacts;
    }

    public ArrayList<Contact> matchContactNumbers(Map<String, Contact> contactsMap) {
        ArrayList<Contact> contacts = new ArrayList<>();
        // Get numbers
        final String[] numberProjection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        };

        Cursor phone = new CursorLoader(context,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                numberProjection,
                null,
                null,
                null).loadInBackground();

        if (phone.moveToFirst()) {
            final int contactNumberColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            final int contactTypeColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
            final int contactIdColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);

            Set<String> uniqueNumbers = new HashSet<>();
            while (!phone.isAfterLast()) {
                final String number = phone.getString(contactNumberColumnIndex);

                //removing duplicate contacts
                String number1 = number.replaceAll("[^0-9]", "");
                if (number1.startsWith("0")) {
                    number1 = number1.substring(1, number1.length());
                } else if (number1.startsWith("91") && number1.length() == 12) {
                    number1 = number1.substring(2, number1.length());
                }
                Log.d("number1: ", number1);
                final String contactId = phone.getString(contactIdColumnIndex);
                Contact contact = contactsMap.get(contactId);
                if (contact == null) {
                    continue;
                }
                final int type = phone.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                CharSequence phoneType = ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.getResources(), type, customLabel);
                //Log.d("number2", number1);
                if (!uniqueNumbers.contains(number1)) {
                    Log.d("added number:", number1 + ":" + number);
                    contact.addNumber(number, phoneType.toString());
                    contacts.add(contact);
                }
                uniqueNumbers.add(number1);
                phone.moveToNext();
            }
        }

        phone.close();
        return contacts;
    }


}
