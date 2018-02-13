/*
Author: Samuel Alston
Last Modified: dd/mm/yyyy 12/02/2018

Purpose: this activity will do the image manipulation on the user selected
    image from MainActivity.
 */
package alston.samuel.photoapp;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

public class EditBitmap extends AppCompatActivity {

    private Bitmap bitmap;
    private Bitmap mutableBitmap;
    private ImageView imageView;
    private static final int COLOR_MAX = 255;

    private static float red_value;
    private static float grn_value;
    private static float blu_value;
    private static int brt_value;
    private boolean brt_changed;
    private static SeekBar red_seekbar;
    private SeekBar grn_seekbar;
    private SeekBar blu_seekbar;
    private SeekBar brt_seekbar;
    private Button reflection;
    private Button reset;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bitmap);
        imageView = findViewById(R.id.editImageView);
        bitmap = getIntent().getParcelableExtra("bitmap");
        //make bitmap mutable so we can apply effects
        mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        imageView.setImageBitmap(mutableBitmap);

        reflection = (Button) findViewById(R.id.button_reflection);
        reflection.setOnClickListener(new btnReflection());
        reset = (Button) findViewById(R.id.button_reset);
        reset.setOnClickListener(new btnReset());
        save = (Button) findViewById(R.id.button_save);
        save.setOnClickListener(new btnSave());
        red_seekbar = init_red_seekbar();
        blu_seekbar = init_blu_seekbar();
        grn_seekbar = init_grn_seekbar();
        brt_seekbar = init_brt_seekbar();
    }

    public static Bitmap applyReflection(Bitmap originalImage) {
        // gap space between original and reflected
        final int reflectionGap = 4;
        // get image size
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // this will not scale but will flip on the Y axis
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        // create a Bitmap with the flip matrix applied to it.
        // we only want the bottom half of the image
        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height/2, width, height/2, matrix, false);

        // create a new bitmap with same width but taller to fit reflection
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height/2), Bitmap.Config.ARGB_8888);

        // create a new Canvas with the bitmap that's big enough for
        // the image plus gap plus reflection
        Canvas canvas = new Canvas(bitmapWithReflection);
        // draw in the original image
        canvas.drawBitmap(originalImage, 0, 0, null);
        // draw in the gap
        Paint defaultPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
        // draw in the reflection
        canvas.drawBitmap(reflectionImage,0, height + reflectionGap, null);

        // create a shader that is a linear gradient that covers the reflection
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff,
                Shader.TileMode.CLAMP);
        // set the paint to use this shader (linear gradient)
        paint.setShader(shader);
        // set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

        return bitmapWithReflection;
    }

    public static Bitmap boostColor(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        int A,R,G,B;
        int pixel;

        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R =(int)(Color.red(pixel) * (1 + red_value*0.1));
                if(R > 255) R = 255;
                G =(int)(Color.green(pixel) * (1 + grn_value*0.01));
                if(G > 255) G = 255;

                B =(int)(Color.blue(pixel) * (1 + blu_value*0.01));
                if(B > 255) B = 255;

                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }

    public static Bitmap doBrightness(Bitmap src, int value) {
        value-=50;
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                // increase/decrease each channel
                R += value;
                if(R > COLOR_MAX) { R = COLOR_MAX; }
                else if(R < 0) { R = 0; }

                G += value;
                if(G > COLOR_MAX) { G = COLOR_MAX; }
                else if(G < 0) { G = 0; }

                B += value;
                if(B > COLOR_MAX) { B = COLOR_MAX; }
                else if(B < 0) { B = 0; }

                // apply new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }

    public void redraw(){

        imageView.setImageBitmap(mutableBitmap);
        if (brt_changed) {
            mutableBitmap = doBrightness(mutableBitmap, brt_value);
        }

    }

    public SeekBar init_red_seekbar() {
        final SeekBar red_seekbar = (SeekBar) findViewById(R.id.red_seekbar);
        final TextView redText = (TextView) findViewById(R.id.red_txt);
        red_value =0f;
        red_seekbar.setProgress(50);

        red_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                redText.setText("RED "+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                red_value = seekBar.getProgress();
                mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                mutableBitmap = boostColor(mutableBitmap);
                redraw();
            }
        });
        return red_seekbar;
    }

    public SeekBar init_grn_seekbar() {
        final SeekBar grn_seekbar = (SeekBar) findViewById(R.id.grn_seekbar);
        final TextView grnText = (TextView) findViewById(R.id.grn_txt);
        grn_value = 0f;
        grn_seekbar.setProgress(50);
        grn_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                grnText.setText("GRN "+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                grn_value = seekBar.getProgress();
                mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                mutableBitmap = boostColor(mutableBitmap);
                redraw();
            }
        });
        return grn_seekbar;
    }

    public SeekBar init_blu_seekbar() {
        final SeekBar blu_seekbar = (SeekBar) findViewById(R.id.blu_seekbar);
        final TextView bluText = (TextView) findViewById(R.id.blu_txt);
        blu_value = 0f;
        blu_seekbar.setProgress(50);

        blu_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bluText.setText("BLU "+(int) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                blu_value = seekBar.getProgress();
                mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                mutableBitmap = boostColor(mutableBitmap);
                redraw();
            }
        });
        return blu_seekbar;
    }

    public SeekBar init_brt_seekbar() {
        final SeekBar brt_seekbar = (SeekBar) findViewById(R.id.brt_seekbar);
        final TextView brtText = (TextView) findViewById(R.id.brt_txt);
        brt_changed = false;
        brt_seekbar.setProgress(50);

        brt_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brtText.setText("BRT "+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                brt_changed = true;
                brt_value = seekBar.getProgress();
                mutableBitmap = doBrightness(mutableBitmap,brt_value);
                redraw();
            }
        });
        return brt_seekbar;
    }

    class btnReflection implements  Button.OnClickListener{
        //set Open Gallery button intent
        @Override
        public void onClick(View view) {
            mutableBitmap = applyReflection(mutableBitmap);
            imageView.setImageBitmap(mutableBitmap);
        }
    }

    class btnReset implements Button.OnClickListener{
        @Override
        public void onClick(View view) {
            imageView.setImageBitmap(bitmap);
            red_seekbar.setProgress(50);
            grn_seekbar.setProgress(50);
            blu_seekbar.setProgress(50);
            brt_seekbar.setProgress(50);
        }
    }

    class btnSave implements Button.OnClickListener{
        @Override
        public void onClick(View view){
            saveImageToExternalStorage(mutableBitmap);
        }
    }
    private void saveImageToExternalStorage(Bitmap finalBitmap) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "sra_p2_img" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(),"Attempting to save: "+fname,Toast.LENGTH_LONG).show();

        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });

    }

}
