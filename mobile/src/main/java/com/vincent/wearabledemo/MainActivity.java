package com.vincent.wearabledemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import android.widget.Toast;

import java.io.FileNotFoundException;

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

        check_BigStyle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    check_InboxStyle.setChecked(false);
            }
        });

        check_InboxStyle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    inboxNumber.setVisibility(View.VISIBLE);
                    check_BigStyle.setChecked(false);
                }
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
                        notification_wearableOnly();
                        break;
                    case 3:
                        notification_bigPicClick();
                        break;
                    case 4:
                        addPages();
                        break;
                    case 5:
                        notification_stacking();
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

    private void notification_wearableOnly()
    {
        String input = getTextInput();

        Uri uri = Uri.parse("http://www.google.com/#q=" + input);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(input));
        mapIntent.setData(mapUri);
        PendingIntent mapPendingIntent = PendingIntent.getActivity(this, 0, mapIntent, 0);

        NotificationCompat.Action mapAction = new NotificationCompat.Action
                .Builder(R.mipmap.sand_icon, "Open Map!", mapPendingIntent).build();

        Uri marketUri = Uri.parse("market://search?q=" + input);
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
//        marketIntent.setData(marketUri);
        PendingIntent marketPendingIntent = PendingIntent.getActivity(this, 0, marketIntent, 0);

        NotificationCompat.Action marketAction = new NotificationCompat.Action
                .Builder(android.R.drawable.ic_input_add, "Open Market", marketPendingIntent).build();

        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                .addAction(mapAction)
                .addAction(marketAction)
                .setHintHideIcon(true)
                .setBackground(BitmapFactory.decodeResource(getResources(), android.R.color.holo_blue_dark));

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Wearable-Only Feature")
                .setContentText(input)
                .setContentIntent(pendingIntent)
                .extend(wearableExtender)
                .setSmallIcon(R.mipmap.sand_icon)
                .setAutoCancel(true);

        sandNotification(notiBuilder, input);
    }

    private void notification_bigPicClick()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            Uri uri = data.getData();
            Log.i("URI Content", uri.toString());
            ContentResolver CR = this.getContentResolver();
            try {
                Bitmap image = BitmapFactory.decodeStream(CR.openInputStream(uri));
                notification_bigPicSand(image);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("File Not Found!", e.getMessage());
            }
        }
    }

    private void notification_bigPicSand(Bitmap image)
    {
        String input = getTextInput();

        Uri uri = Uri.parse("http://www.google.com/#q=" + input);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(input));
        mapIntent.setData(mapUri);
        PendingIntent mapPendingIntent = PendingIntent.getActivity(this, 0, mapIntent, 0);

        NotificationCompat.Action mapAction =
                new NotificationCompat.Action.Builder(R.mipmap.sand_icon, "Open Map!", mapPendingIntent).build();

       /*
       NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                .addAction(mapAction)
                .setHintHideIcon(true)
                .setBackground(BitmapFactory.decodeResource(getResources(), android.R.color.holo_blue_dark));
       */
                // setBackground 會把 BigPictureStyle 的 Picture 給覆蓋掉!

        NotificationCompat.BigPictureStyle bigPicStyle = new NotificationCompat.BigPictureStyle()
                .setBigContentTitle("BIG PICTURE!")
                .bigPicture(image);

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.sand_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_android01))
                //如果有 BigPictureStyle 的話，LargeIcon就不會有作用!
                .setContentTitle("Big Picture Feature")
                .setContentText(input)
                .setContentIntent(pendingIntent)
                .addAction(mapAction)
                //.extend(wearableExtender)
                .setStyle(bigPicStyle)
                .setAutoCancel(true);

        sandNotification(notiBuilder, input);
    }

    private void addPages()
    {
        String input = getTextInput();

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.sand_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_android01))
                .setContentTitle("First Page")
                .setContentText(input);

        Notification secondPage = new NotificationCompat.Builder(this)
                .setContentTitle("Second Page")
                .setContentText(input)
                .build();

        Notification thirdPage = new NotificationCompat.Builder(this)
                .setContentTitle("Third Page")
                .setContentText(input)
                .build();

        //在用來 Add Page 的 Notification 中 setSmallIcon or setLargeIcon 是沒用的!!!
        //setStyle也頂多改變文字而已

        notiBuilder.extend(new NotificationCompat.WearableExtender()
                .addPage(secondPage)
                .addPage(thirdPage));

        sandNotification(notiBuilder, input);
    }

    private void notification_stacking()
    {
        EditText groupIdInput = (EditText) findViewById(R.id.groupIdInput);
        String groupId = groupIdInput.getText().toString();
        String input = getTextInput();

        if (groupId.isEmpty())
            Toast.makeText(MainActivity.this, "Group ID Empty!", Toast.LENGTH_SHORT).show();
        else {
            NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("Stacking Massage")
                    .setContentText(input)
                    .setSmallIcon(R.mipmap.sand_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_android01))
                    .setGroup(groupId);

            if (check_InboxStyle.isChecked())
            {
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                String number = inboxNumber.getText().toString();
                int count;
                if (number.isEmpty())
                    count = 1;
                else
                    count = Integer.valueOf(number);

                for (int i = 0; i < count; i++) {
                    inboxStyle.addLine((i + 1) + input);
                }
                inboxStyle.setBigContentTitle(count + " New Inbox Massages");
                inboxStyle.setSummaryText("SUMMARY: " + input);

                notiBuilder.setContentTitle(count + " New Massages");
                notiBuilder.setStyle(inboxStyle);
                notiBuilder.setGroupSummary(true);
            }
            notificationId++;
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(notificationId, notiBuilder.build());
        }
    }

    private void sandNotification(NotificationCompat.Builder notiBuilder, String text)
    {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        /**
         * Big Text Style only affects on phones!
         * It will appear while text is too long,
         * or ActionButton was added.
         * If wearableExtender used, then the Big Text Style will not work!
         */
        if (check_BigStyle.isChecked())
        {
            NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
            bigStyle.setBigContentTitle("BIG TEXT TITLE!!");
            bigStyle.bigText(text);

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
        //setStyle只能選一種，BigText or Inbox or BigPicture.
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
