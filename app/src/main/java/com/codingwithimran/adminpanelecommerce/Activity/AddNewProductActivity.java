package com.codingwithimran.adminpanelecommerce.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.codingwithimran.adminpanelecommerce.R;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.codingwithimran.adminpanelecommerce.Modals.AllProductModal;
import com.codingwithimran.adminpanelecommerce.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddNewProductActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 4;

    private EditText newproductNameEditText;
    private EditText newdescriptionEditText;
    private EditText newpriceEditText;
    private EditText productStock;
    private ImageView newproductImageView;
    private VideoView newProductVideoView;
    private Button newselectImageButton;
    private Button newaddButton;
    String mimeType;
    private Uri imageUri;

    private FirebaseFirestore db;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        newproductNameEditText = findViewById(R.id.new_product_name_edit_text);
        newdescriptionEditText = findViewById(R.id.new_description_edit_text);
        newpriceEditText = findViewById(R.id.new_price_edit_text);
        newproductImageView = findViewById(R.id.new_image_view);
        newselectImageButton = findViewById(R.id.new_select_image_button);
        newaddButton = findViewById(R.id.new_add_button);
        newProductVideoView = findViewById(R.id.new_videoView);
        productStock = findViewById(R.id.new_stock_edit_text);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("product_images");
        newselectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        newaddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
    }
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            mimeType = getContentResolver().getType(imageUri);
            Toast.makeText(this, "" + mimeType, Toast.LENGTH_SHORT).show();
            if (mimeType.startsWith("image/")) {
                newproductImageView.setImageURI(imageUri);
                newproductImageView.setVisibility(View.VISIBLE);

                newProductVideoView.setVisibility(View.GONE);

            } else if (mimeType.startsWith("video/")) {
                newProductVideoView.setVideoURI(imageUri);
                newProductVideoView.setVisibility(View.VISIBLE);
                newProductVideoView.pause();
                newproductImageView.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "Unsupported file type", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Add product in Firebase
    private void addProduct() {
        String productName = newproductNameEditText.getText().toString().trim();
        String description = newdescriptionEditText.getText().toString().trim();
        String price = newpriceEditText.getText().toString().trim();
        String stock = productStock.getText().toString().trim();

        if (productName.isEmpty() || description.isEmpty() || price.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl = uri.toString();
                        AllProductModal product;
                        if (mimeType.startsWith("image/")) {
                            product = new AllProductModal(imageUrl, description, productName, Integer.parseInt(stock), Integer.parseInt(price));
                            product.setStockProduct(Integer.parseInt(stock));
                            db.collection("NewProducts").add(product).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(AddNewProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddNewProductActivity.this, "Error adding product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (mimeType.startsWith("video/")) {
                            product = new AllProductModal( description, productName, Integer.parseInt(price));
                            product.setProduct_video(imageUrl);
                            db.collection("NewProducts").add(product).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(AddNewProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddNewProductActivity.this, "Error adding product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(AddNewProductActivity.this, "no proper format is set", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddNewProductActivity.this, "Error uploading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}