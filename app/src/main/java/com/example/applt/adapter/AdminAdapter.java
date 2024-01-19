package com.example.applt.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.applt.R;
import com.example.applt.model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminAdapter extends FirebaseRecyclerAdapter<Product, AdminAdapter.myViewHolder> {
    public AdminAdapter(@NonNull FirebaseRecyclerOptions<Product> options) {
        super(options);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_admin,parent,false);
        return new myViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Product model) {

        holder.name.setText(model.getName());
        holder.description.setText(model.getDescription());
        holder.price.setText(String.valueOf(model.getPrice()));
        holder.price_old.setText(String.valueOf(model.getPrice_old()));
        holder.content.setText(model.getContent());


        Glide.with(holder.image.getContext())
                .load(model.getImage())
                .placeholder(R.drawable.ic_launcher_foreground)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.image);

        holder.btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.image.getContext())
                        .setContentHolder(new ViewHolder(R.layout.popup_update))
                        .setExpanded(true,1200)
                        .create();

                View view = dialogPlus.getHolderView();

                EditText name = view.findViewById(R.id.txtname);
                EditText mota = view.findViewById(R.id.txtmota);
                EditText gia = view.findViewById(R.id.txtgia);
                EditText surl = view.findViewById(R.id.txtimg);
                EditText giacu = view.findViewById(R.id.txtgiacu);
                EditText content = view.findViewById(R.id.txtcontent);


                Button btnUpdate = view.findViewById(R.id.btnUpdate);

                name.setText(model.getName());
                mota.setText(model.getDescription());
                gia.setText(String.valueOf(model.getPrice()));
                giacu.setText(String.valueOf(model.getPrice_old()));
                surl.setText(model.getImage());
                content.setText(model.getContent());


                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name",name.getText().toString());
                        map.put("description",mota.getText().toString());
                        map.put("price",Long.parseLong(gia.getText().toString()));
                        map.put("price_old",Long.parseLong(gia.getText().toString()));
                        map.put("image",surl.getText().toString());
                        map.put("content",content.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Product")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.name.getContext(),"Sửa thành công!!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.name.getContext(),"Sửa không thành công!!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        holder.btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                builder.setTitle("Bạn có chắc chắn ko?");

                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("Product")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.name.getContext(),"Cancelled",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView name, description, price, price_old,content;

        Button btnsua,btnxoa;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            image = (CircleImageView)itemView.findViewById(R.id.img1);
            name = (TextView) itemView.findViewById(R.id.nametext);
            description = (TextView) itemView.findViewById(R.id.mota);
            price = (TextView) itemView.findViewById(R.id.gia);
            price_old = (TextView) itemView.findViewById(R.id.giacu);
            content = (TextView) itemView.findViewById(R.id.content);

            btnsua = (Button) itemView.findViewById(R.id.btnsua);
            btnxoa = (Button) itemView.findViewById(R.id.btnxoa);
        }
    }
}
