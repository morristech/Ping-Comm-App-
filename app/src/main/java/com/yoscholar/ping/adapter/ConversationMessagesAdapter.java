package com.yoscholar.ping.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yoscholar.ping.R;
import com.yoscholar.ping.retrofitPojo.conversation.ConversationMessage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by agrim on 16/5/17.
 */

public class ConversationMessagesAdapter extends BaseAdapter {

    private static final String TAG = ConversationMessagesAdapter.class.getSimpleName();
    public static final String ALERT = "alert";
    public static final String NOTIFICATION = "notification";

    private Context context;
    private ArrayList<ConversationMessage> conversationMessageArrayList;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    public ConversationMessagesAdapter(Context context, ArrayList<ConversationMessage> conversationMessageArrayList) {
        this.context = context;
        this.conversationMessageArrayList = conversationMessageArrayList;
    }

    @Override
    public int getCount() {
        return conversationMessageArrayList.size();
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
            convertView = inflater.inflate(R.layout.conversation_messages_list_item, parent, false);

        }

        ImageView image = (ImageView) convertView.findViewById(R.id.type_indicator_image_view);

        if (conversationMessageArrayList.get(position).getType().equals(ALERT))
            image.setImageResource(R.drawable.ic_warning_black_24dp);
        else if (conversationMessageArrayList.get(position).getType().equals(NOTIFICATION))
            image.setImageResource(R.drawable.ic_notifications_black_24dp);

        TextView childNameTextView = (TextView) convertView.findViewById(R.id.message_title_text_view);
        childNameTextView.setText(conversationMessageArrayList.get(position).getTitle());

        TextView providerNameTextView = (TextView) convertView.findViewById(R.id.message_content_text_view);
        providerNameTextView.setText(conversationMessageArrayList.get(position).getContent());

        TextView messageTimeTextView = (TextView) convertView.findViewById(R.id.relative_time_text_view);

        CharSequence relativeTime = "empty";
        try {

            relativeTime = DateUtils.getRelativeTimeSpanString(
                    dateFormat.parse(conversationMessageArrayList.get(position).getCreatedAt()).getTime(),
                    new Date().getTime(),
                    DateUtils.SECOND_IN_MILLIS);

        } catch (ParseException e) {

            Log.e(TAG, "Error while parsing date string : " + e);

        }

        messageTimeTextView.setText(relativeTime);

        return convertView;
    }
}
