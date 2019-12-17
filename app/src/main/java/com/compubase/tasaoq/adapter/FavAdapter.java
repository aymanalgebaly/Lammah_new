package com.compubase.tasaoq.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.compubase.tasaoq.R;
import com.compubase.tasaoq.helper.TinyDB;
import com.compubase.tasaoq.model.FavModel;
import com.compubase.tasaoq.ui.activities.HomeActivity;
import com.compubase.tasaoq.ui.fragments.SelectedItemFragment;

import java.util.ArrayList;
import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {
    private Context context;
    private List<FavModel>productsModels;
    private SharedPreferences preferences;
    private String id_user;

    public FavAdapter(List<FavModel> productsModels) {
        this.productsModels = productsModels;
    }

    public FavAdapter(Context context) {
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
        final FavModel productsModel = productsModels.get(i);

        String priceDiscount = productsModel.getPriceDiscount();
        String s = priceDiscount + "%";
        viewHolder.txt_discount.setText(s);
        viewHolder.offer_sale.setText(productsModel.getPriceDiscount());
        viewHolder.offer.setText(productsModel.getPrice());
        viewHolder.ratingBar.setRating(Float.parseFloat(productsModel.getRate()));
        viewHolder.title.setText(productsModel.getTitle());
        viewHolder.heart.setBackgroundResource(R.drawable.heart_on);
        Glide.with(context).load(productsModel.getImg1()).into(viewHolder.img_item);

        preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);

        id_user = preferences.getString("id", "");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeActivity homeActivity = (HomeActivity) context;

                SelectedItemFragment selectedItemFragment = new SelectedItemFragment();
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
                tinyDB.putString("id", String.valueOf(productsModel.getId()));
            }
        });
        viewHolder.heart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (viewHolder.heart.isChecked()){

                }
            }
        });
    }

//    private void addToFav(Integer id) {
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
//                            Toast.makeText(context, "Added to favourite", Toast.LENGTH_SHORT).show();
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

    public void setAdapter(ArrayList<FavModel> topRatedModels) {
        this.productsModels = topRatedModels;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,rate_num,offer,offer_sale,txt_discount;
        ImageView img_item;
        RatingBar ratingBar;
        CheckBox heart;
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
