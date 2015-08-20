package com.vincent.wearabledemo;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.view.Gravity;

import java.util.List;

public class GridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context context;
    private List rows;

    public GridPagerAdapter(Context ctx, FragmentManager fm)
    {
        super(fm);
        context = ctx;
    }

    static final int[] BG_IMAGES = new int[] {
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark,
            android.R.color.holo_red_light,
            android.R.color.darker_gray,
            android.R.color.holo_purple,
    };

    // A simple container for static data in each page
    private static class Page
    {
        //Static Resources
        int titleRes = R.string.picker_title;
        int textRes = R.string.picker_text;
        int iconRes = android.R.drawable.star_big_on;

        int gravity = Gravity.CENTER;
        int expandDirection = CardFragment.EXPAND_DOWN;
        boolean expanded = true;
        float expansionFactor = 3.0f;
    }

    // Create a static set of pages in a 2D array
    private final Page[][] PAGES = {
            {new Page(), new Page()},
            {new Page(), new Page()},
            {new Page(), new Page(), new Page()},
            {new Page(), new Page(), new Page()},
            {new Page(), new Page(), new Page(), new Page()},
    };


    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int column)
    {
        Page page = PAGES[row][column];

        String rowNum = String.valueOf(row + 1);
        String columnNum = String.valueOf(column + 1);

        String title = page.titleRes != 0 ?
                rowNum + " " + context.getString(page.titleRes) : null;
        String text = page.textRes != 0 ?
                columnNum + " " +  context.getString(page.textRes) : null;

        CardFragment cardFragment = CardFragment.create(title, text, page.iconRes);

        cardFragment.setCardGravity(page.gravity);
        cardFragment.setExpansionEnabled(page.expanded);
        cardFragment.setExpansionDirection(page.expandDirection);
        cardFragment.setExpansionFactor(page.expansionFactor);

        return cardFragment;
    }

    @Override
    public Drawable getBackgroundForRow(int row)
    {
        return context.getResources().getDrawable(BG_IMAGES[row % BG_IMAGES.length]);
    }

    @Override
    public Drawable getBackgroundForPage(int row, int column)
    {
        if (row == 2 && column == 1)
            return context.getResources().getDrawable(R.mipmap.ic_launcher);
        else
            return GridPagerAdapter.BACKGROUND_NONE;
    }

    // Obtain the number of pages (Vertical)
    @Override
    public int getRowCount() {
        return PAGES.length;
    }

    // Obtain the number of pages (Horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        return PAGES[rowNum].length;
    }
}
