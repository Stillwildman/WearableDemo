package com.vincent.wearabledemo.activity;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;

import com.vincent.wearabledemo.R;

public class CardActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_activity);
        setAmbientEnabled();

        /**
         * 有兩種方法增加 Card，
         *
         * 下面是第一種，搭配 FrameLayout 使用，
         * 並且包在 BoxInsetLayout 裡，在 FrameLayout 裡設定 app:layout_box="(position)" 的屬性
         * 來調整 Card 的位置，Layout內容較無法客製化。
         *
         * 第二種方法是，直接在 layout 中加入 CardScrollView & CardFrame 等物件，
         * 階層為 BoxInsetLayout → CardScrollView → CardFrame，
         *
         * CardScrollView 相當於 FrameLayout，可設定 CardFrame 的 Gravity 屬性來改變位置。
         */

        /*
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        CardFragment cardFragment = CardFragment.create(
                getString(R.string.card_title),
                getString(R.string.card_text),
                R.drawable.card_frame);

        fragmentTransaction.add(R.id.frame_layout, cardFragment);
        fragmentTransaction.commit();
        */
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
    }
}
