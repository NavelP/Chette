package com.nvdeveloper.Chette.actions.SMSActions;

public class department_messages_p {
    String message;
    String hotline_number;
    String time_sent;

    public department_messages_p(String message, String hotline_number, String time_sent) {
        this.message = message;
        this.hotline_number = hotline_number;
        this.time_sent = time_sent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHotline_number() {
        return hotline_number;
    }

    public void setHotline_number(String hotline_number) {
        this.hotline_number = hotline_number;
    }

    public String getTime_sent() {
        return time_sent;
    }

    public void setTime_sent(String time_sent) {
        this.time_sent = time_sent;
    }
}
