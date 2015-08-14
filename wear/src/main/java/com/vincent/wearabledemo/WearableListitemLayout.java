package com.vincent.wearabledemo;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WearableListitemLayout extends LinearLayout implements WearableListView.OnCenterProximityListener {

    private ImageView itemImage;
    private TextView nameText;

    private final float textFadeAlpha;
    private final int imageFadeColor;
    private final int imageChosenColor;

    public WearableListitemLayout(Context context)
    {
        this(context, null);
    }

    public WearableListitemLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public WearableListitemLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        textFadeAlpha = getResources().getInteger(R.integer.action_text_faded_alpha) / 100f;
        imageFadeColor = getResources().getColor(R.color.grey);
        imageChosenColor = getResources().getColor(R.color.blue);
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
//        ((GradienDrawable) itemImage.getDrawable()).setColor(imageFaseColor);
    }

    @Override
    public void onNonCenterPosition(boolean b)
    {

    }
}
