package com.nvdeveloper.Chette.actions.ImageActions;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nvdeveloper.Chette.R;
import com.nvdeveloper.Chette.actions.ManipulateActions;
import com.nvdeveloper.Chette.user.MainActivityUser;

import java.io.ByteArrayOutputStream;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;


public class ImageCompressorAndUploader extends Fragment{
    String gallery_or_photo;
    String department_name;
    String phone_number;

    ProgressBar uploadProgress;
    TextView uploaded_percentage;

    EditText caption_edit_text;
    ManipulateActions manipulateActions;
    public ImageCompressorAndUploader(String gallery_or_photo,  String department_name, String phone_number, ManipulateActions manipulateActions){
        this.gallery_or_photo = gallery_or_photo;
        this.department_name = department_name;
        this.phone_number = phone_number;
        this.manipulateActions = manipulateActions;
    }

    ImageView image_to_upload;
    Button upload_image;
    Uri imageUri;

    DateTimeFormatter dateTimeFormatter;
    LocalDateTime now;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String myDate;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chette-72742-default-rtdb.firebaseio.com/");
    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://chette-72742.appspot.com/");

    String unique_name;

    private String Storage_Path = "Department_Complaints";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_action_activity, container, false);

        image_to_upload = view.findViewById(R.id.shotImage);
        upload_image = view.findViewById(R.id.upload_image);

        uploadProgress = view.findViewById(R.id.upload_progress);
        uploaded_percentage = view.findViewById(R.id.percentage_uploaded);

        uploadProgress.setVisibility(View.GONE);
        uploaded_percentage.setVisibility(View.GONE);

        caption_edit_text = view.findViewById(R.id.CaptionEditText);

        if(gallery_or_photo.equals("photo")){
            ShotPhoto();
        }else{
            PickImage();
        }
        //disable the upload button if no image is selected
        upload_image.setEnabled(false);

        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProgress.setVisibility(View.VISIBLE);
                uploaded_percentage.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UploadImage();
                    }
                }, 2000);

            }
        });
        return view;
    }

    //getting image extension
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    private void UploadImage() {
        if(imageUri != null){
            unique_name = CreateUniqueName() + "." + getFileExtension(imageUri);
            StorageReference fileStorage = storageReference.child("complaints").child(department_name).child(unique_name);

            fileStorage.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            uploadProgress.setProgress(0);
                            uploaded_percentage.setText((int) 0 + "%");

                            Toast.makeText(getContext(), "Uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                    storageReference.child("complaints").child(department_name).child(unique_name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Image_P image_info = new Image_P(caption_edit_text.getText().toString().trim(),
                                    uri.toString());

                            String uploadId = databaseReference.push().getKey();
                            databaseReference.child("complaints").child("images").child(department_name).child(getDate()).child(MainActivityUser.phone).
                                    child(uploadId).setValue(image_info);

                            databaseReference.child("complaints").child("images").child(department_name).child(getDate()).child(MainActivityUser.phone).
                                    child(uploadId).child("boosts").setValue("1");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    uploadProgress.setVisibility(View.GONE);
                                    uploaded_percentage.setVisibility(View.GONE);
                                }
                            }, 3000);
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("URLERROR", e.getMessage());
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 *(snapshot.getBytesTransferred() / snapshot.getTotalByteCount()));

                    uploaded_percentage.setText((int) progress + "%");
                    uploadProgress.setProgress((int) progress);
                }
            });
        }
    }

    private String getDate() {
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        myDate = simpleDateFormat.format(calendar.getTime());

        return myDate;
    }

    //if the image is to be picked from the gallery
    private void PickImage(){
        //checking permissions to read external storage
        if((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }else{
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 12);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if((requestCode == 12) && (resultCode == RESULT_OK)){
            if(data != null){
                image_to_upload.setImageURI(null);
                imageUri = data.getData();
                image_to_upload.setImageURI(data.getData());
                upload_image.setEnabled(true);
            }
        }

        if((requestCode == 100) && (resultCode == RESULT_OK)){
            //yet to be implemented from the camera
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageUri = getImageUri(getContext(), photo);
            image_to_upload.setImageURI(imageUri);
            upload_image.setEnabled(true);
        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    //method to get the current time as image name
    private String CreateUniqueName(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yy--HH:mm:ss.SSS");
            now = LocalDateTime.now();

            return dateTimeFormatter.format(now);
        }else{
            return System.currentTimeMillis() + "";
        }
    }
    //camera intent
    private Object CameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 100);

        return null;
    }

    //if the image is to be shot using the camera
    private void ShotPhoto(){
        Toast.makeText(getContext(), "here", Toast.LENGTH_SHORT).show();
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }else{
            CameraIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if((requestCode == 0) && (gallery_or_photo.equals("gallery"))){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 12);
        }

        if((requestCode == 200) && (gallery_or_photo.equals("photo"))){
            manipulateActions.PostCameraPhoto(phone_number, department_name);
        }
    }
}
