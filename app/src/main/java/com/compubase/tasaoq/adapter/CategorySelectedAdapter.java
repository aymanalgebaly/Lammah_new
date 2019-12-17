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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.compubase.tasaoq.R;
import com.compubase.tasaoq.data.API;
import com.compubase.tasaoq.helper.RetrofitClient;
import com.compubase.tasaoq.helper.TinyDB;
import com.compubase.tasaoq.model.CategoryModel;
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

public class CategorySelectedAdapter extends RecyclerView.Adapter<CategorySelectedAdapter.ViewHolder> {
    private Context context;
    private List<CategoryModel>categoryModelList;
    private SharedPreferences preferences;
    private String id_user;

    public CategorySelectedAdapter(List<CategoryModel> categoryModels) {
        this.categoryModelList = categoryModels;
    }

    public CategorySelectedAdapter(Context context) {
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
        final  CategoryModel categoryModel = categoryModelList.get(i);

        viewHolder.txt_discount.setText(categoryModel.getPriceDiscount());
//        viewHolder.offer_sale.setText(productsModel.getTxt_sale_offer());
        viewHolder.offer.setText(categoryModel.getPrice());
        viewHolder.rate_num.setText(categoryModel.getNumberRate());
        viewHolder.title.setText(categoryModel.getTitle());

//        Glide.with(context).load(productsModel.getImg1()).placeholder(R.drawable.heart).into(viewHolder.heart);
        Glide.with(context).load(categoryModel.getImg1()).placeholder(R.drawable.anti_man).into(viewHolder.img_item);

        preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);

        id_user = preferences.getString("id", "");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeActivity homeActivity = (HomeActivity) context;

                SelectedItemFragment selectedItemFragment = new SelectedItemFragment();
                homeActivity.displaySelectedFragmentWithBack(selectedItemFragment);

                TinyDB tinyDB = new TinyDB(context);
                tinyDB.putString("pic",categoryModel.getImg1());
                tinyDB.putString("pic1",categoryModel.getImg2());
                tinyDB.putString("pic2",categoryModel.getImg3());
                tinyDB.putString("name",categoryModel.getTitle());
                tinyDB.putString("rate",categoryModel.getNumberRate());
                tinyDB.putString("price",categoryModel.getPrice());
                tinyDB.putString("des",categoryModel.getDes());
                tinyDB.putString("dis",categoryModel.getPriceDiscount());
                tinyDB.putString("cat", String.valueOf(categoryModel));
            }
        });
        viewHolder.heart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (viewHolder.heart.isChecked()){
                    addToFav(categoryModel.getId());
                }
            }
        });
    }

    private void addToFav(Integer id) {
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

    @Override
    public int getItemCount() {
        return categoryModelList != null ? categoryModelList.size():0;
    }

    public void setAdapter(ArrayList<CategoryModel> categoryModelArrayList) {
        this.categoryModelList = categoryModelArrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,rate_num,offer,offer_sale,txt_discount;
        ImageView img_item;
        CheckBox heart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.txt_name_of_item);
            rate_num = itemView.findViewById(R.id.txt_percent);
            offer = itemView.findViewById(R.id.txt_num);
            offer_sale = itemView.findViewById(R.id.txt_num_sale);
            txt_discount = itemView.findViewById(R.id.txt_percent_sale);

            heart = itemView.findViewById(R.id.img_heart);
            img_item = itemView.findViewById(R.id.img_item);


        }
    }
}
