package com.thehobbygardener.greenr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SnapshotManager extends AppCompatActivity {

    //Setup for the camera intent
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;

    public static final String MY_SEED_BANK = "MySeedsFile";
    SharedPreferences seedSP;

    ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snapshot_manager);

        //Grab incoming intent information
        Intent incomingIntent = getIntent();
        final String name = incomingIntent.getStringExtra("Name");
        //Used to determine whether this photo is for the front or back of the seed packet
        final Boolean isFront = incomingIntent.getBooleanExtra("IsFront", true);

        button = (ImageButton) findViewById(R.id.ib_photo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        seedSP = getSharedPreferences(MY_SEED_BANK, MODE_PRIVATE);
        Button bSave = (Button) findViewById(R.id.b_save);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get current seed object
                Gson retrieveGson = new Gson();
                String retrieveJson = seedSP.getString(name, "");
                final SeedTemplate seed = retrieveGson.fromJson(retrieveJson, SeedTemplate.class);

                //Check whether this should be front or back
                if (isFront) {
                    seed.frontPhotoPath = mCurrentPhotoPath;
                } else {
                    seed.backPhotoPath = mCurrentPhotoPath;
                }

                Gson gson = new Gson();
                String json = gson.toJson(seed);
                seedSP.edit().putString(seed.name, json).apply();

                startActivity(new Intent(getApplicationContext(), SeedBank.class));
            }
        });

        Button bCancel = (Button) findViewById(R.id.b_cancel);
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SeedBank.class));
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i("Snapshot", "IO exception: error creating file");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "SeedBank_" + timeStamp + ".jpg";
        File photo = new File(Environment.getExternalStorageDirectory(), imageFileName);

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = photo.getAbsolutePath();
        return photo;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse("file://" + mCurrentPhotoPath));
                button = (ImageButton) findViewById(R.id.ib_photo);
                button.setImageBitmap(imageBitmap);
                Log.i("OnResult: ", "image set!");
            } catch (IOException e) {
                Log.i("OnResult", "Exception!");
                e.printStackTrace();
            }
        }
    }
}
