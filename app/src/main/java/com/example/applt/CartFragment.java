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
    static TextView t_total;

    List<Cart> cartProducts = new ArrayList<>();
    private int totalPrice = 0;

    private String userId = MainActivity.userId;

    public static void updatePrice(int price) {
        t_total.setText(String.valueOf(price) + "đ");
    }

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
                String total = t_total.getText().toString().trim();



                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Cart");

                myRef.removeValue();
                addOnBill(cartProducts, userId, total);
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
                getListProduct();
                t_total.setText("0đ");

                Toast.makeText(requireContext(), "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUi(View view) {
        btn_tt = view.findViewById(R.id.btn_tt);
        rcv_itemCart = view.findViewById(R.id.rcv_itemCart);
        t_total = view.findViewById(R.id.t_total);
    }

    private void getListProduct() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Cart");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Cart> updatedCartList = new ArrayList<>();

                for (DataSnapshot cartItemSnapshot : snapshot.getChildren()) {
                    DataSnapshot productSnapshot = cartItemSnapshot.child("product");
                    DataSnapshot quantity = cartItemSnapshot.child("quantity");
                    Product product = productSnapshot.getValue(Product.class);

                    if (product != null) {
                        Cart cartItem = new Cart(product, quantity.getValue(Integer.class));
                        totalPrice = totalPrice + product.getPrice() * quantity.getValue(Integer.class);
                        cartItem.setIdCart(cartItemSnapshot.getKey());
                        updatedCartList.add(cartItem);
                    }
                }

                t_total.setText(totalPrice + "đ");

                // Cập nhật giỏ hàng và thông báo cho Adapter
                cartProducts.clear();
                cartProducts.addAll(updatedCartList);
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}