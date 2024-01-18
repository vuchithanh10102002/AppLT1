package com.example.applt.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.applt.R;
import com.example.applt.model.Product;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.productViewHolder> {

    private Context mContext;
    private List<Product> mListProduct;
    private OnItemClickListener mListener;

    public ProductAdapter(Context Context, List<Product> mListProduct, OnItemClickListener mListener) {
        this.mContext = Context;
        this.mListProduct = mListProduct;
        this.mListener = mListener;
    }

    public void setData(List<Product> list) {
        mListProduct = list;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new productViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull productViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Product product = mListProduct.get(position);

        if (product == null) {
            return;
        }
        String price, price_old = null;
        float priceProduct = product.getPrice();
        float priceOldProduct = product.getPrice_old();

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

        if (priceOldProduct >= 1000) {
            Currency currency = Currency.getInstance("VND");

            // Sử dụng NumberFormat để định dạng giá tiền Việt Nam và bỏ phần thập phân
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(0);
            numberFormat.setCurrency(currency);
            price_old = numberFormat.format(priceOldProduct) + "đ";
        } else {
            price_old = String.valueOf((int) priceOldProduct);
        }

//        holder.imgProduct.setImageResource(product.getImage());
        Glide.with(mContext)
                .load(product.getImage())
                .into(holder.img_product);
        holder.tvFullname.setText(product.getName());
        holder.tvPrice.setText(price);
        holder.tvPriceold.setText(price_old);
        holder.tvDescription.setText(Html.fromHtml(product.getDescription()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(product);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListProduct != null) {
            return mListProduct.size();
        }
        return 0;
    }

    public class productViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFullname, tvPrice, tvPriceold, tvDescription, tvContent;

        private ImageView img_product;
        public productViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullname = itemView.findViewById(R.id.t_fullname);
            tvPrice = itemView.findViewById(R.id.price);
            tvPriceold = itemView.findViewById(R.id.t_price_old);
            tvDescription = itemView.findViewById(R.id.tv_description);
            img_product = itemView.findViewById(R.id.img_product);
        }

    }
}
