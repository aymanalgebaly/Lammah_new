package com.compubase.tasaoq.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.compubase.tasaoq.R;
import com.compubase.tasaoq.adapter.TopRatedAdapter;
import com.compubase.tasaoq.adapter.TopRatedNavigationAdapter;
import com.compubase.tasaoq.model.TopRatedModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopRatedFragment extends Fragment {


    @BindView(R.id.rcv_top_rated_fragment)
    RecyclerView rcvTopRatedFragment;
    Unbinder unbinder;
    private TopRatedNavigationAdapter topRatedAdapter;

    public TopRatedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_rated, container, false);
        unbinder = ButterKnife.bind(this, view);

        setupRecyclerTopRated();
        fetchDataTopRated();
        return view;
    }

    private void fetchDataTopRated() {

        ArrayList<TopRatedModel> topRatedModels = new ArrayList<>();

        int[] image1 = new int[]{R.drawable.titanic, R.drawable.avengers, R.drawable.anti_man, R.drawable.titanic,
                R.drawable.avengers, R.drawable.anti_man};

        int[] imageHeart = new int[]{R.drawable.heart, R.drawable.heart, R.drawable.heart, R.drawable.heart,
                R.drawable.heart, R.drawable.heart};

        String[] title = new String[]{"aaa", "bbb", "ccc", "ddd", "eee", "fff", "ggg"};
        String[] offer = new String[]{"200", "230", "150", "260", "180", "140", "300"};
        String[] offer_sale = new String[]{"150", "130", "100", "180", "90", "110", "170"};
        String[] percent = new String[]{"-10%", "-50%", "-20%", "-30%", "-40%", "-90%", "-70%"};
        String[] rate = new String[]{"4.0", "5.0", "4.5", "3.0", "3.5", "4.5", "2.5"};

        for (int i = 0; i < image1.length; i++) {

            topRatedModels.add(new TopRatedModel(image1[i], imageHeart[i], percent[i], offer_sale[i], offer[i],
                    rate[i], title[i]));
        }
        topRatedAdapter.setAdapter(topRatedModels);
        topRatedAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerTopRated() {

        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        rcvTopRatedFragment.setLayoutManager(linearLayoutManager);
        topRatedAdapter = new TopRatedNavigationAdapter(getActivity());
        rcvTopRatedFragment.setAdapter(topRatedAdapter);
        topRatedAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
