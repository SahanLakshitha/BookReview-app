package com.what_to_read;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.info_tech.R;

public class LoginActivity extends AppCompatActivity {
    EditText name, pass;
    TextView registration ;
    Button login;
    DatabaseFactory databaseFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_login);

        name = findViewById(R.id.etName);
        pass = findViewById(R.id.etPassword);
        login = findViewById(R.id.login);
        registration = findViewById(R.id.registration);
        databaseFactory = new DatabaseFactory(this);

        Cursor loginUser = databaseFactory.get_current_user();
        if(loginUser.getCount() != 0){
            startActivity(new Intent(getApplicationContext(), MainHomePage.class));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nm = name.getText().toString().trim();
                String ps = pass.getText().toString().trim();

                databaseFactory.delete_curent_user(); // delete temp users if available


                if(!(nm.equals("")) && !(ps.equals(""))){
                    Cursor response = databaseFactory.verifyUser(nm);
                    if(response.getCount()==0){
                        Toast.makeText(LoginActivity.this, "No Entry Exists, Please Register Now", Toast.LENGTH_SHORT).show();
                        name.setText("");
                        pass.setText("");
                        return;
                    }
                    while (response.moveToNext()){
                        if(response.getString(1).equals(ps)){
                            databaseFactory.save_Current_user(nm, response.getString(2), null);
                            startActivity(new Intent(getApplicationContext(), MainHomePage.class));
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Password is not match", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Please fill the forms", Toast.LENGTH_SHORT).show();
                }

                name.setText("");
                pass.setText("");
            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });

    }
}