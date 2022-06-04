package com.what_to_read;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.info_tech.R;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainAddNewBlog extends AppCompatActivity {
    static final String code = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    ImageView home, addImage;
    EditText part, brand, description;
    Button addBlog;
    User user;

    Boolean validity_of_images = false;

    DatabaseFactory databaseFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_add_new_blog);
        databaseFactory = new DatabaseFactory(this);

        Cursor currentUser = databaseFactory.get_current_user();

        if (currentUser.getCount() == 1){
            while (currentUser.moveToNext()) {
                user = new User(currentUser.getString(0), currentUser.getString(1), null);
            }
        }
        else {
            databaseFactory.delete_curent_user();
            startActivity(new Intent(this, LoginActivity.class));
        }

        part = findViewById(R.id.Name);
        brand = findViewById(R.id.Brand);
        description = findViewById(R.id.Description);
        addImage = findViewById(R.id.btn_add_image);
        home = findViewById(R.id.homeBtn);
        addBlog = findViewById(R.id.PostBlogBtn2);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainHomePage.class));
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });



        addBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String _part = part.getText().toString().trim();
               String _brand = brand.getText().toString().trim();
               String _des = description.getText().toString().trim();

                if(TextUtils.isEmpty(_part)){
                    part.setError("please enter the book name.");
                    part.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(_brand)){
                    brand.setError("please enter author of the book.");
                    brand.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(_des)){
                    description.setError("please enter book catagory.");
                    description.requestFocus();
                    return;
                }
                if(!validity_of_images){
                    Toast.makeText(MainAddNewBlog.this, " Insert picture of the book.", Toast.LENGTH_SHORT).show();
                    validity_of_images = false;
                    return;
                }

                byte[] bytespp = convertImageViewToByteArray(addImage);
                String id = randomString();

                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String Date = dateFormat.format(date);

                if(databaseFactory.add_posts_data(id, _part, _brand, _des, bytespp, user.getEmail(), user.getName(), Date)){
                    Toast.makeText(MainAddNewBlog.this, "successfully posted your blog", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainHomePage.class));
                }else {
                    Toast.makeText(getApplicationContext(), "blog not updated", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getImage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainAddNewBlog.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_choose_image, null);

        builder.setCancelable(false);
        builder.setView(dialogView);

        ImageView imageCamera = dialogView.findViewById(R.id.imageView3);
        ImageView imagePGallery = dialogView.findViewById(R.id.imageView4);
        TextView btn_cancle = dialogView.findViewById(R.id.button);


        final AlertDialog alertDialogProfilePicture = builder.create();
        alertDialogProfilePicture.show();

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogProfilePicture.dismiss();
            }
        });

        imageCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
                alertDialogProfilePicture.cancel();
            }
        });

        imagePGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePicture.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(takePicture, 2);
                }
                alertDialogProfilePicture.cancel();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImageUri = data.getData();
                    addImage.setImageURI(selectedImageUri);
                    validity_of_images = true;
                }
                break;
            case 2:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapImage = (Bitmap) bundle.get("data");
                    addImage.setImageBitmap(bitmapImage);
                    validity_of_images = true;
                }
                break;
        }
    }

    private byte[] convertImageViewToByteArray(ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private String randomString(){
        StringBuilder sb = new StringBuilder(10);
        for(int i = 0; i < 10; i++)
            sb.append(code.charAt(rnd.nextInt(code.length())));
        return sb.toString();
    }
}