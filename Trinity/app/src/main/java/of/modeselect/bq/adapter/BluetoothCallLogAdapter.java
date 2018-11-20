package of.modeselect.bq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import of.modeselect.bq.R;
import of.modeselect.bq.bean.CallLog;

import java.util.List;

public class BluetoothCallLogAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<CallLog> callLog;

    public BluetoothCallLogAdapter(Context context, List<CallLog> callLog) {

        this.mInflater = LayoutInflater.from(context);
        this.callLog = callLog;
    }

    @Override
    public int getCount() {
        return callLog.size();
    }

    @Override
    public Object getItem(int position) {
        return callLog.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.bluetooth_calllog_item, parent, false);
            holder = new ViewHolder();

            holder.name = convertView.findViewById(R.id.calllog_name);
            holder.type = convertView.findViewById(R.id.calllog_type);
            holder.timestamp = convertView.findViewById(R.id.calllog_timestamp);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        CallLog callLog = this.callLog.get(position);
        holder.name.setText(callLog.getName());
        holder.type.setText(callLog.getType());
        holder.timestamp.setText(callLog.getTimestamp());

        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView type;
        TextView timestamp;
    }

}