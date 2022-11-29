package com.nvdeveloper.Chette.actions;

public interface ManipulateActions {
    void MakeDefaultPhoneCall(String phone_number);
    void MakeChetteCall(String phone_number);
    void PostGalleryPhoto(String phone_number, String department_name);
    void PostCameraPhoto(String phone_number, String department_name);
    void SendDefaultSms(String phone_number);
    void SendChetteSms(String phone_number);
}
