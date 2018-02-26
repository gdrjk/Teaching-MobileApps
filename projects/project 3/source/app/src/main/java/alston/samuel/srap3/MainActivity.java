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
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.IOUtils;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button btnpic;
    private Button btngal;
    private Button btndetect;
    private ImageView imgTakenPic;
    private Bitmap bitmap;
    //code for using camera
    private static final int CAM_REQUEST = 1313;
    //code for using gallery/file explorer
    public static final int PICK_IMAGE = 1;
    private static final int IMAGE_VIEW_WIDTH = 455;
    private Handler mCloudHandler;
    private HandlerThread mCloudThread;
    private static final String TAG = MainActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgTakenPic = (ImageView)findViewById(R.id.imageView);

        btnpic = (Button) findViewById(R.id.button_camera);
        btnpic.setOnClickListener(new btnTakePhotoClicker());

        btngal = (Button) findViewById(R.id.button_gallery);
        btngal.setOnClickListener(new btnOpenGalleryClicker());

        btndetect = (Button) findViewById(R.id.button_detect);
        btndetect.setOnClickListener(new btnMakeRequest());

        mCloudThread = new HandlerThread("CloudThread");
        mCloudThread.start();
        mCloudHandler = new Handler(mCloudThread.getLooper());
    }

    private void resizeBitmap(){
        //resize the bitmap to a predetermined imageView width (IMAGE_VIEW_WIDTH)
        float aspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
        int width = IMAGE_VIEW_WIDTH;
        int height = Math.round(width / aspectRatio);

        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
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

    class btnMakeRequest implements  Button.OnClickListener{
        //make request to Google for label detection
        @Override
        public void onClick(View view) {
            if(bitmap!=null){
                /*Intent labelIntent = new Intent(getApplicationContext(),LabelDetection.class);
                labelIntent.putExtra("bitmap",bitmap);
                labelIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivity(labelIntent);

                labelMap = makeRequest();
                printMap(labelMap);
                */
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                final byte[] photoData = stream.toByteArray();

                mCloudHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "sending image to cloud vision");
                        // annotate image by uploading to Cloud Vision API
                        try {
                            Map<String, Float> annotations = LabelDetection.annotateImage(photoData);
                            Log.d(TAG, "cloud vision annotations:" + annotations);
                            if (annotations != null) {
                                printMap(annotations);
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "Cloud Vison API error: ", e);
                        }
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Select an image to edit!", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Toast.makeText(getApplicationContext(),pair.getKey() + " = " + pair.getValue(),Toast.LENGTH_LONG).show();
            it.remove(); // avoids a ConcurrentModificationException
        }
        Toast.makeText(getApplicationContext(),"No more labels.",Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCloudThread.quit();
    }
}