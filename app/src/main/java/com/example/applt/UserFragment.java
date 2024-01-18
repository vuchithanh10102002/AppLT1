package com.example.applt;

import static android.app.appsearch.AppSearchResult.RESULT_OK;

import static com.example.applt.MainActivity.MY_REQUEST_CODE;
import static com.example.applt.MainActivity.userId;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserFragment extends Fragment {

    LinearLayout btn_logout, btn_updateUser, btn_changePassword;
    private TextView username, t_email;
    private EditText fullname;
    private ImageView avatar;

    private Uri mUri;
    private String mEmailUser, mPasswordUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_user, container, false);
        initUI(root);

        showUserInformation();



        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });

        btn_updateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile();
            }
        });

        btn_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                ChangePasswordFragment mChangePasswordFragment = new ChangePasswordFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, mChangePasswordFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    private void onClickUpdateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return ;
        }

        if (mUri == null) {
            mUri = user.getPhotoUrl();

        }

        String strUserName = fullname.getText().toString().trim();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(strUserName)
                .setPhotoUri(mUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Cập nhật tài khoản thành công!", Toast.LENGTH_SHORT).show();
                            showUserInformation();
                        }
                    }

                });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User/" + userId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("username", strUserName);
        updates.put("img", mUri.toString());

        myRef.updateChildren(updates);
    }

    public void setmUri(Uri mUri) {
        this.mUri = mUri;
    }


    private void onClickRequestPermission() {
        MainActivity mainActivity = (MainActivity) requireActivity();

        if(mainActivity == null) {
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mainActivity.onClickOpenGallary();

            return;
        }

        if (requireContext().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mainActivity.onClickOpenGallary();
        } else {
            String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(permissions, MY_REQUEST_CODE);
        }
    }



    private void initUI(View view) {
        btn_logout = view.findViewById(R.id.btn_logout);
        username = view.findViewById(R.id.t_username);
        fullname = view.findViewById(R.id.t_fullname);
        t_email = view.findViewById(R.id.t_email);
        avatar = view.findViewById(R.id.img_avatar);
        btn_updateUser = view.findViewById(R.id.btn_updateUser);
        btn_changePassword = view.findViewById(R.id.btn_changePassword);
    }

    private void showUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        username.setText("Xin chào, " + name);
        fullname.setText(name);


        Glide.with(this).load(photoUrl).error(R.drawable.user_default).into(avatar);

        t_email.setText(email);
    }

    public void setBitmapImageView(Bitmap bitmapImageView) {
        avatar.setImageBitmap(bitmapImageView);
    }
}