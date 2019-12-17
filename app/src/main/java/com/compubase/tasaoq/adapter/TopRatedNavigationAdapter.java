package com.compubase.tasaoq.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.compubase.tasaoq.R;
import com.compubase.tasaoq.model.TopRatedModel;

import java.util.ArrayList;
import java.util.List;

public class TopRatedNavigationAdapter extends RecyclerView.Adapter<TopRatedNavigationAdapter.ViewHolder> {
    private Context context;
    private List<TopRatedModel>topRatedModelList;

    public TopRatedNavigationAdapter(List<TopRatedModel> topRatedModelList) {
        this.topRatedModelList = topRatedModelList;
    }

    public TopRatedNavigationAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.top_rated_design_navigation, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TopRatedModel topRatedModel = topRatedModelList.get(i);

        viewHolder.txt_discount.setText(topRatedModel.getTxt_sale());
        viewHolder.offer_sale.setText(topRatedModel.getTxt_sale_offer());
        viewHolder.offer.setText(topRatedModel.getTxt_offer());
        viewHolder.rate_num.setText(topRatedModel.getTxt_rate());
        viewHolder.title.setText(topRatedModel.getTitle());

        Glide.with(context).load(topRatedModel.getImg_heart()).into(viewHolder.heart);
        Glide.with(context).load(topRatedModel.getImg_item()).into(viewHolder.img_item);
    }

    @Override
    public int getItemCount() {
        return topRatedModelList != null ? topRatedModelList.size():0;
    }

    public void setAdapter(ArrayList<TopRatedModel> topRatedModels) {
        this.topRatedModelList = topRatedModels;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,rate_num,offer,offer_sale,txt_discount;
        ImageView heart,img_item;
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
