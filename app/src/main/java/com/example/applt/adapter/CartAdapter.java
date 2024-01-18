package com.example.applt.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.applt.R;
import com.example.applt.model.Cart;
import com.example.applt.model.Product;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.cartViewHolder> {
    private Context mContext;
    private List<Cart> mListProductCart;

    public CartAdapter(Context mContext, List<Cart> mListProductCart) {
        this.mContext = mContext;
        this.mListProductCart = mListProductCart;
    }



    @NonNull
    @Override
    public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartAdapter.cartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cartViewHolder holder, int position) {
        Cart product = mListProductCart.get(position);

        String price = null;
        float priceProduct = product.getProduct().getPrice();

        if (priceProduct >= 1000) {
            Currency currency = Currency.getInstance("VND");

            // Sử dụng NumberFormat để định dạng giá tiền Việt Nam và bỏ phần thập phân
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(0);
            numberFormat.setCurrency(currency);
            price = numberFormat.format(priceProduct) + "đ";
        } else {
            price = String.valueOf((int) priceProduct);
        }

        Glide.with(mContext)
                .load(product.getProduct().getImage())
                .into(holder.cart_img);
        holder.cart_name.setText(product.getProduct().getName());
        holder.cart_price.setText(price);
        holder.qty_cart.setText(String.valueOf(product.getQuantity()));


        holder.btn_removeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Cart/" + product.getProduct().getId());

                myRef.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListProductCart != null) {
            return mListProductCart.size();
        }
        return 0;
    }

    public class cartViewHolder extends RecyclerView.ViewHolder {
        ImageView cart_img;
        TextView cart_name, cart_price, qty_cart, btn_removeCart;
        public cartViewHolder(@NonNull View itemView) {
            super(itemView);
            cart_img = itemView.findViewById(R.id.cart_img);
            cart_name = itemView.findViewById(R.id.cart_name);
            cart_price = itemView.findViewById(R.id.cart_price);
            qty_cart = itemView.findViewById(R.id.qty_cart);
            btn_removeCart = itemView.findViewById(R.id.btn_removeCart);
        }

    }
}
