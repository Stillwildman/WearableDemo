package com.vincent.wearabledemo;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ListAdapter extends WearableListView.Adapter {

    private String[] listData;
    private final Context listContext;
    private final LayoutInflater inflater;

    public ListAdapter(Context context, String[] dataSet)
    {
        listContext = context;
        listData = dataSet;
        inflater = LayoutInflater.from(context);
    }

    public static class ItemViewHolder extends WearableListView.ViewHolder
    {
        private TextView textView;

        public ItemViewHolder(View itemView)
        {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.nameText);
        }
    }

    // Create new views for list items
    // (Invoked by the WearableListView's layout manager)
    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // Inflate our custom layout for list items
        return new ItemViewHolder(inflater.inflate(R.layout.list_item_layout, null));
    }

    // Replace the contents of a list item
    // Instead of creating new views, the list tries to recycle existing ones
    // (Invoked by the WearableListView's layout manager)
    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position)
    {
        // Retrieve the text view
        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        TextView view = itemHolder.textView;

        // Replace text contents
        view.setText(listData[position]);

        // Replace list item's metadata
        holder.itemView.setTag(position);
    }

    // Return the size of your dataset
    // (invoked by the WearableListView's layout manager)
    @Override
    public int getItemCount()
    {
        return listData.length;
    }
}
