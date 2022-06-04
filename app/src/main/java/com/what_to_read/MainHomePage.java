package com.what_to_read;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.info_tech.R;

import java.util.ArrayList;

public class MainHomePage extends AppCompatActivity {
    ImageView addBlog;
    ListView listView;
    ImageView logout;
    DatabaseFactory databaseFactory;
    User user;

    ArrayList<Blog_Details> data;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 123;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        databaseFactory = new DatabaseFactory(this);
        Cursor blog = databaseFactory.get_all_blog();

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


        ArrayList<Blog_Details> data = new ArrayList<>();
        if (blog.getCount() > 0){
            while (blog.moveToNext()){
                Blog_Details blogData = new Blog_Details(blog.getString(0), blog.getString(1), blog.getString(2),
                        blog.getString(3),  blog.getString(4), blog.getString(5), blog.getBlob(6));
                data.add(blogData);
            }
        }

        System.out.println(data);

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_home_page);


        addBlog = findViewById(R.id.addBlogBtn);
        listView = findViewById(R.id.list_item_View);
        logout = findViewById(R.id.logout);


        CustomeAdapter1 customeAdapter = new CustomeAdapter1(this, data, user);
        listView.setAdapter(customeAdapter);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseFactory.delete_curent_user();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        addBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainAddNewBlog.class));
            }
        });
    }
}



            class CustomeAdapter1 extends ArrayAdapter<Blog_Details> {


    private DatabaseFactory databaseFactory;

    public CustomeAdapter1(Context context, ArrayList<Blog_Details> blogs, User user) {
        super(context, R.layout.activity_card, blogs);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Blog_Details blog = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_card, parent, false);
        }


        TextView name = convertView.findViewById(R.id.textView9);
        TextView email = convertView.findViewById(R.id.textView11);
        TextView type = convertView.findViewById(R.id.getType);
        TextView brand = convertView.findViewById(R.id.getBrand);
        TextView description = convertView.findViewById(R.id.textView16);
        ImageView blogImage = convertView.findViewById(R.id.imageView6);
        ImageView comment = convertView.findViewById(R.id.Comment);
        ImageView edit_delete = convertView.findViewById(R.id.imageView9);
//        TextView date = convertView.findViewById(R.id.);


        byte [] img_byte = blog.getImage();
        Bitmap bmp = BitmapFactory.decodeByteArray(img_byte, 0, img_byte.length);

        edit_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setItems(R.array.items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switch (id){
                            case 0:
                                // edit post
                                break;
                            case 1:
                                // delete post
                                delete_post(blog);
                                break;
                        }
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });



        name.setText(blog.getName());
        type.setText(blog.getPart());
        brand.setText(blog.getBrand());
        description.setText(blog.getDescription());
        blogImage.setImageBitmap(bmp);
        email.setText(blog.getEmail());

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("-----------------------");
                Context context = getContext();
                Intent i = new Intent(context, Message.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("DATA", blog);
                context.startActivity(i);
            }
        });

        return convertView;
    }

    private void delete_post(Blog_Details blog){
        databaseFactory = new DatabaseFactory(getContext());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Confirm");
        alertDialogBuilder.setMessage("Are you sure?");

        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Cursor cursor = databaseFactory.delete_post(blog.getPost_id());
                System.out.println(cursor.getCount());

                if(!(cursor.getCount() > 0)){
                    Toast.makeText(getContext(), "Post Deleted Successfully", Toast.LENGTH_SHORT).show();

                    Context context = getContext();
                    Intent i = new Intent(context, MainHomePage.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtra("POST_DATA", blog);
                    context.startActivity(i);

                }
                dialog.dismiss();
            }
        });

        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    }