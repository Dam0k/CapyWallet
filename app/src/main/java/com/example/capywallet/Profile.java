package com.example.capywallet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;

public class Profile extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int PERMISSION_REQUEST_CAMERA = 102;
    private ImageView profileImageView;
    private Button takePhotoButton;
    private DatabaseHelper dbHelper;
    private long imageId = -1; // Default value to indicate no image
    private EditText editTextIncome;
    private Button saveIncomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        dbHelper = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileImageView = findViewById(R.id.profileImageView);
        takePhotoButton = findViewById(R.id.takePhotoButton);

        // Retrieve the imageId if it was saved previously
        imageId = dbHelper.getLastImageId();

        // Load the image if an imageId is available
        if (imageId != -1) {
            byte[] imageData = dbHelper.getImage(imageId);
            if (imageData != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                profileImageView.setImageBitmap(bitmap);
            }
        }

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    dispatchTakePictureIntent();
                } else {
                    requestCameraPermission();
                }
            }
        });

        editTextIncome = findViewById(R.id.editTextIncome);
        saveIncomeButton = findViewById(R.id.saveIncomeButton);

        saveIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIncomeToDatabase();
            }
        });
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profileImageView.setImageBitmap(imageBitmap);

            // Save the captured image to the database
            saveImageToDatabase(imageBitmap);
        }
    }

    private void saveImageToDatabase(Bitmap imageBitmap) {
        if (imageBitmap != null) {
            // Convert Bitmap to byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            // Save byte array to database and get the image id
            imageId = dbHelper.saveImage(byteArray);
            if (imageId != -1) {
                Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveIncomeToDatabase() {
        String incomeText = editTextIncome.getText().toString();
        if (!incomeText.isEmpty()) {
            double income = Double.parseDouble(incomeText);
            long existingIncomeId = dbHelper.getExistingIncomeId(); // Add this method to DatabaseHelper
            if (existingIncomeId != -1) {
                // If income already exists, update it
                int rowsAffected = dbHelper.updateIncome(existingIncomeId, income);
                if (rowsAffected > 0) {
                    Toast.makeText(this, "Income updated successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Failed to update income", Toast.LENGTH_SHORT).show();
                }
            } else {
                // If income doesn't exist, save it
                long incomeId = dbHelper.saveIncome(income);
                if (incomeId != -1) {
                    Toast.makeText(this, "Income saved successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Failed to save income", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Please enter income", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
