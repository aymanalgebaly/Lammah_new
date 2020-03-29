package com.compubase.tasaoq.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.compubase.tasaoq.R;
import com.compubase.tasaoq.model.ProductsModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolderCart> {

    private TextView tvTotall;
    private Context context;
    private Realm realm;
    private List<ProductsModel> productsModels;
    private List<Double> totalPriceList = new ArrayList<>();


    public CartAdapter(List<ProductsModel> productsModelList, TextView totalPriceCart) {
        this.tvTotall = totalPriceCart;
        this.productsModels = productsModelList;

        if (productsModelList.size() > 0)
            for (int i = 0; i < productsModels.size(); i++) {
                totalPriceList.add(Double.parseDouble(String.valueOf(productsModels.get(i).getPrice())));
            }


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
        viewHolderCart.title.setText(productsModel.getTitle());
        viewHolderCart.desc.setText(productsModel.getDes());
        viewHolderCart.price.setText(productsModel.getPrice());
        viewHolderCart.total_price.setText(productsModel.getPrice());
        Glide.with(context).load(productsModel.getImg1()).placeholder(R.drawable.fastakni_logo).into(viewHolderCart.img);

        viewHolderCart.btn_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quntity = Integer.parseInt((String) viewHolderCart.quntity.getText());
                double total_price = Double.parseDouble((String) viewHolderCart.total_price.getText());

                double price = total_price / quntity;

                if (quntity >= 2) {
                    quntity -= 1;

                    total_price = quntity * price;

                    totalPriceList.set(i, total_price);

                    double allTotalPrice = getTotalPrice();
                    tvTotall.setText(String.valueOf(allTotalPrice));


                    viewHolderCart.quntity.setText(String.valueOf(quntity));
                    viewHolderCart.total_price.setText(String.valueOf(total_price));
                }


            }
        });


        viewHolderCart.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quntity = Integer.parseInt((String) viewHolderCart.quntity.getText());

                quntity += 1;

                double price = quntity * Double.parseDouble(productsModel.getPrice());

                totalPriceList.set(i, price);

                double totalPrice = getTotalPrice();

                tvTotall.setText(String.valueOf(totalPrice));

                viewHolderCart.quntity.setText(String.valueOf(quntity));
                viewHolderCart.total_price.setText(String.valueOf(price));
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


    }

    @Override
    public int getItemCount() {
        return productsModels != null ? productsModels.size() : 0;
    }

    public class ViewHolderCart extends RecyclerView.ViewHolder {
        ImageView img;
        Button btn_min, btn_plus, btn_delete;
        TextView title, desc, price, total_price, quntity;

        public ViewHolderCart(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_product_cart);
            title = itemView.findViewById(R.id.txt_title_cart);
            desc = itemView.findViewById(R.id.txt_des_cart);
            price = itemView.findViewById(R.id.txt_price_value);
            total_price = itemView.findViewById(R.id.txt_total_price_value);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            quntity = itemView.findViewById(R.id.txt_num_value);

            btn_plus = itemView.findViewById(R.id.btn_min);
            btn_min = itemView.findViewById(R.id.btn_plus);

        }
    }

    public double getTotalPrice() {
        double total = 0;

        for (int i = 0; i < totalPriceList.size(); i++) {

            total = total + totalPriceList.get(i);
        }
        return total;
    }
}
