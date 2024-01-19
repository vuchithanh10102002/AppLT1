package com.example.applt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Add_Activity extends AppCompatActivity {

    EditText name, mota, gia, surl,giacu,content;
    Button btnAdd, btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        name = (EditText) findViewById(R.id.name);
        mota = (EditText) findViewById(R.id.txtmota);
        gia = (EditText) findViewById(R.id.txtgia);
        surl = (EditText) findViewById(R.id.txtimg);
        giacu = (EditText) findViewById(R.id.txtgiacu);
        content = (EditText) findViewById(R.id.txtcontent);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
                clearAll();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Add_Activity.this, AdminActivity.class));
            }
        });

    }

    private void insertData(){
        Map<String,Object> map = new HashMap<>();
        map.put("name",name.getText().toString());
        map.put("description",mota.getText().toString());
        map.put("price",Long.parseLong(gia.getText().toString()));
        map.put("image",surl.getText().toString());
        map.put("price_old",Long.parseLong(giacu.getText().toString()));
        map.put("content",content.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Product").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Add_Activity.this, "Thêm thành công!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Add_Activity.this, "Thêm không thành công!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearAll(){
        name.setText("");
        mota.setText("");
        gia.setText("");
        surl.setText("");
        giacu.setText("");
        content.setText("");
    }
}