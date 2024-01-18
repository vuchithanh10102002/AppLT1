package com.example.applt;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.applt.adapter.CartAdapter;
import com.example.applt.adapter.ProductAdapter;
import com.example.applt.model.Bill;
import com.example.applt.model.Cart;
import com.example.applt.model.Product;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    RecyclerView rcv_itemCart;
    CartAdapter cartAdapter;

    Button btn_tt;

    List<Cart> cartProducts = new ArrayList<>();
    private int totalPrice = 0;

    private String userId = MainActivity.userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        initUi(root);

        getListProduct();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1);
        rcv_itemCart.setLayoutManager(gridLayoutManager);
        cartAdapter = new CartAdapter(requireContext(), cartProducts);
        rcv_itemCart.setAdapter(cartAdapter);

        btn_tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOnBill(cartProducts, userId, String.valueOf(totalPrice));

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Cart");

                myRef.removeValue();

                cartAdapter.notifyDataSetChanged();
            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    private void addOnBill(List<Cart> cartProducts, String userId, String total) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Bill");

        Bill cartProduct = new Bill(userId, total, cartProducts);

        myRef.push().setValue(cartProduct, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(requireContext(), "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUi(View view) {
        btn_tt = view.findViewById(R.id.btn_tt);
        rcv_itemCart = view.findViewById(R.id.rcv_itemCart);
    }

    private void getListProduct() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Cart");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Cart cartProduct = snapshot.getValue(Cart.class);
                totalPrice = totalPrice + cartProduct.getProduct().getPrice() * cartProduct.getQuantity();
                cartProducts.add(cartProduct);

                cartAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Cart cartProduct = snapshot.getValue(Cart.class);


//                cartProducts.add(cartProduct);

                Log.d("aloo", cartProduct.toString());
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}