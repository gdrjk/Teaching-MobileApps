/*
Author: Samuel Alston
Last Modified: dd/mm/yyyy 12/02/2018
Project Overview:
    The goal of this app is to meet the standards of the CS 480 Project #2 requirements.
    "...develop an image manipulation application. Your program should be able to both
    manipulate existing phone images or to take new ones that can then be manipulated."
Current Status: Currently, the application launches to the first view where you can
    press a button to launch the camera, once you take an image and approve it said
    image will appear in the imageView below the open camera button in the first view.
    Open image gallery button added, selected image can be moved to the imageView
    Edit image button added. Nothing attached to it yet.
*/
package alston.samuel.photoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    Button btnpic;
    Button btngal;
    ImageView imgTakenPic;
    //code for using camera
    private static final int CAM_REQUEST = 1313;
    //code for using gallery/file explorer
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnpic = (Button) findViewById(R.id.button_camera);
        imgTakenPic = (ImageView)findViewById(R.id.imageView);
        btnpic.setOnClickListener(new btnTakePhotoClicker());

        btngal = (Button) findViewById(R.id.button_gallery);
        imgTakenPic = (ImageView) findViewById(R.id.imageView);
        btngal.setOnClickListener(new btnOpenGalleryClicker());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            super.onActivityResult(requestCode, resultCode, data);
            //if cam request, get image from camera
            if(requestCode == CAM_REQUEST){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgTakenPic.setImageBitmap(bitmap);
            }
            //if picking image from gallery we have to convert URI to data via inputStream
            if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri imageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imgTakenPic.setImageBitmap(selectedImage);
            }
        } catch(Exception e) {
            //don't break if the user sends no image
        }

    }

    class btnTakePhotoClicker implements Button.OnClickListener{
        //set Open camera button intent
        @Override
        public void onClick(View view){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAM_REQUEST);
        }
    }

    class btnOpenGalleryClicker implements  Button.OnClickListener{
        //set Open Gallery button intent
        @Override
        public void onClick(View view) {
            // Create intent to Open Image applications like Gallery, Google Photos
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            startActivityForResult(galleryIntent, PICK_IMAGE);

        }

    }
}
