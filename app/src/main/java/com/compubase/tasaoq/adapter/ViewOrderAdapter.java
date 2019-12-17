package com.compubase.tasaoq.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.compubase.tasaoq.R;
import com.compubase.tasaoq.model.ProductOfOrdersModel;
import com.compubase.tasaoq.model.ProductsModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

//import io.realm.Realm;
//import io.realm.RealmResults;

public class ViewOrderAdapter extends RecyclerView.Adapter<ViewOrderAdapter.ViewHolderCart> {

    private Context context;
    private Realm realm;
    private int j = 1;
    private String price;
    private int i1;
    private int _stringVall;

    public ViewOrderAdapter(Context context) {
        this.context = context;
    }

    private List<ProductOfOrdersModel>productsModels;

    public ViewOrderAdapter(List<ProductOfOrdersModel> productsModelList) {
        this.productsModels = productsModelList;
    }

    @NonNull
    @Override
    public ViewHolderCart onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        realm = Realm.getDefaultInstance();
        View view = LayoutInflater.from(context).inflate(R.layout.cart_design, viewGroup, false);
        return new ViewHolderCart(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderCart viewHolderCart, @SuppressLint("RecyclerView") final int i) {
        ProductOfOrdersModel productOfOrdersModel = productsModels.get(i);

        Glide.with(context).load(productOfOrdersModel.getImg1()).placeholder(R.drawable.fastakni_logo).into(viewHolderCart.img);

        Log.i( "onBindViewHolder", String.valueOf(productOfOrdersModel.getId()));

//        viewHolderCart.btn_plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String _stringVal;
//                Log.d("src", "Decreasing value...");
//                if (j > 1) {
//                    j = j - 1;
//                    _stringVal = String.valueOf(j);
//                    viewHolderCart.countity.setText(_stringVal);
//                } else {
//                    Toast.makeText(context, "Value can't be less than 1", Toast.LENGTH_SHORT).show();
//                    Log.d("src", "Value can't be less than 1");
//                }
//
//            }
//
//        });

//        viewHolderCart.btn_min.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Log.d("src", "Increasing value...");
//                j = j + 1;
//                _stringVall = j ;
//                price = productsModel.getPrice();
//
//                int out_price = Integer.parseInt(price);
//
//                i1 = _stringVall * out_price;
//                viewHolderCart.countity.setText(String.valueOf(_stringVall));
//
//
//            }
//        });
//        viewHolderCart.btn_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                realm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(@NonNull Realm realm) {
//                        RealmResults<ProductsModel> result = realm.where(ProductsModel.class).findAll();
//                        result.deleteFromRealm(i);
//                        productsModels.remove(i);
//                        notifyDataSetChanged();
//                    }
//                });
//
//            }
//        });


        Log.i( "onBindViewHolder", String.valueOf(_stringVall));

        viewHolderCart.title.setText(productOfOrdersModel.getTitle());
        viewHolderCart.total_price.setText(productOfOrdersModel.getPrice());
        viewHolderCart.desc.setText(productOfOrdersModel.getDes());
        viewHolderCart.price.setText(productOfOrdersModel.getPrice());

    }

    @Override
    public int getItemCount() {
        return productsModels != null ? productsModels.size():0;
    }

    public class ViewHolderCart extends RecyclerView.ViewHolder {
        ImageView img;
        Button btn_min,btn_plus,btn_delete;
        TextView title,desc,price,total_price,countity;
        public ViewHolderCart(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_product_cart);
            title = itemView.findViewById(R.id.txt_title_cart);
            desc = itemView.findViewById(R.id.txt_des_cart);
            price = itemView.findViewById(R.id.txt_price_value);
            total_price = itemView.findViewById(R.id.txt_total_price_value);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            countity = itemView.findViewById(R.id.txt_num_value);

            btn_min = itemView.findViewById(R.id.btn_min);
            btn_plus = itemView.findViewById(R.id.btn_plus);

        }
    }
}
