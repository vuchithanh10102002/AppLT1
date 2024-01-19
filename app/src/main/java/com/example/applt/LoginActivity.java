package com.example.applt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.applt.model.User;

public class LoginActivity extends AppCompatActivity {

    EditText edtUser, edtPassword;
    Button btnLogin, btnRegis;
    TextView btn_forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();


        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisActivity.class);
                startActivity(intent);
                finishAffinity();
            }

        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtUser.getText().toString().trim();
                String pass = edtPassword.getText().toString().trim();

                if (email.equals("") || pass.equals("")) {
                    Toast toast = Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy dủ thông tin!", Toast.LENGTH_SHORT);
                    toast.show();

                    return;
                }

                checkUser(email, pass);
            }
        });


        btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });




    }

    private void initUI() {
        edtUser = (EditText)findViewById(R.id.editTextUser);
        edtPassword = (EditText)findViewById(R.id.editTextPassword);
        btnLogin = (Button)findViewById(R.id.btn_login);
        btnRegis = (Button)findViewById(R.id.btn_regis_login);
        btn_forgot = (TextView) findViewById(R.id.btn_forgot);
    }

    private void checkAdmin(String uid) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User/" + uid);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Log.d("type", user.getType());

                if(user.getType().equals("admin")) {
                    Toast toast = Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT);
                    toast.show();

                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } else {

                    Toast toast = Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT);
                    toast.show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUser(String email, String pass) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user != null) {
                                String uid = user.getUid();

                                checkAdmin(uid);
                            }


                        } else {

                            Toast toast = Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT);
                            toast.show();

                        }
                    }

                });




    }
}