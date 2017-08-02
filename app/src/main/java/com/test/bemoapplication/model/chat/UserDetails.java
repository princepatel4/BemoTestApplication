package com.test.bemoapplication.model.chat;

/**
 * Created by pardypanda05 on 1/8/17.
 */

public class UserDetails {

    String nickName;
    String fireBaseToken;

    public UserDetails(String nickName, String fireBaseToken) {
        this.nickName = nickName;
        this.fireBaseToken = fireBaseToken;
    }

    public String getFireBaseToken() {
        return fireBaseToken;
    }

    public void setFireBaseToken(String fireBaseToken) {
        this.fireBaseToken = fireBaseToken;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}
