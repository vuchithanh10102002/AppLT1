package com.example.applt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.applt.model.Bill;
import com.example.applt.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisActivity extends AppCompatActivity {

    Button btnLogin, btnRegis, btnReturn;
    EditText edtPassword, edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);
        initUI();

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regisUser();
            }
        });

    }

    private void initUI() {
        btnRegis = (Button)findViewById(R.id.btn_regis);
        edtPassword = (EditText)findViewById(R.id.editTextPassword);
        edtEmail = (EditText)findViewById(R.id.editTextEmail);
    }

    private void regisUser() {
        String email = edtEmail.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();

        if (email.equals("") || pass.equals("")) {
            Toast toast = Toast.makeText(RegisActivity.this, "Vui lòng nhập đầy dủ thông tin!", Toast.LENGTH_SHORT);
            toast.show();
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user != null) {
                                String uid = user.getUid();

                                User user1 = new User("", email, pass, "", "user");

                                addUserFromDb(user1, uid);
                                Toast toast = Toast.makeText(RegisActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT);
                                toast.show();

                                Intent intent = new Intent(RegisActivity.this, LoginActivity.class);
                                startActivity(intent);

                                finishAffinity();
                            }

                        } else {
                            Toast.makeText(RegisActivity.this, "Tài khoản đã tồn tại!", Toast.LENGTH_LONG).show();
                        }
                    }

                });


    }

    private void addUserFromDb(User user, String uid) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");

        myRef.child(uid).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(RegisActivity.this, "Đăng kí thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}