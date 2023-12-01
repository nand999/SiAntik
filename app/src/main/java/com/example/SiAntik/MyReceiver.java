//package com.example.SiAntik;
//
//import static android.app.PendingIntent.getActivity;
//import static java.security.AccessController.getContext;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.AlarmManager;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import androidx.core.app.ActivityCompat;
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//import androidx.core.content.ContextCompat;
//
//import java.util.Calendar;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class MyReceiver extends BroadcastReceiver {
//    private static final String CHANNEL_ID = "MyChannel";
//    private static final int NOTIFICATION_ID = 1;
//    private static final int REQUEST_NOTIFICATION_PERMISSION = 100;
//    private static final int ALARM_REQUEST_CODE = 101;
//
//
//    public static void triggerNotification(Context context) {
//        // Metode public untuk memicu notifikasi
//        MyReceiver receiver = new MyReceiver();
//        receiver.showNotification(context);
//    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        // Show notification at 7 AM
//        if (intent.getAction() != null && intent.getAction().equals("MY_NOTIFICATION_ACTION")) {
//            showNotification(context);
//        } else {
//            // Set up alarm for next day
//            scheduleNotification(context);
//        }
//    }
//
//    private void scheduleNotification(Context context) {
//        // Set the notification time to 7 AM
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 7);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//
//        // Check if the time has passed today, if yes schedule for tomorrow
//        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
//            calendar.add(Calendar.DAY_OF_YEAR, 1);
//        }
//
//        // Create an intent for the BroadcastReceiver
//        Intent intent = new Intent(context, MyReceiver.class);
//        intent.setAction("MY_NOTIFICATION_ACTION");
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        // Schedule the alarm using AlarmManager
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        if (alarmManager != null) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//        }
//    }
//
//    private void showNotification(Context context) {
//        createNotificationChannel(context);
//
//        // Check if permission is granted to show notifications
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.USE_FULL_SCREEN_INTENT) != PackageManager.PERMISSION_GRANTED) {
//            // If not granted, request permission or handle accordingly
//            // ...
//        } else {
//            // Permission granted, proceed to show the notification
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                    .setSmallIcon(android.R.drawable.ic_dialog_info)
//                    .setContentTitle("Pengingat")
//                    .setContentText("Anda belum mengirimkan laporan!")
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .setAutoCancel(true);
//
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//            notificationManager.notify(NOTIFICATION_ID, builder.build());
//        }
//    }
//
//
//
//    private void createNotificationChannel(Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "My Channel";
//            String description = "Channel untuk pengingat";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//
//            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//    private boolean cekKondisi() {
//        // Implement your condition checking here
//        return false;
//    }
//
//    private boolean checkLaporanStatus() {
//        Bundle extras = getActivity().getIntent().getExtras();
//        String nik_user = extras.getString("NIK");
////        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
////        String nik_user = sharedPref.getString("NIK", "");
//
//        RetrofitEndPoint layananApi = RetrofitClient.getConnection().create(RetrofitEndPoint.class);
//
//        Call<StatusData1> panggilan = layananApi.getStatusData(nik_user);
//
//        panggilan.enqueue(new Callback<StatusData1>() {
//            @Override
//            public boolean onResponse(Call<StatusData1> call, Response<StatusData1> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    StatusData1 statusData = response.body();
//                    String status = statusData.getStatus();
//                    boolean b;
//
//                    if (status.equals("sudah_lapor_positif")) {
//                        // Tampilkan gambar "anda sudah lapor"
//                        b = false;
//                    } else if (status.equals("belum_lapor")) {
//                        // Tampilkan gambar "anda belum lapor bulan ini"
//                       b= true;
//                    } else if(status.equals("error")){
//                        b = false;
//                    } else if (status.equals("sudah_lapor_negatif")) {
//                        b =  false;
//                    } else if (status.equals("sudah_lapor_belum")) {
//                        b = false;
//                    }
//                } else {
//                    // Handle respon server tidak berhasil di sini
//                    return false;
//                } return b;
//            }
//
//            @Override
//            public void onFailure(Call<StatusData1> call, Throwable t) {
//                // Handle kegagalan koneksi atau panggilan API di sini
//                t.printStackTrace();
//            }
//        });
//    }
//}
