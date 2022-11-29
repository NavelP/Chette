package com.nvdeveloper.Chette.track_response;

public class complaint_p {
    String complaint_department_name;
    String complaint_text, complaint_image_url, image_caption;

    String num_of_votes;
    String date;
    String time_send;
    String user_phone;
    String complaint_db_id;
    String boosts;

    public complaint_p(String complaint_department_name, String complaint_text, String complaint_image_url,
                       String image_caption, String num_of_votes, String date, String time_send,
                       String user_phone, String complaint_db_id, String boosts) {
        this.complaint_department_name = complaint_department_name;
        this.complaint_text = complaint_text;
        this.complaint_image_url = complaint_image_url;
        this.image_caption = image_caption;
        this.num_of_votes = num_of_votes;
        this.date = date;
        this.time_send = time_send;
        this.user_phone = user_phone;
        this.complaint_db_id = complaint_db_id;
        this.boosts = boosts;
    }

    public String getBoosts() {
        return boosts;
    }

    public void setBoosts(String boosts) {
        this.boosts = boosts;
    }

    public String getTime_send() {
        return time_send;
    }

    public void setTime_send(String time_send) {
        this.time_send = time_send;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getComplaint_db_id() {
        return complaint_db_id;
    }

    public void setComplaint_db_id(String complaint_db_id) {
        this.complaint_db_id = complaint_db_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getImage_caption() {
        return image_caption;
    }

    public void setImage_caption(String image_caption) {
        this.image_caption = image_caption;
    }

    public String getComplaint_department_name() {
        return complaint_department_name;
    }

    public void setComplaint_department_name(String complaint_department_name) {
        this.complaint_department_name = complaint_department_name;
    }

    public String getComplaint_text() {
        return complaint_text;
    }

    public void setComplaint_text(String complaint_text) {
        this.complaint_text = complaint_text;
    }

    public String getComplaint_image_url() {
        return complaint_image_url;
    }

    public void setComplaint_image_url(String complaint_image_url) {
        this.complaint_image_url = complaint_image_url;
    }

    public String getNum_of_votes() {
        return num_of_votes;
    }

    public void setNum_of_votes(String num_of_votes) {
        this.num_of_votes = num_of_votes;
    }
}
