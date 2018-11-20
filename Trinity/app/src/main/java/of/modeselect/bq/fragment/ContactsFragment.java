package of.modeselect.bq.fragment;

import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import of.modeselect.bq.R;
import of.modeselect.bq.adapter.BluetoothContactAdapter;
import of.modeselect.bq.bean.Contact;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {

    private static final String TAG = "BT.ContactsFragment";

    private static final String[] PHONES_PROJECTION = new String[] {
                Phone.DISPLAY_NAME,
                Phone.NUMBER,
                Photo.PHOTO_ID,
                Phone.CONTACT_ID
    };
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    private static final int PHONES_NUMBER_INDEX       = 1;
    private static final int PHONES_PHOTO_ID_INDEX     = 2;
    private static final int PHONES_CONTACT_ID_INDEX   = 3;

    private ListView contactsListView;
    private BluetoothContactAdapter contactAdapter;
    private List<Contact> contactList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.bluetooth_contacts, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        contactsListView = view.findViewById(R.id.contacts_list_view);
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // FixMe: Show left view with contact details.
            }
        });

        contactList = getContactList();
        contactAdapter = new BluetoothContactAdapter(getContext(), contactList);
        contactsListView.setAdapter(contactAdapter);
    }

    public List<Contact> getContactList() {
        List<Contact> list = new ArrayList<Contact>();

        Cursor cursor = getContext().getContentResolver().query(Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);

        if ((cursor == null) || (cursor.getCount() <= 0)) {
            return null;
        }
        Log.d(TAG, "Count: " + cursor.getCount());

        while (cursor.moveToNext()) {

            String phoneNumber = cursor.getString(PHONES_NUMBER_INDEX);
            String contactName = cursor.getString(PHONES_DISPLAY_NAME_INDEX);
            Long contactid = cursor.getLong(PHONES_CONTACT_ID_INDEX);
            Long photoid = cursor.getLong(PHONES_PHOTO_ID_INDEX);

            Bitmap contactPhoto = null;
            if(photoid > 0 ) {
                Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactid);
                InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getContext().getContentResolver(), uri);
                contactPhoto = BitmapFactory.decodeStream(input);
            }
            else {
                contactPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.bt_contact_default_avatar);
            }

            list.add(new Contact(contactName, phoneNumber, contactPhoto));
        }
        cursor.close();
        return list;
    }

}
