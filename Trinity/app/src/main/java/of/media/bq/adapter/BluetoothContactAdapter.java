package of.media.bq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import of.media.bq.R;
import of.media.bq.bean.Contact;

import java.util.List;

public class BluetoothContactAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Contact> contacts;

    public BluetoothContactAdapter(Context context, List<Contact> contacts) {

        this.mInflater = LayoutInflater.from(context);
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.bluetooth_contacts_item, parent, false);
            holder = new ViewHolder();

            holder.avatar = convertView.findViewById(R.id.contact_avatar);
            holder.name = convertView.findViewById(R.id.contact_name);
            holder.phone = convertView.findViewById(R.id.contact_phone);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Contact contact = contacts.get(position);
        holder.avatar.setImageBitmap(contact.getPhoto());
        holder.name.setText(contact.getName());
        holder.phone.setText(contact.getNumber());

        return convertView;
    }

    private class ViewHolder {
        ImageView avatar;
        TextView name;
        TextView phone;
    }

}