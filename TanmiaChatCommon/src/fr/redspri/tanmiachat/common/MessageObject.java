package fr.redspri.tanmiachat.common;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor@Getter
public class MessageObject {
    private String author;
    private String password;
    private String content;

    public String serialize() {
        return new Gson().toJson(this);
    }

    public static MessageObject deserialize(String json) {
        return new Gson().fromJson(json, MessageObject.class);
    }

}
