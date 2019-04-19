package oliver.bookhunter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // setting alarm
        Intent alarmIntent = new Intent(arg0, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(arg0, 0, alarmIntent, 0);
        manager = (AlarmManager) arg0.getSystemService(Context.ALARM_SERVICE);
        int interval = 10000;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(arg0, "Alarm Set", Toast.LENGTH_SHORT).show();



    }

}