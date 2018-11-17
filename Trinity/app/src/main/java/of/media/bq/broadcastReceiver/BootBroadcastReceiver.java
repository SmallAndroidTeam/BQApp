package of.media.bq.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {

    private final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_BOOT.equals(intent.getAction())) {
            Log.v("BroadcastReceiver", "We'll launch bluetooth service");
            //Intent service = new Intent(context, BluetoothService.class);
            //context.startService(service);
        }
    }

}
