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
import com.compubase.tasaoq.helper.TinyDB;
import com.compubase.tasaoq.model.CategoriesModel;
import com.compubase.tasaoq.ui.activities.HomeActivity;
import com.compubase.tasaoq.ui.fragments.CategorySelectedFragment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CategoriesModel> categoriesModelList;


    public CategoriesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.categories_design, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final CategoriesModel categoriesModel = categoriesModelList.get(i);

        viewHolder.title.setText(categoriesModel.getName());

        Glide.with(context).load(categoriesModel.getImg()).into(viewHolder.img);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity homeActivity = (HomeActivity) context;

                TinyDB tinyDB = new TinyDB(context);
                tinyDB.putString("title",categoriesModel.getName());
                tinyDB.putString("img",categoriesModel.getImg());
                tinyDB.putObject("categories",categoriesModel);

                CategorySelectedFragment categorySelectedFragment = new CategorySelectedFragment();

                homeActivity.displaySelectedFragmentWithBack(categorySelectedFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesModelList != null ? categoriesModelList.size():0;
    }

    public void setAdapter(ArrayList<CategoriesModel> categoriesModels) {
        this.categoriesModelList = categoriesModels;
    }

    public void setData(ArrayList<CategoriesModel> categoriesModelArrayList) {

        this.categoriesModelList = categoriesModelArrayList;
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
