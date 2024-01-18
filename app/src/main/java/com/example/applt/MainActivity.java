package com.example.applt;

import static android.app.appsearch.AppSearchResult.RESULT_OK;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final int MY_REQUEST_CODE = 10;

    private UserFragment mUserFragement = new UserFragment();
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    public static String userId = firebaseUser.getUid();
    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();

                        if (intent == null) {
                            return ;
                        }

                        Uri uri = intent.getData();
                        mUserFragement.setmUri(uri);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            mUserFragement.setBitmapImageView(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navi);
        bottomNavigationView.setSelectedItemId(R.id.icon_home);

        replaceFragment(new HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.icon_home) {
//                Toast.makeText(MainActivity.this, "Trang chủ", Toast.LENGTH_SHORT).show();
                replaceFragment(new HomeFragment());
            }

            if(item.getItemId() == R.id.icon_phone) {
//                Toast.makeText(MainActivity.this, "Liên hệ", Toast.LENGTH_SHORT).show();
                replaceFragment(new PhoneFragment());
            }

            if(item.getItemId() == R.id.icon_menu) {
//                Toast.makeText(MainActivity.this, "Danh mục", Toast.LENGTH_SHORT).show();
//                replaceFragment(new );
            }

            if(item.getItemId() == R.id.icon_user) {
//                Toast.makeText(MainActivity.this, "Tài khoản", Toast.LENGTH_SHORT).show();
                replaceFragment(mUserFragement);
            }

            if(item.getItemId() == R.id.icon_cart) {
//                Toast.makeText(MainActivity.this, "Giỏ hàng", Toast.LENGTH_SHORT).show();
                replaceFragment(new CartFragment());
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onClickOpenGallary();
            }
        }
    }

    public void onClickOpenGallary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }
}