package com.example.applt;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.applt.model.Cart;
import com.example.applt.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Currency;

public class ProductDetailFragment extends Fragment {

    private String productId;

    private TextView product_name, product_price, product_priceOld, product_content, quantity;
    private ImageView product_img;
    private ImageButton btn_addcart, btn_minus, btn_plus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product_detail, container, false);

        getProductDetail(root);

        if (getArguments() != null) {
            productId = getArguments().getString("productId");
        }


        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer qty = Integer.parseInt(quantity.getText().toString().trim());
                if(qty.equals(1)) {

                } else {
                    qty--;
                    quantity.setText(qty.toString());
                }
            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer qty = Integer.parseInt(quantity.getText().toString().trim());
                if(qty.equals(0)) {

                } else {
                    qty++;
                    quantity.setText(qty.toString());
                }
            }
        });

        setProductDetail(productId);
        // Inflate the layout for this fragment
        return root;
    }

    private void setProductDetail(String productId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Product");

        DatabaseReference productsRef = myRef.child(productId);



        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Product product = snapshot.getValue(Product.class);

                    Glide.with(requireContext())
                            .load(product.getImage())
                            .into(product_img);

                    product_name.setText(product.getName());

                    product_content.setText(HtmlCompat.fromHtml(product.getContent(), HtmlCompat.FROM_HTML_MODE_LEGACY));
//                    product_img.setImageResource(product.getImage());

                    float priceProductNumber = product.getPrice();
                    float priceOldProductNumber = product.getPrice();
                    String stringPrice, stringPriceOld;

                    if (priceProductNumber >= 1000) {
                        Currency currency = Currency.getInstance("VND");

                        // Sử dụng NumberFormat để định dạng giá tiền Việt Nam và bỏ phần thập phân
                        NumberFormat numberFormat = NumberFormat.getInstance();
                        numberFormat.setMaximumFractionDigits(0);
                        numberFormat.setCurrency(currency);
                        stringPrice = numberFormat.format(priceProductNumber) + "đ";
                    } else {
                        stringPrice = String.valueOf((int) priceProductNumber);
                    }

                    if (priceOldProductNumber >= 1000) {
                        Currency currency = Currency.getInstance("VND");

                        // Sử dụng NumberFormat để định dạng giá tiền Việt Nam và bỏ phần thập phân
                        NumberFormat numberFormat = NumberFormat.getInstance();
                        numberFormat.setMaximumFractionDigits(0);
                        numberFormat.setCurrency(currency);
                        stringPriceOld = numberFormat.format(priceOldProductNumber) + "đ";
                    } else {
                        stringPriceOld = String.valueOf((int) priceOldProductNumber);
                    }

                    product_price.setText(stringPrice);
                    product_priceOld.setText(stringPriceOld);

                    btn_addcart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Integer qty = Integer.parseInt(quantity.getText().toString().trim());
                            onClickAddtoCart(product, qty);
                        }
                    });

                } else {

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getProductDetail(View view) {
        product_name = view.findViewById(R.id.t_fullname);
        product_price = view.findViewById(R.id.price);
        product_priceOld = view.findViewById(R.id.t_price_old);
        product_content  = view.findViewById(R.id.t_content);
        product_img = view.findViewById(R.id.product_img);
        btn_addcart = view.findViewById(R.id.btn_addcart);
        btn_minus = view.findViewById(R.id.btn_minus);
        btn_plus = view.findViewById(R.id.btn_plus);
        quantity = view.findViewById(R.id.quantity);
    }

    private void onClickAddtoCart(Product product, int quantity) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Cart");

        String pathObject = String.valueOf(product.getId());

        Cart cartProduct = new Cart(product, quantity);

        myRef.child(pathObject).setValue(cartProduct, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(requireContext(), "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}