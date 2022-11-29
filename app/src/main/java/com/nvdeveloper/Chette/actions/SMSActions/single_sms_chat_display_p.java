package com.nvdeveloper.Chette.actions.SMSActions;

public class single_sms_chat_display_p {
    String image_url, most_recent_message, number_of_messages, time_sent,last_seen;
    int image;
    String name;

    public single_sms_chat_display_p(String image_url, String most_recent_message, String number_of_messages, String time_sent, String last_seen,
                                     String name) {
        this.image_url = image_url;
        this.most_recent_message = most_recent_message;
        this.number_of_messages = number_of_messages;
        this.time_sent = time_sent;
        this.last_seen = last_seen;
        this.name = name;
    }

    public single_sms_chat_display_p(String most_recent_message, String number_of_messages, String time_sent, String last_seen, int image,
                                     String name) {
        this.most_recent_message = most_recent_message;
        this.number_of_messages = number_of_messages;
        this.time_sent = time_sent;
        this.last_seen = last_seen;
        this.image = image;
        this.name = name;

    }
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(String last_seen) {
        this.last_seen = last_seen;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getMost_recent_message() {
        return most_recent_message;
    }

    public void setMost_recent_message(String most_recent_message) {
        this.most_recent_message = most_recent_message;
    }

    public String getNumber_of_messages() {
        return number_of_messages;
    }

    public void setNumber_of_messages(String number_of_messages) {
        this.number_of_messages = number_of_messages;
    }

    public String getTime_sent() {
        return time_sent;
    }

    public void setTime_sent(String time_sent) {
        this.time_sent = time_sent;
    }
}
