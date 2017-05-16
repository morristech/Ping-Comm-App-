package com.yoscholar.ping.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.yoscholar.ping.R;
import com.yoscholar.ping.retrofitPojo.conversations.Conversation;

import java.util.ArrayList;

/**
 * Created by agrim on 16/5/17.
 */

public class ConversationsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Conversation> conversationArrayList;

    public ConversationsAdapter(Context context, ArrayList<Conversation> conversationArrayList) {
        this.context = context;
        this.conversationArrayList = conversationArrayList;
    }

    @Override
    public int getCount() {
        return conversationArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.conversations_list_item, parent, false);

        }

        int color = Color.parseColor("#53AB3A");

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(conversationArrayList.get(position).getChildName().charAt(0) + "", color);

        ImageView image = (ImageView) convertView.findViewById(R.id.alphabet_image_view);
        image.setImageDrawable(drawable);

        TextView headingTextView = (TextView) convertView.findViewById(R.id.heading_text_view);
        headingTextView.setText(conversationArrayList.get(position).getChildName() + " <> " + conversationArrayList.get(position).getProviderName().split(" ")[0]);

        TextView messageTextView = (TextView) convertView.findViewById(R.id.message_text_view);
        messageTextView.setText(conversationArrayList.get(position).getMessage());

        TextView messageTimeTextView = (TextView) convertView.findViewById(R.id.message_time_text_view);
        messageTimeTextView.setText(conversationArrayList.get(position).getMessageTime());

        return convertView;
    }
}
