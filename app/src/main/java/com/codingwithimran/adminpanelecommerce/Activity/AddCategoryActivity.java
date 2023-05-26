package com.codingwithimran.adminpanelecommerce.Activity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codingwithimran.adminpanelecommerce.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddCategoryActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText categoryTypeInput;
    private EditText categoryNameInput;
    private ImageView categoryIconButton;
    private ImageView categoryIconImage;
    private Button saveButton;

    private Uri imageUri;

    private FirebaseFirestore db;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        categoryTypeInput = findViewById(R.id.categoryTypeInput);
        categoryNameInput = findViewById(R.id.categoryNameInput);
        categoryIconButton = findViewById(R.id.categoryIconSelectButton);
        categoryIconImage = findViewById(R.id.categoryIconImage);
        saveButton = findViewById(R.id.saveButton);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        categoryIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCategory();
            }
        });
    }

    // Get Image from gallery and store it in Storage
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            categoryIconImage.setImageURI(imageUri);
        }
    }
    //       End from Getting Image from Gallery

    //       Save Data in Firebase
    private void saveCategory() {
        String categoryType = categoryTypeInput.getText().toString().trim();
        String categoryName = categoryNameInput.getText().toString().trim();

        if (categoryType.isEmpty()) {
            categoryTypeInput.setError("Category type is required.");
            categoryTypeInput.requestFocus();
            return;
        }

        if (categoryName.isEmpty()) {
            categoryNameInput.setError("Category name is required.");
            categoryNameInput.requestFocus();
            return;
        }

        if (imageUri == null) {
            Toast.makeText(this, "Please select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("category_icons");
        final StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

        UploadTask uploadTask = fileRef.putFile(imageUri);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return fileRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String iconUrl = task.getResult().toString();

                Map<String, Object> category = new HashMap<>();
                category.put("category_type", categoryType);
                category.put("category_name", categoryName);
                category.put("category_icon", iconUrl);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("categories").add(category)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(this, "Category added successfully.", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Error adding category: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "Error uploading image: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }


}