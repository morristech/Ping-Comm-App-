package com.yoscholar.ping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.yoscholar.ping.R;
import com.yoscholar.ping.adapter.ConversationsAdapter;
import com.yoscholar.ping.retrofitPojo.conversations.Conversation;
import com.yoscholar.ping.retrofitPojo.conversations.ConversationData;
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

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private Toolbar toolbar;
    private ListView conversationsListView;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        getConversations();
    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        conversationsListView = (ListView) findViewById(R.id.conversations_list_view);

    }

    private void getConversations() {

        RetrofitApi.ApiInterface apiInterface = RetrofitApi.getApiInterfaceInstance();

        Call<ConversationData> conversationDataCall = apiInterface.conversations(
                AppPreference.getString(this, AppPreference.EMAIL),
                AppPreference.getString(this, AppPreference.TOKEN)
        );

        conversationDataCall.enqueue(new Callback<ConversationData>() {
            @Override
            public void onResponse(Call<ConversationData> call, Response<ConversationData> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {

                        displayConversations(response.body().getConversations());

                    } else if (response.body().getStatus().equalsIgnoreCase("failure")) {


                        AppPreference.logOut(HomeActivity.this);

                        Toast.makeText(HomeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        openLoginScreen();

                        finish();

                    }

                } else {
                    Toast.makeText(HomeActivity.this, "Some error.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ConversationData> call, Throwable t) {

                Toast.makeText(HomeActivity.this, "Network error.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void displayConversations(ArrayList<Conversation> conversationArrayList) {


        Collections.sort(conversationArrayList, new Comparator<Conversation>() {
            @Override
            public int compare(Conversation o1, Conversation o2) {

                Date date1 = new Date();
                Date date2 = new Date();

                try {

                    date1 = dateFormat.parse(o1.getMessageTime());
                    date2 = dateFormat.parse(o2.getMessageTime());


                } catch (ParseException e) {

                    Log.e(TAG, "Error while parsing date string : " + e);
                }

                return date2.compareTo(date1);

            }
        });

        ConversationsAdapter conversationsAdapter = new ConversationsAdapter(HomeActivity.this, conversationArrayList);
        conversationsListView.setAdapter(conversationsAdapter);
    }


    private void openLoginScreen() {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
