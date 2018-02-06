/*
Author: Samuel Alston
Last Modified: dd/mm/yyyy 05/02/2018

Project Overview:
    The goal of this app is to meet the standards of the CS 480 Project #2 requirements.
    "...develop an image manipulation application. Your program should be able to both
    manipulate existing phone images or to take new ones that can then be manipulated."
    
Current Status: Currently, the application launches to the first view where you can
    press a button to launch the camera, once you take an image and approve it said
    image will appear in the imageView below the open camera button in the first view.
*/
package alston.sam.photoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Button btnpic;
    ImageView imgTakenPic;
    private static final int CAM_REQUEST=1313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnpic = (Button) findViewById(R.id.button);
        imgTakenPic = (ImageView)findViewById(R.id.imageView);
        btnpic.setOnClickListener(new btnTakePhotoClicker());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == CAM_REQUEST){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgTakenPic.setImageBitmap(bitmap);
            }
        } catch(Exception e) {
            //don't break if the user sends no image
        }

    }

    class btnTakePhotoClicker implements Button.OnClickListener{

        @Override
        public void onClick(View view){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAM_REQUEST);
        }
    }
}
