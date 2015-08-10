package com.vincent.wearabledemo;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private int notificationId = 001;

    private EditText textInput;
    private ListView itemList;

    private CheckBox check_BigStyle;
    private CheckBox check_InboxStyle;
    private EditText inboxNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        check_BigStyle = (CheckBox) findViewById(R.id.check_BigStyle);
        check_InboxStyle = (CheckBox) findViewById(R.id.check_InboxStyle);
        inboxNumber = (EditText) findViewById(R.id.inboxNumber);

        check_InboxStyle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    inboxNumber.setVisibility(View.VISIBLE);
                else
                    inboxNumber.setVisibility(View.GONE);
            }
        });

        textInput = (EditText) findViewById(R.id.textInput);
        itemList = (ListView) findViewById(R.id.demoList);

        String[] demoItems = getResources().getStringArray(R.array.List_Items);
        ListAdapter listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, demoItems);
        itemList.setAdapter(listAdapter);

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        notification_basic();
                        break;
                    case 1:
                        notification_actionButton();
                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                }
            }
        });
    }

    private void notification_basic()
    {
        String input = getTextInput();

        Uri uri = Uri.parse("http://www.google.com/#q=" + input);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.sand_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_android01))
                .setContentTitle("Basic Feature")
                .setContentText(input)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        sandNotification(notiBuilder, input);
    }

    private void notification_actionButton()
    {
        String input = getTextInput();

        Uri uri = Uri.parse("http://www.google.com/#q=" + input);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(input));
        mapIntent.setData(mapUri);
        PendingIntent mapPendingIntent = PendingIntent.getActivity(this, 0, mapIntent, 0);

        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.mipmap.sand_icon, "Open Map!", mapPendingIntent).build();

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.sand_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_android01))
                .setContentTitle("Basic Feature")
                .setContentText(input)
                .setContentIntent(pendingIntent)
                .addAction(action)
                .setAutoCancel(true);

        sandNotification(notiBuilder, input);
    }



    private void sandNotification(NotificationCompat.Builder notiBuilder, String text)
    {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (check_BigStyle.isChecked())
        {
            NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
            bigStyle.setBigContentTitle("BIG TITLE!!");
            bigStyle.bigText(text);
            //Big Style only affects on the phone!

            notiBuilder.setStyle(bigStyle);
        }
        if (check_InboxStyle.isChecked())
        {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle("Inbox TITLE!!");

            String number = inboxNumber.getText().toString();
            int count;
            if (number.isEmpty())
                count = 1;
            else
                count = Integer.valueOf(number);

            for (int i = 0; i < count; i++) {
                inboxStyle.addLine((i + 1) + text);
            }
            notiBuilder.setStyle(inboxStyle);
        }

        notificationManager.notify(notificationId, notiBuilder.build());
    }

    private String getTextInput() {
        return textInput.getText().toString();
    }

    public void clearClick(View view) {
        textInput.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
