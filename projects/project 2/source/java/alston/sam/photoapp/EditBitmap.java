package alston.samuel.photoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class EditBitmap extends AppCompatActivity {

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bitmap);
        try {
            ImageView imageView = findViewById(R.id.editImageView);
            bitmap = getIntent().getParcelableExtra("bitmap");

            imageView.setImageBitmap(bitmap);
        } catch (Exception e){
            //don't break maybe?
        }
    }
}
