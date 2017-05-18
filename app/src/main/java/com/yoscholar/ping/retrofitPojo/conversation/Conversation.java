
package com.yoscholar.ping.retrofitPojo.conversation;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Conversation {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("conversation_messages")
    @Expose
    private ArrayList<ConversationMessage> conversationMessages = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ConversationMessage> getConversationMessages() {
        return conversationMessages;
    }

    public void setConversationMessages(ArrayList<ConversationMessage> conversationMessages) {
        this.conversationMessages = conversationMessages;
    }

}
