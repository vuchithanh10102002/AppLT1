package com.example.applt;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.applt.adapter.ProductAdapter;
import com.example.applt.adapter.SliderAdapter;
import com.example.applt.model.Product;
import com.example.applt.model.Slider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment {

    private ViewPager mViewPager;
    private CircleIndicator mCircleIndicator;
    private RecyclerView rcvProduct;
    private ProductAdapter productAdapter;
    private SliderAdapter sliderAdapter;

    private List<String> slides = new ArrayList<>();
    private List<Product> products = new ArrayList<>();

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("SliderHome");
    private ProductAdapter.OnItemClickListener onItemClickListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initUI(root);

        //carousel
        getListPhoto();

        sliderAdapter = new SliderAdapter(requireContext(), slides);
        mViewPager.setAdapter(sliderAdapter);

        getListProduct();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        rcvProduct.setLayoutManager(gridLayoutManager);
        productAdapter = new ProductAdapter(requireContext(), products, onItemClickListener);
        rcvProduct.setAdapter(productAdapter);

        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                onClickItemGotoDetail(product);
            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    private void initUI(View view) {
        mViewPager = view.findViewById(R.id.view_pager);
        mCircleIndicator = view.findViewById(R.id.circle_indicator);
        rcvProduct = view.findViewById(R.id.rcv_product);
    }

    private void getListPhoto() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Slider");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue() instanceof HashMap) {
                        HashMap<String, Object> slideData = (HashMap<String, Object>) snapshot.getValue();
                        // Lấy giá trị từ HashMap và chuyển đổi thành String
                        String slide = (String) slideData.get("imageUrl");
                        slides.add(slide);
                        mCircleIndicator.setViewPager(mViewPager);
                    }

                }

                sliderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed to load slides", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getListProduct() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Product");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    Product product = datasnapshot.getValue(Product.class);
                    products.add(product);
                }

                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void onClickItemGotoDetail(Product product) {
        Fragment newFragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putString("productId", String.valueOf(product.getId()));
        newFragment.setArguments(args);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void setOnClickItem() {};

}