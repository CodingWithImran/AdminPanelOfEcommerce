package com.codingwithimran.adminpanelecommerce.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddAllProductActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 3;

    private EditText productNameEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private EditText stockEditText;
    private ImageView productImageView;
    private VideoView productVideoView;
    private Button selectImageButton;
    private Button addButton;

    private Uri imageUri;
    String mimeType;

    private FirebaseFirestore db;
    private StorageReference storageReference;
    ProgressDialog progressDialog;
    String productId = UUID.randomUUID().toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_all_product);

        productNameEditText = findViewById(R.id.product_name_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        priceEditText = findViewById(R.id.price_edit_text);
        productImageView = findViewById(R.id.image_view);
        selectImageButton = findViewById(R.id.select_image_button);
        addButton = findViewById(R.id.add_button);
        productVideoView = findViewById(R.id.productVideoView);
        stockEditText = findViewById(R.id.stock_edit_text);



        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("product_images");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please waite...");
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
    }
    private void openFileChooser() {
        // Launch the file picker intent
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
//                // Launch the ProductDetailsActivity with the image URI
//                Intent intent = new Intent(this, ProductDetailsActivity.class);
//                intent.putExtra("image", imageUri.toString());
//                startActivity(intent);
                productImageView.setImageURI(imageUri);
                productImageView.setVisibility(View.VISIBLE);

                productVideoView.setVisibility(View.GONE);

            } else if (mimeType.startsWith("video/")) {
//                // Launch the ProductDetailsActivity with the video URI
//                Intent intent = new Intent(this, ProductDetailsActivity.class);
//                intent.putExtra("video", fileUri.toString());
//                startActivity(intent);
                productVideoView.setVideoURI(imageUri);
                productVideoView.setVisibility(View.VISIBLE);
                productVideoView.pause();
                productImageView.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "Unsupported file type", Toast.LENGTH_SHORT).show();
            }

        }
    }


   // Add product in Firebase
    private void addProduct() {
        progressDialog.show();
        String productName = productNameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String stock = stockEditText.getText().toString().trim();

        if (productName.isEmpty() || description.isEmpty() || price.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        Toast.makeText(this, "" + fileReference, Toast.LENGTH_SHORT).show();
        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl = uri.toString();
                        AllProductModal product;
                        if (mimeType.startsWith("image/")) {
                            product = new AllProductModal(imageUrl, description, productName,Integer.parseInt(stock),  Integer.parseInt(price));
                            product.setProductId(productId);
                            db.collection("AllProducts").add(product).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddAllProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddAllProductActivity.this, "Error adding product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (mimeType.startsWith("video/")) {
                            product = new AllProductModal( description, productName, Integer.parseInt(price));
                            product.setProduct_video(imageUrl);
                            product.setStockProduct(Integer.parseInt(stock));
                           product.setProductId(productId);
                            db.collection("AllProducts").add(product).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddAllProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddAllProductActivity.this, "Error adding product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(AddAllProductActivity.this, "no proper format is set", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddAllProductActivity.this, "Error uploading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}