package of.modeselect.bq.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import of.modeselect.bq.R;
import of.modeselect.bq.adapter.BluetoothCallLogAdapter;
import of.modeselect.bq.bean.CallLog;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CallLogFragment extends Fragment {
    private static final String TAG = "BT.CallLogFragment";

    private ListView contactsListView;
    private BluetoothCallLogAdapter callLogAdapter;
    private List<CallLog> callLogBeanList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.bluetooth_calllog, container, false);
        initViews(view);
        return view;
    }


    private void initViews(View view) {
        contactsListView = view.findViewById(R.id.calllog_list_view);
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // FixMe: Show left view with contact details.
            }
        });

        callLogBeanList = getCallLogList();
        callLogAdapter = new BluetoothCallLogAdapter(getContext(), callLogBeanList);
        contactsListView.setAdapter(callLogAdapter);
    }

    public static String getFormatedDateTime(long dateTime) {
        SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(new Date(dateTime));
        return localTime;
    }

    public List<CallLog> getCallLogList() {
        List<CallLog> list = new ArrayList<CallLog>();

        Cursor cursor = getContext().getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI, null, null, null, android.provider.CallLog.Calls.DEFAULT_SORT_ORDER);

        if ((cursor == null) || (cursor.getCount() <= 0)) {
            return null;
        }
        Log.d(TAG, "Count: " + cursor.getCount());

        while (cursor.moveToNext()) {
            CallLog call = new CallLog();

            /* Reading Number */
            call.setNumber(cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER)));
            Log.d(TAG, "NUMBER: " + cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER)));

            /* Reading Name */
            String name = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
            Log.d(TAG, "Cached Name: " + cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME)));
            if ((name == null) || (name.length() == 0)) {
                call.setName(call.getNumber());
            }
            else {
                call.setName(name);
            }

            /* Reading Date */
            /* cursor.getColumnIndex(CallLog.Calls.DATE); */
            //String date= DateUtil.timedate(cursor.getString(DATE));
            call.setTimestamp(getFormatedDateTime(cursor.getLong(cursor.getColumnIndex(android.provider.CallLog.Calls.DATE))));
            Log.d(TAG, "DATE: " + getFormatedDateTime(cursor.getLong(cursor.getColumnIndex(android.provider.CallLog.Calls.DATE))));

            /* Reading duration */
            /* cursor.getColumnIndex(CallLog.Calls.DURATION); */
            call.setDuration(cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.DURATION))+"秒");

            /* Reading Date */
            /* cursor.getColumnIndex(CallLog.Calls.TYPE); */
            int type=cursor.getInt(cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE));
            String result="";
            switch (type) {
                case android.provider.CallLog.Calls.INCOMING_TYPE:
                    result = "已接";
                    break;
                case android.provider.CallLog.Calls.OUTGOING_TYPE:
                    result = "已拨";
                    break;
                case android.provider.CallLog.Calls.MISSED_TYPE:
                    result = "未接";
                    break;
                default:
                    break;
            }
            call.setType(result);
            Log.d(TAG, "TYPE: " + result);

            list.add(call);
        }
        cursor.close();
        return list;
    }

}
