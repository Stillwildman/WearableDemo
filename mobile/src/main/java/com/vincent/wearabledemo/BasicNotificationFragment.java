package com.vincent.wearabledemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BasicNotificationFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static BasicNotificationFragment instance;

    private int notificationId = 001;

    private EditText textInput;

    private CheckBox check_BigStyle;
    private CheckBox check_InboxStyle;
    private EditText inboxNumber;

    private EditText groupIdInput;

    public static BasicNotificationFragment newInstance(int sectionNumber)
    {
        BasicNotificationFragment fragmentInstance = new BasicNotificationFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragmentInstance.setArguments(args);

        instance = fragmentInstance;
        return fragmentInstance;
    }

    public BasicNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_basic_notification, container, false);

        textInput = (EditText) rootView.findViewById(R.id.textInput);
        ListView itemList = (ListView) rootView.findViewById(R.id.demoList);
        ImageButton clearButton = (ImageButton) rootView.findViewById(R.id.clearBtn);
        check_BigStyle = (CheckBox) rootView.findViewById(R.id.check_BigStyle);
        check_InboxStyle = (CheckBox) rootView.findViewById(R.id.check_InboxStyle);
        inboxNumber = (EditText) rootView.findViewById(R.id.inboxNumber);
        groupIdInput = (EditText) rootView.findViewById(R.id.groupIdInput);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textInput.setText("");
            }
        });

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

        String[] demoItems = getResources().getStringArray(R.array.List_Items);
        ListAdapter listAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, demoItems);

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
        return rootView;
    }

    private void notification_basic()
    {
        String input = getTextInput();

        Uri uri = Uri.parse("http://www.google.com/#q=" + input);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(getActivity())
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
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(input));
        mapIntent.setData(mapUri);
        PendingIntent mapPendingIntent = PendingIntent.getActivity(getActivity(), 0, mapIntent, 0);

        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.mipmap.sand_icon, "Open Map!", mapPendingIntent).build();

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(getActivity())
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
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(input));
        mapIntent.setData(mapUri);
        PendingIntent mapPendingIntent = PendingIntent.getActivity(getActivity(), 0, mapIntent, 0);

        NotificationCompat.Action mapAction = new NotificationCompat.Action
                .Builder(R.mipmap.sand_icon, "Open Map!", mapPendingIntent).build();

        Uri marketUri = Uri.parse("market://search?q=" + input);
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
//        marketIntent.setData(marketUri);
        PendingIntent marketPendingIntent = PendingIntent.getActivity(getActivity(), 0, marketIntent, 0);

        NotificationCompat.Action marketAction = new NotificationCompat.Action
                .Builder(android.R.drawable.ic_input_add, "Open Market", marketPendingIntent).build();

        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                .addAction(mapAction)
                .addAction(marketAction)
                .setHintHideIcon(true)
                .setBackground(BitmapFactory.decodeResource(getResources(), android.R.color.holo_blue_dark));

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(getActivity())
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
        //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
          //上面那行，或下面這段，都可以
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        startActivityForResult(intent, 0);
    }

    public void notification_bigPicSand(Bitmap image)
    {
        String input = getTextInput();

        Uri uri = Uri.parse("http://www.google.com/#q=" + input);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(input));
        mapIntent.setData(mapUri);
        PendingIntent mapPendingIntent = PendingIntent.getActivity(getActivity(), 0, mapIntent, 0);

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

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(getActivity())
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

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(getActivity())
                .setSmallIcon(R.mipmap.sand_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_android01))
                .setContentTitle("First Page")
                .setContentText(input);

        Notification secondPage = new NotificationCompat.Builder(getActivity())
                .setContentTitle("Second Page")
                .setContentText(input)
                .build();

        Notification thirdPage = new NotificationCompat.Builder(getActivity())
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
        String groupId = groupIdInput.getText().toString();
        String input = getTextInput();

        if (groupId.isEmpty())
            Toast.makeText(getActivity(), "Group ID Empty!", Toast.LENGTH_SHORT).show();
        else {
            NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(getActivity())
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
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
            notificationManager.notify(notificationId, notiBuilder.build());
        }
    }

    private void sandNotification(NotificationCompat.Builder notiBuilder, String text)
    {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());

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
}
