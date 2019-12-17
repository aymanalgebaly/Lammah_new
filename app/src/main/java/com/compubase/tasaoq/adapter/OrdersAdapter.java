package com.compubase.tasaoq.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.compubase.tasaoq.R;
import com.compubase.tasaoq.model.LammahOrdersModel;
import com.compubase.tasaoq.model.OrdersModel;
import com.compubase.tasaoq.ui.activities.HomeActivity;
import com.compubase.tasaoq.ui.fragments.ConfirmFragment;
import com.compubase.tasaoq.ui.fragments.ViewOrderFragment;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolderOrders> {
    private Context context;
    private List<LammahOrdersModel>lammahOrdersModels;

    public OrdersAdapter(List<LammahOrdersModel> lammahOrdersModels1) {
        this.lammahOrdersModels = lammahOrdersModels1;
    }

    @NonNull
    @Override
    public ViewHolderOrders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.order_design, viewGroup, false);
        return new ViewHolderOrders(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderOrders viewHolderOrders, int i) {
        final LammahOrdersModel lammahOrdersModel = lammahOrdersModels.get(i);

        viewHolderOrders.price.setText(lammahOrdersModel.getTotlePrice());
        viewHolderOrders.id.setText(String.valueOf(lammahOrdersModel.getId()));
        viewHolderOrders.date.setText(lammahOrdersModel.getDatee());

        final Integer id = lammahOrdersModel.getId();

        viewHolderOrders.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity homeActivity = (HomeActivity)context;
                Bundle bundle = new Bundle();
                bundle.putParcelable("id_order", lammahOrdersModel);
                ViewOrderFragment viewOrderFragment = new ViewOrderFragment();
                viewOrderFragment.setArguments(bundle);
                homeActivity.displaySelectedFragmentWithBack(viewOrderFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lammahOrdersModels != null ? lammahOrdersModels.size():0;
    }

    public class ViewHolderOrders extends RecyclerView.ViewHolder {
        TextView price,id,date;
        public ViewHolderOrders(@NonNull View itemView) {
            super(itemView);

            price = itemView.findViewById(R.id.txt_price);
            id = itemView.findViewById(R.id.txt_id);
            date = itemView.findViewById(R.id.txt_date);
        }
    }
}
