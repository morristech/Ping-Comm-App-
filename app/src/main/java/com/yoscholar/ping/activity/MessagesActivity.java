package com.yoscholar.ping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yoscholar.ping.R;
import com.yoscholar.ping.adapter.ConversationMessagesAdapter;
import com.yoscholar.ping.retrofitPojo.conversation.Conversation;
import com.yoscholar.ping.retrofitPojo.conversation.ConversationMessage;
import com.yoscholar.ping.utils.AppPreference;
import com.yoscholar.ping.utils.RetrofitApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesActivity extends AppCompatActivity {

    private static final String TAG = MessagesActivity.class.getSimpleName();

    private Toolbar toolbar;
    private ListView conversationMessagesListView;
    private TextView childNameTextView;
    private TextView providerNameTextView;
    private ProgressBar progressBar;

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        init();

        getConversationMessages();
    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        childNameTextView = (TextView) findViewById(R.id.child_name_text_view);
        childNameTextView.setText(getIntent().getStringExtra(HomeActivity.CHILD_NAME));

        providerNameTextView = (TextView) findViewById(R.id.provider_name_text_view);
        providerNameTextView.setText(getIntent().getStringExtra(HomeActivity.PROVIDER_NAME));

        conversationMessagesListView = (ListView) findViewById(R.id.conversation_messages_list_view);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    private void getConversationMessages() {

        RetrofitApi.ApiInterface apiInterface = RetrofitApi.getApiInterfaceInstance();

        Call<Conversation> conversationCall = apiInterface.conversationMessages(
                getIntent().getStringExtra(HomeActivity.CHILD_ID),
                getIntent().getStringExtra(HomeActivity.PROVIDER_ID),
                AppPreference.getString(MessagesActivity.this, AppPreference.TOKEN)
        );

        conversationCall.enqueue(new Callback<Conversation>() {
            @Override
            public void onResponse(Call<Conversation> call, Response<Conversation> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {


                        displayConversationMessages(response.body().getConversationMessages());

                    } else if (response.body().getStatus().equalsIgnoreCase("failure")) {

                        AppPreference.logOut(MessagesActivity.this);

                        Toast.makeText(MessagesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        openLoginScreen();

                        finish();

                    }

                } else {

                    Toast.makeText(MessagesActivity.this, "Some error.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<Conversation> call, Throwable t) {

                Toast.makeText(MessagesActivity.this, "Network error.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void displayConversationMessages(ArrayList<ConversationMessage> conversationMessageArrayList) {

        conversationMessagesListView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        Collections.sort(conversationMessageArrayList, new Comparator<ConversationMessage>() {
            @Override
            public int compare(ConversationMessage o1, ConversationMessage o2) {

                Date date1 = new Date();
                Date date2 = new Date();

                try {

                    date1 = dateFormat.parse(o1.getCreatedAt());
                    date2 = dateFormat.parse(o2.getCreatedAt());


                } catch (ParseException e) {

                    Log.e(TAG, "Error while parsing date string : " + e);
                }

                return date2.compareTo(date1);

            }
        });

        ConversationMessagesAdapter conversationMessagesAdapter = new ConversationMessagesAdapter(MessagesActivity.this, conversationMessageArrayList);

        conversationMessagesListView.setAdapter(conversationMessagesAdapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void openLoginScreen() {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

}
