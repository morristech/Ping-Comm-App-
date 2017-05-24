package com.yoscholar.ping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yoscholar.ping.R;
import com.yoscholar.ping.adapter.ConversationsAdapter;
import com.yoscholar.ping.pojo.RefreshScreen;
import com.yoscholar.ping.retrofitPojo.conversations.Conversation;
import com.yoscholar.ping.retrofitPojo.conversations.ConversationData;
import com.yoscholar.ping.utils.AppPreference;
import com.yoscholar.ping.utils.RetrofitApi;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    public static final String CHILD_ID = "child_id";
    public static final String CHILD_NAME = "child_name";
    public static final String PROVIDER_ID = "provider_id";
    public static final String PROVIDER_NAME = "provider_name";

    private Toolbar toolbar;
    private ListView conversationsListView;
    private ProgressBar progressBar;
    private LinearLayout errorLinearLayout;
    private Button retryButton;

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        conversationsListView = (ListView) findViewById(R.id.conversations_list_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        errorLinearLayout = (LinearLayout) findViewById(R.id.error_layout);
        retryButton = (Button) findViewById(R.id.retry_button);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getConversations();
            }
        });

    }

    private void getConversations() {

        progressBar.setVisibility(View.VISIBLE);
        conversationsListView.setVisibility(View.GONE);
        errorLinearLayout.setVisibility(View.GONE);

        RetrofitApi.ApiInterface apiInterface = RetrofitApi.getApiInterfaceInstance();

        Call<ConversationData> conversationDataCall = apiInterface.conversations(
                AppPreference.getString(this, AppPreference.EMAIL),
                AppPreference.getString(this, AppPreference.TOKEN)
        );

        conversationDataCall.enqueue(new Callback<ConversationData>() {
            @Override
            public void onResponse(Call<ConversationData> call, Response<ConversationData> response) {

                if (response.isSuccessful()) {

                    progressBar.setVisibility(View.GONE);
                    conversationsListView.setVisibility(View.VISIBLE);
                    errorLinearLayout.setVisibility(View.GONE);

                    if (response.body().getStatus().equalsIgnoreCase("success")) {

                        displayConversations(response.body().getConversations());

                    } else if (response.body().getStatus().equalsIgnoreCase("failure")) {


                        AppPreference.logOut(HomeActivity.this);

                        Toast.makeText(HomeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        openLoginScreen();

                        finish();

                    }

                } else {

                    progressBar.setVisibility(View.GONE);
                    conversationsListView.setVisibility(View.GONE);
                    errorLinearLayout.setVisibility(View.VISIBLE);

                    Toast.makeText(HomeActivity.this, "Some error.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ConversationData> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                conversationsListView.setVisibility(View.GONE);
                errorLinearLayout.setVisibility(View.VISIBLE);

                Toast.makeText(HomeActivity.this, "Network error.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void displayConversations(final ArrayList<Conversation> conversationArrayList) {

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
        conversationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(HomeActivity.this, MessagesActivity.class);
                intent.putExtra(CHILD_ID, conversationArrayList.get(position).getChildId());
                intent.putExtra(CHILD_NAME, conversationArrayList.get(position).getChildName());
                intent.putExtra(PROVIDER_ID, conversationArrayList.get(position).getProviderId());
                intent.putExtra(PROVIDER_NAME, conversationArrayList.get(position).getProviderName());

                startActivity(intent);
            }
        });
    }


    private void openLoginScreen() {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getConversations();

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Called to refresh the conversationMessagesListView when a new push notification is received.
     * Called from {@link com.yoscholar.ping.fcm.MessagingService#sendNotification(String, String)}
     *
     * @param refreshScreen refreshScreen
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPushNotificationReceived(RefreshScreen refreshScreen) {

        if (refreshScreen.isRefresh())
            getConversations();
    }

    public void openYoScholar(View view) {



    }
}
