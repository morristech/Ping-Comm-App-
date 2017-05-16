package com.yoscholar.ping.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import com.yoscholar.ping.R;
import com.yoscholar.ping.adapter.ConversationsAdapter;
import com.yoscholar.ping.retrofitPojo.conversations.Conversation;
import com.yoscholar.ping.retrofitPojo.conversations.ConversationData;
import com.yoscholar.ping.utils.AppPreference;
import com.yoscholar.ping.utils.RetrofitApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView conversationsListView;

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

                    ArrayList<Conversation> conversationArrayList = response.body().getConversations();

                    ConversationsAdapter conversationsAdapter = new ConversationsAdapter(HomeActivity.this, conversationArrayList);
                    conversationsListView.setAdapter(conversationsAdapter);


                } else {
                    Toast.makeText(HomeActivity.this, "Some error.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ConversationData> call, Throwable t) {

                Toast.makeText(HomeActivity.this, "Some error.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
