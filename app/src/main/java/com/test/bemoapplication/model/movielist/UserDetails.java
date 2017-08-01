package com.test.bemoapplication.model.chat;

/**
 * Created by pardypanda05 on 1/8/17.
 */

public class UserDetails {
    String nickName;
    String avatarURL;

    public UserDetails(String nickName, String avatarURL) {
        this.nickName = nickName;
        this.avatarURL = avatarURL;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

}
