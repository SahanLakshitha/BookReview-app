package com.what_to_read;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.info_tech.R;

import java.security.SecureRandom;
import java.util.ArrayList;

public class Message extends AppCompatActivity {
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    ListView message_listView;
    ImageView back_btn;
    DatabaseFactory databaseFactory;
    EditText text_message;
    TextView btn_send;
    CustomeAdapter2 customeAdapter;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        Blog_Details blog_data = (Blog_Details) intent.getSerializableExtra("DATA");
        databaseFactory = new DatabaseFactory(this);
        getSupportActionBar().hide();
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

        Cursor message_data = databaseFactory.get_messages(blog_data);

        ArrayList<Message_Details> data = new ArrayList<>();
        if (message_data.getCount() > 0){
            while (message_data.moveToNext()){
                Message_Details messageData = new Message_Details(message_data.getString(0), message_data.getString(1), message_data.getString(2),
                        message_data.getString(3));
                data.add(messageData);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        back_btn = findViewById(R.id.btn_back);
        message_listView = findViewById(R.id.message_listView);
        text_message = findViewById(R.id.message_text);
        btn_send = findViewById(R.id.message_btn_send);

        customeAdapter = new CustomeAdapter2(this, data, user);
        message_listView.setAdapter(customeAdapter);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = text_message.getText().toString().trim();
                String key = randomString();

                if(!msg.isEmpty()){
                    if(databaseFactory.add_Comment(blog_data, msg, key, user.getEmail(), user.getName())){
                        Message_Details messageData = new Message_Details(key, user.getEmail(), user.getName(), msg);
                        data.add(messageData);

                        customeAdapter = new CustomeAdapter2(getApplicationContext(), data, user);
                        message_listView.setAdapter(customeAdapter);
                        text_message.setText("");
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Message sending failed ..", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    return;
                }
            }
        });

        message_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Message_Details temData = data.get(i);
                if(!temData.getMessage().equals("message deleted") && temData.getSender_email().equals(user.getEmail())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Message.this);
                    builder.setMessage("Do you want to delete the Message?");
                    builder.setTitle("Confirmation !");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            //                        finish();
                            databaseFactory.delete_msg(blog_data, temData.getMessage_id());
                            data.get(i).setMessage("message deleted");

                            customeAdapter = new CustomeAdapter2(getApplicationContext(), data, user);
                            message_listView.setAdapter(customeAdapter);

                        }
                    });
                    builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int which)
                        {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

                return false;
            }
        });

    }

    private String randomString(){
        StringBuilder sb = new StringBuilder(10);
        for(int i = 0; i < 10; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}

class CustomeAdapter2 extends ArrayAdapter<Message_Details> {
    User user_data;

    public CustomeAdapter2(Context context, ArrayList<Message_Details> blogs, User user_data) {
        super(context, R.layout.activity_mess_card, blogs);
        this.user_data = user_data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Message_Details message = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_mess_card, parent, false);
        }

        TextView message_rec = convertView.findViewById(R.id.message_rec);
        TextView message_rec_email = convertView.findViewById(R.id.message_rec_mail);
        TextView message_rec_name = convertView.findViewById(R.id.message_rec_name);
        TextView message_sen = convertView.findViewById(R.id.message_sen);

        if(user_data.getEmail().equals(message.getSender_email())){
            message_rec.setVisibility(View.GONE);
            message_rec_email.setVisibility(View.GONE);
            message_rec_name.setVisibility(View.GONE);
            message_sen.setText(message.getMessage());
        }
        else {
            message_sen.setVisibility(View.GONE);
            message_rec.setText(message.getMessage());
            message_rec_email.setText(message.getSender_name());
            message_rec_name.setText(message.getSender_email());
        }

        return convertView;
    }




}