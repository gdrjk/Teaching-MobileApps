/*
Author: Samuel Alston
Last modified: 02/24/2018

Purpose: This app is to complete the requirements for CS480's project 3.
"...use the Google Vision API to interpret pictures taken by the camera. Your application should then try to guess what is in the picture."


 */
package alston.samuel.srap3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private Button btnpic;
    private Button btngal;
    private Button btnedit;
    private ImageView imgTakenPic;
    private Bitmap bitmap;
    //code for using camera
    private static final int CAM_REQUEST = 1313;
    //code for using gallery/file explorer
    public static final int PICK_IMAGE = 1;
    private static final int IMAGE_VIEW_WIDTH = 455;
    //the vision object used to pass info back and forth to Google Vision API
    private Vision vision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgTakenPic = (ImageView)findViewById(R.id.imageView);

        btnpic = (Button) findViewById(R.id.button_camera);
        btnpic.setOnClickListener(new btnTakePhotoClicker());

        btngal = (Button) findViewById(R.id.button_gallery);
        btngal.setOnClickListener(new btnOpenGalleryClicker());

        btnedit = (Button) findViewById(R.id.button_manipulate);
        btnedit.setOnClickListener(new btnEditPhotoClicker());

        vision = createVisionBuilder().build();
    }

    private Vision.Builder createVisionBuilder(){
        //create a vision builder for a new vision
        Vision.Builder visionBuilder = new Vision.Builder(
                new NetHttpTransport(),
                new AndroidJsonFactory(),
                null);

        visionBuilder.setVisionRequestInitializer(
                new VisionRequestInitializer("AIzaSyCAlxdQBvhRZ5IcCYFEi7pAoVUpE_LBaDo"));

        return visionBuilder;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            super.onActivityResult(requestCode, resultCode, data);
            //if cam request, get image from camera
            if(requestCode == CAM_REQUEST){
                bitmap = (Bitmap) data.getExtras().get("data");
                imgTakenPic.setImageBitmap(bitmap);
            }
            //if picking image from gallery we have to convert URI to data via inputStream
            if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri imageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(imageStream);
                resizeBitmap();
                imgTakenPic.setImageBitmap(bitmap);
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

    class btnEditPhotoClicker implements  Button.OnClickListener{
        //launch edit image activity
        @Override
        public void onClick(View view) {
            if(bitmap!=null){
                Intent editIntent = new Intent(getApplicationContext(),DrawingActivity.class);
                editIntent.putExtra("bitmap",bitmap);
                editIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivity(editIntent);
            } else {
                Toast.makeText(getApplicationContext(), "Select an image to edit!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void resizeBitmap(){
        //resize the bitmap to a predetermined imageView width (IMAGE_VIEW_WIDTH)
        float aspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
        int width = IMAGE_VIEW_WIDTH;
        int height = Math.round(width / aspectRatio);

        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
    }


}