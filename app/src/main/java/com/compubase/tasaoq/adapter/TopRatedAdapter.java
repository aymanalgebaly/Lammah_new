package com.compubase.tasaoq.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.compubase.tasaoq.R;
import com.compubase.tasaoq.data.API;
import com.compubase.tasaoq.helper.RetrofitClient;
import com.compubase.tasaoq.helper.TinyDB;
import com.compubase.tasaoq.model.ProductsModel;
import com.compubase.tasaoq.ui.activities.HomeActivity;
import com.compubase.tasaoq.ui.fragments.SelectedItemFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TopRatedAdapter extends RecyclerView.Adapter<TopRatedAdapter.ViewHolder> {
    private Context context;
    private List<ProductsModel>productsModels;
    private SharedPreferences preferences;
    private String id_user;
    private Integer id;
    private String isfav;

    public TopRatedAdapter(List<ProductsModel> productsModels) {
        this.productsModels = productsModels;
    }

    public void setDataList(List<ProductsModel> productsModels) {
        this.productsModels = productsModels;
        notifyDataSetChanged();
    }

    public TopRatedAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.top_rated_design, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final ProductsModel productsModel = productsModels.get(i);

        String priceDiscount = productsModel.getPriceDiscount();
        String s = priceDiscount + "%";
        viewHolder.txt_discount.setText(s);
        viewHolder.offer_sale.setText(productsModel.getPriceDiscount());
        viewHolder.offer.setText(productsModel.getPrice());

        String rate = productsModel.getRate();

        if (rate.equals("")){
            viewHolder.ratingBar.setRating(0);
        }else {

            viewHolder.ratingBar.setRating(Float.parseFloat(rate));

        }
        viewHolder.title.setText(productsModel.getTitle());

//        Glide.with(context).load(productsModel.getImg1()).placeholder(R.drawable.heart).into(viewHolder.heart);
        Glide.with(context).load(productsModel.getImg1()).into(viewHolder.img_item);

        preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);

        id_user = preferences.getString("id", "");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = productsModel.getId();
//                Toast.makeText(context, String.valueOf(id), Toast.LENGTH_SHORT).show();
                isfav = productsModel.getIsfav();
                HomeActivity homeActivity = (HomeActivity) context;

//                Bundle bundle=new Bundle();
//                bundle.putParcelable("pro_model",productsModel); //key and value
//set Fragmentclass Arguments
                SelectedItemFragment selectedItemFragment = new SelectedItemFragment();
//                selectedItemFragment.setArguments(bundle);
                homeActivity.displaySelectedFragmentWithBack(selectedItemFragment);

                TinyDB tinyDB = new TinyDB(context);
                tinyDB.putString("pic",productsModel.getImg1());
                tinyDB.putString("pic1",productsModel.getImg2());
                tinyDB.putString("pic2",productsModel.getImg3());
                tinyDB.putString("name",productsModel.getTitle());
                tinyDB.putString("rate",productsModel.getNumberRate());
                tinyDB.putString("price",productsModel.getPrice());
                tinyDB.putString("des",productsModel.getDes());
                tinyDB.putString("dis",productsModel.getPriceDiscount());
                tinyDB.putString("id_pro", String.valueOf(id));
            }
        });
        viewHolder.heart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (viewHolder.heart.isChecked()){

                    Retrofit retrofit = RetrofitClient.getInstant();
                    API api = retrofit.create(API.class);
                    Call<ResponseBody> responseBodyCall = api.insertFav(id_user, String.valueOf(id));
                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()){
                                try {
                                    assert response.body() != null;
                                    String string = response.body().string();
                                    if (string.equals("True")){

                                        Toast.makeText(context, "Added to favourite", Toast.LENGTH_SHORT).show();

                                        if ("yes".equals(isfav)){
                                            viewHolder.heart.setBackgroundResource(R.drawable.heart_on);
                                        }else {
                                            viewHolder.heart.setBackgroundResource(R.drawable.heart_off);
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

//    private void addToFav(Integer id, final String fav) {
//        Retrofit retrofit = RetrofitClient.getInstant();
//        API api = retrofit.create(API.class);
//        Call<ResponseBody> responseBodyCall = api.insertFav(id_user, String.valueOf(id));
//        responseBodyCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()){
//                    try {
//                        assert response.body() != null;
//                        String string = response.body().string();
//                        if (string.equals("True")){
//
//                            Toast.makeText(context, "Added to favourite", Toast.LENGTH_SHORT).show();
//
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    public int getItemCount() {
        return productsModels != null ? productsModels.size():0;
    }

    public void setAdapter(ArrayList<ProductsModel> topRatedModels) {
        this.productsModels = topRatedModels;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,rate_num,offer,offer_sale,txt_discount;
        ImageView img_item;
        CheckBox heart;
        RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.txt_name_of_item);
            rate_num = itemView.findViewById(R.id.txt_percent);
            offer = itemView.findViewById(R.id.txt_num);
            offer_sale = itemView.findViewById(R.id.txt_num_sale);
            txt_discount = itemView.findViewById(R.id.txt_percent_sale);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            heart = itemView.findViewById(R.id.img_heart);
            img_item = itemView.findViewById(R.id.img_item);


        }
    }
}
