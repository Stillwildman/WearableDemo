package com.vincent.wearabledemo.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vincent.wearabledemo.R;

public class WearableListItemLayout extends LinearLayout implements WearableListView.OnCenterProximityListener {

    private ImageView itemImage;
    private TextView nameText;

    private final float textFadeAlpha;
    private final int imageFadeColor;
    private final int imageChosenColor;

    public WearableListItemLayout(Context context)
    {
        this(context, null);
    }

    public WearableListItemLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public WearableListItemLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        textFadeAlpha = getResources().getInteger(R.integer.action_text_faded_alpha) / 100f;
        imageFadeColor = getResources().getColor(android.R.color.darker_gray);
        imageChosenColor = getResources().getColor(android.R.color.holo_blue_dark);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();

        itemImage = (ImageView) findViewById(R.id.itemImage);
        nameText = (TextView) findViewById(R.id.nameText);
    }

    @Override
    public void onCenterPosition(boolean b)
    {
        nameText.setAlpha(1f);
        ((GradientDrawable) itemImage.getDrawable()).setColor(imageChosenColor);
    }

    @Override
    public void onNonCenterPosition(boolean b)
    {
        nameText.setAlpha(textFadeAlpha);
        ((GradientDrawable) itemImage.getDrawable()).setColor(imageFadeColor);
    }
}
