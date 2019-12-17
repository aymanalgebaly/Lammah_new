package com.compubase.tasaoq.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.compubase.tasaoq.R;
import com.compubase.tasaoq.adapter.CategoriesAdapter;
import com.compubase.tasaoq.adapter.CategoriesNavigationAdapter;
import com.compubase.tasaoq.model.CategoriesModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment {


    @BindView(R.id.rcv_category_fragment)
    RecyclerView rcvCategoryFragment;
    Unbinder unbinder;
    private CategoriesNavigationAdapter categoriesAdapter;

    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        unbinder = ButterKnife.bind(this, view);

        setupRecycler();
        fetchData();
        return view;
    }

    private void setupRecycler() {

        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(),2);
        rcvCategoryFragment.setLayoutManager(linearLayoutManager);
        categoriesAdapter = new CategoriesNavigationAdapter(getActivity());
        rcvCategoryFragment.setAdapter(categoriesAdapter);
        categoriesAdapter.notifyDataSetChanged();

    }

    public void fetchData() {

        ArrayList<CategoriesModel> categoriesModels = new ArrayList<>();

        int[] image = new int[]{R.drawable.kago,R.drawable.loz,R.drawable.fozdo2,R.drawable.leb,
                R.drawable.dwar_elshams,R.drawable.sodany,R.drawable.amar_eldein};

        String[]title = new String[]{"كاجو","لوز","فزدق","لب","زيوت","سودانى","قمر الدين"};

        for (int i = 0; i <image.length ; i++) {

            categoriesModels.add(new CategoriesModel(title[i],image[i]));
        }
        categoriesAdapter.setAdapter(categoriesModels);
        categoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
