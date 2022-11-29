package com.nvdeveloper.Chette.track_response;

public interface ManipulateComplaints {
    void MakeABoost(String image_text, String department, String day, String chette_user, String complaint_db_id);
    void DeleteAComplaint(String image_text, String department, String day, String chette_user, String complaint_db_id);
    void EnlargeOnClick(String department_name, String image_url, String image_caption, String Votes);
}
