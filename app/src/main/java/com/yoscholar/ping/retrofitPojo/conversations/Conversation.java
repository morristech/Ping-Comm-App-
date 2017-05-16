
package com.yoscholar.ping.retrofitPojo.conversations;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Conversation {

    @SerializedName("child_id")
    @Expose
    private String childId;
    @SerializedName("child_name")
    @Expose
    private String childName;
    @SerializedName("provider_id")
    @Expose
    private String providerId;
    @SerializedName("provider_name")
    @Expose
    private String providerName;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("message_time")
    @Expose
    private String messageTime;

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
