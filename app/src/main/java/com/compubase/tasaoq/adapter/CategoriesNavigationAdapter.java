package com.compubase.tasaoq.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.compubase.tasaoq.R;
import com.compubase.tasaoq.model.CategoriesModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoriesNavigationAdapter extends RecyclerView.Adapter<CategoriesNavigationAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CategoriesModel> categoriesModelList;

    public CategoriesNavigationAdapter(ArrayList<CategoriesModel> categoriesModelList) {
        this.categoriesModelList = categoriesModelList;
    }

    public CategoriesNavigationAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.categories_design_navigation, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        CategoriesModel categoriesModel = categoriesModelList.get(i);

        viewHolder.title.setText(categoriesModel.getTitle());

        Glide.with(context).load(categoriesModel.getImg()).placeholder(R.drawable.avengers).into(viewHolder.img);
    }

    @Override
    public int getItemCount() {
        return categoriesModelList != null ? categoriesModelList.size():0;
    }

    public void setAdapter(ArrayList<CategoriesModel> categoriesModels) {
        this.categoriesModelList = categoriesModels;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        CircleImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.txt_title);
            img = itemView.findViewById(R.id.img_circle);
        }
    }
}
