package com.test.bemoapplication.model.chat;

/**
 * Created by pardypanda05 on 1/8/17.
 */

public class ChatConversation {

    String messageKey;
    String userKey;
    String userName;
    String chatMessage;
    String deviceID;
    String timeStamp;

    public ChatConversation(String messageKey, String userKey, String userName, String chatMessage, String deviceID, String timeStamp) {
        this.messageKey = messageKey;
        this.userKey = userKey;
        this.userName = userName;
        this.chatMessage = chatMessage;
        this.deviceID = deviceID;
        this.timeStamp = timeStamp;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public ChatConversation() {
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean  equals (Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            ChatConversation chat = (ChatConversation) object;
            if (this.messageKey.equals(chat.getMessageKey())) {
                result = true;
            }
        }
        return result;
    }
}
