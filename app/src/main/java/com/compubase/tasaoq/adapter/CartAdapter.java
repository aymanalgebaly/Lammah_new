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
import com.compubase.tasaoq.helper.TinyDB;
import com.compubase.tasaoq.model.ProductsModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

//import io.realm.Realm;
//import io.realm.RealmResults;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolderCart> {

    private Context context;
    private Realm realm;
    private int j = 1;
    private String price;
    private int i1;
    private int _stringVall;
    private int i2;
    private int _stringVal;
    private int i3;
    private int integer;
    private TextView totall;
    private int i4;
    private Integer price_frag;
    private String priceeeee;


    public CartAdapter(Context context) {
        this.context = context;
    }

    private List<ProductsModel> productsModels;

    public CartAdapter(List<ProductsModel> productsModelList, TextView totalPriceCart) {
        this.totall = totalPriceCart;
        this.productsModels = productsModelList;
    }

    public CartAdapter(List<ProductsModel> productsModelList) {
        productsModels = productsModelList;
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
        final ProductsModel productsModel = productsModels.get(i);

        Glide.with(context).load(productsModel.getImg1()).placeholder(R.drawable.fastakni_logo).into(viewHolderCart.img);

        String s = totall.getText().toString();
        price_frag = Integer.valueOf(s);

        Log.i( "soooooooo",s);

        priceeeee = productsModel.getPrice();
//        totall.setText(price);

        Log.i("onBindViewHolder", String.valueOf(productsModel.getId()));

        viewHolderCart.btn_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("src", "Increasing value...");
                j = j + 1;
                Log.i("onClick", String.valueOf(j));
                _stringVall = j;
                viewHolderCart.countity.setText(String.valueOf(_stringVall));

                String price = productsModel.getPrice();
                Integer integer = Integer.valueOf(price);
                i2 = _stringVall * integer;


                viewHolderCart.total_price.setText(String.valueOf(i2));


//                productsModel.setPrice(viewHolderCart.total_price.getText().toString());
//                productsModels.set(i, productsModel);
//                totall.setText(String.valueOf(getTotalPrice()));


            }
        });

        viewHolderCart.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("src", "Decreasing value...");
                if (j > 1) {
                    j = j - 1;
                    _stringVal = j;

                    if (!(_stringVal == 1)) {
                        String price = productsModel.getPrice();
                        CartAdapter.this.integer = i2;
                        i3 = CartAdapter.this.integer - Integer.valueOf(price);
                        viewHolderCart.total_price.setText(String.valueOf(i3));

//                        productsModel.setPrice(viewHolderCart.total_price.getText().toString());
//                        productsModels.set(i, productsModel);
//                        totall.setText(String.valueOf(getTotalPrice()));


                    } else {
                        String price = productsModel.getPrice();
                        Integer int_price = Integer.valueOf(price);
                        int i6 = int_price / _stringVal;
                        int i5 = i6;
                        viewHolderCart.total_price.setText(String.valueOf(i6));
//                        String price1 = productsModel.getPrice();
//                        Integer integer = Integer.valueOf(price1);
//                        int i5 = i3 - price_frag;
//                        productsModel.setPrice(String.valueOf(viewHolderCart.price.getText().toString()));
//                        productsModels.set(i, productsModel);
//                        totall.setText(String.valueOf(getTotalPrice()));

//                        viewHolderCart.total_price.setText(String.valueOf(getTotalPrice()));

                    }

                    viewHolderCart.countity.setText(String.valueOf(_stringVal));

                } else {
                    Toast.makeText(context, "Value can't be less than 1", Toast.LENGTH_SHORT).show();
                    Log.d("src", "Value can't be less than 1");
                }

            }

        });

        viewHolderCart.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        RealmResults<ProductsModel> result = realm.where(ProductsModel.class).findAll();
                        result.deleteFromRealm(i);
                        productsModels.remove(i);
                        notifyDataSetChanged();
                    }
                });

            }
        });

        viewHolderCart.title.setText(productsModel.getTitle());
        viewHolderCart.desc.setText(productsModel.getDes());
        viewHolderCart.price.setText(productsModel.getPrice());
        viewHolderCart.total_price.setText(productsModel.getPrice());

    }

    @Override
    public int getItemCount() {
        return productsModels != null ? productsModels.size() : 0;
    }

    public class ViewHolderCart extends RecyclerView.ViewHolder {
        ImageView img;
        Button btn_min, btn_plus, btn_delete;
        TextView title, desc, price, total_price, countity;

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

    public double getTotalPrice() {
        double total = 0;

        for (int i = 0; i < productsModels.size(); i++) {

            total = total + Double.parseDouble(productsModels.get(i).getPrice());
        }
        return total;
    }
}
