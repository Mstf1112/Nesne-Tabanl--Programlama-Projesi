package com.example.logsignsql;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logsignsql.databinding.ActivityLoginBinding;
public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    AuthDB databaseHelper;
    @Override
    //BİNDİNG METODUNA ATMA
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper = new AuthDB(this);
        //LOGİN BUTONU
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.loginEmail.getText().toString();
                String password = binding.loginPassword.getText().toString();
                if(email.equals("")||password.equals(""))
                    Toast.makeText(com.example.logsignsql.LoginActivity.this, "TÜM ALANLAR DOLDURULMALI!", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkCredentials = databaseHelper.checkEmailPassword(email, password);
                    if(checkCredentials == true){
                        Toast.makeText(com.example.logsignsql.LoginActivity.this, "GİRİŞ BAŞARILI!", Toast.LENGTH_SHORT).show();
                        try {
                            Intent intent  = new Intent(LoginActivity.this, Home.class);
                            startActivity(intent);
                            System.out.println("gitti");
                        }
                       catch(Exception a){
                           System.out.println("gidemedi");
                       }


                    }else{
                        Toast.makeText(com.example.logsignsql.LoginActivity.this, "GEÇERSİZ KARAKTER", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.signupRedirectText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {Intent intent  = new Intent(LoginActivity.this, SignupActivity.class);
                        startActivity(intent);
                    }
                });

            }
        }

