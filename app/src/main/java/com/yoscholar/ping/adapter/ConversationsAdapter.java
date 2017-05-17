package com.yoscholar.ping.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.yoscholar.ping.R;
import com.yoscholar.ping.retrofitPojo.conversations.Conversation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by agrim on 16/5/17.
 */

public class ConversationsAdapter extends BaseAdapter {

    private static final String TAG = ConversationsAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<Conversation> conversationArrayList;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

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

        TextView childNameTextView = (TextView) convertView.findViewById(R.id.child_name_text_view);
        childNameTextView.setText(conversationArrayList.get(position).getChildName());

        TextView providerNameTextView = (TextView) convertView.findViewById(R.id.provider_name_text_view);
        providerNameTextView.setText(conversationArrayList.get(position).getProviderName());

        TextView messageTextView = (TextView) convertView.findViewById(R.id.message_text_view);
        messageTextView.setText(conversationArrayList.get(position).getMessage());

        TextView messageTimeTextView = (TextView) convertView.findViewById(R.id.relative_time_text_view);

        CharSequence relativeTime = "empty";
        try {

            relativeTime = DateUtils.getRelativeTimeSpanString(
                    dateFormat.parse(conversationArrayList.get(position).getMessageTime()).getTime(),
                    new Date().getTime(),
                    DateUtils.SECOND_IN_MILLIS);

        } catch (ParseException e) {

            Log.e(TAG, "Error while parsing date string : " + e);

        }

        messageTimeTextView.setText(relativeTime);

        return convertView;
    }
}
