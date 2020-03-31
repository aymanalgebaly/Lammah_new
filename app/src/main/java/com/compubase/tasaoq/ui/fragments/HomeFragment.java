package com.compubase.tasaoq.ui.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.compubase.tasaoq.R;
import com.compubase.tasaoq.adapter.CategoriesAdapter;
import com.compubase.tasaoq.adapter.TopRatedAdapter;
import com.compubase.tasaoq.data.API;
import com.compubase.tasaoq.helper.RetrofitClient;
import com.compubase.tasaoq.model.CategoriesModel;
import com.compubase.tasaoq.model.ProductsModel;
import com.compubase.tasaoq.ui.activities.HomeActivity;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    @BindView(R.id.rcv_categories)
    RecyclerView rcvCategories;
    @BindView(R.id.rcv_top_rated)
    RecyclerView rcvTopRated;
    Unbinder unbinder;
    @BindView(R.id.main_slider)
    SliderLayout mainSlider;

    private int[] images;
    private List<CategoriesModel> categoriesModelList;
    private CategoriesAdapter categoriesAdapter;
    private TopRatedAdapter topRatedAdapter;
    private ProductsModel productsModelsList;
    private ArrayList<ProductsModel> productsModelArrayList = new ArrayList<>();
    private ArrayList<CategoriesModel> categoriesModelArrayList = new ArrayList<>();
    private SharedPreferences preferences;
    private String id;
    private Integer id_pro;


    private static final String TAG = "HomeFragment";
    private String img;
    private CategoriesModel categoriesModel;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        id = preferences.getString("id", "");

//        Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
//
//        images = new int[]{R.drawable.kago, R.drawable.loz, R.drawable.leb,R.drawable.amar_eldein
//        ,R.drawable.sodany,R.drawable.dwar_elshams,R.drawable.fozdo2};
//
//        for (int image : images) {
//
//        }


        HomeActivity homeActivity = (HomeActivity) getActivity();

        Realm.init(getActivity());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ProductsModel> all = realm.where(ProductsModel.class).findAll();
        homeActivity.cartBadge.setText(String.valueOf(all.size()));


        setupRecycler();
        setupRecyclerTopRated();
        fetchData();
        fetchDataTopRated();

        return view;
    }

    public void fetchData() {

        categoriesModelArrayList.clear();

        RetrofitClient.getInstant().create(API.class).selecte_all_category().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                try {
                    assert response.body() != null;
                    List<CategoriesModel> categoriesModels =
                            Arrays.asList(gson.fromJson(response.body().string(), CategoriesModel[].class));

                    if (response.isSuccessful()) {

                        for (int i = 0; i < categoriesModels.size(); i++) {


                            img = categoriesModels.get(i).getImg();

                            Log.i("onResponse: ", img);

                            List<String> imagsList = new ArrayList<>();
                            imagsList.add(img);

                            for (int o = 0; o < imagsList.size(); o++) {

                                DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
                                textSliderView
                                        .description("")
                                        .image(imagsList.get(o))
                                        .setScaleType(BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle()
                                        .putString("extra", "slider");
                                if (null != mainSlider) {
                                    mainSlider.addSlider(textSliderView);
                                    mainSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                                    mainSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                    mainSlider.setCustomAnimation(new DescriptionAnimation());
                                    mainSlider.setDuration(6000);
                                    mainSlider.moveNextPosition();
                                    mainSlider.startAutoCycle();
//                                    mainSlider.setCustomIndicator(indicators);

                                }

                            }

//                            int imagee = Integer.parseInt(img);

//                            flipperImage(imagee);

                            categoriesModel = new CategoriesModel();

                            categoriesModel.setName(categoriesModels.get(i).getName());
                            categoriesModel.setImg(categoriesModels.get(i).getImg());
                            categoriesModel.setId(categoriesModels.get(i).getId());
                            categoriesModel.setDateregister(categoriesModels.get(i).getDateregister());

                            categoriesModelArrayList.add(categoriesModel);

                        }

                        categoriesAdapter.setData(categoriesModelArrayList);
                        rcvCategories.setAdapter(categoriesAdapter);
                        categoriesAdapter.notifyDataSetChanged();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

                Log.i("onFailure: ", t.getMessage());
            }
        });
//        ArrayList<CategoriesModel> categoriesModels = new ArrayList<>();
//
//        int[] image = new int[]{R.drawable.kago,R.drawable.loz,R.drawable.fozdo2,R.drawable.leb,
//        R.drawable.dwar_elshams,R.drawable.sodany,R.drawable.amar_eldein};
//
//        String[]title = new String[]{"كاجو","لوز","فزدق","لب","زيوت","سودانى","قمر الدين"};
//
//        for (int i = 0; i <image.length ; i++) {
//
//            categoriesModels.add(new CategoriesModel(title[i],image[i]));
//        }
//        categoriesAdapter.setAdapter(categoriesModels);
//        categoriesAdapter.notifyDataSetChanged();
    }

    private void setupRecycler() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
                false);
        rcvCategories.setLayoutManager(linearLayoutManager);
        categoriesAdapter = new CategoriesAdapter(getActivity());
//        rcvCategories.setAdapter(categoriesAdapter);
//        categoriesAdapter.notifyDataSetChanged();

    }

    private void fetchDataTopRated() {

        productsModelArrayList.clear();

        Call<ResponseBody> call2 = RetrofitClient.getInstant().create(API.class).viewProducts("1", id);

        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                try {
                    assert response.body() != null;
                    List<ProductsModel> productsModels = Arrays.asList(gson.fromJson(response.body().string(), ProductsModel[].class));

                    if (response.isSuccessful()) {

                        for (int j = 0; j < productsModels.size(); j++) {

                            productsModelsList = new ProductsModel();

//                            productsModelsList.setId(productsModels.get(j).getIdAdmin());
                            productsModelsList.setCategory(productsModels.get(j).getCategory());
                            productsModelsList.setDes(productsModels.get(j).getDes());
                            productsModelsList.setImg1(productsModels.get(j).getImg1());
                            productsModelsList.setImg2(productsModels.get(j).getImg2());
                            productsModelsList.setImg3(productsModels.get(j).getImg3());
                            productsModelsList.setTitle(productsModels.get(j).getTitle());
                            productsModelsList.setId(productsModels.get(j).getId());
                            productsModelsList.setNumberRate(productsModels.get(j).getNumberRate());
                            productsModelsList.setPrice(productsModels.get(j).getPrice());
                            productsModelsList.setPriceDiscount(productsModels.get(j).getPriceDiscount());
                            productsModelsList.setRate(productsModels.get(j).getRate());

                            id_pro = productsModels.get(j).getId();

                            productsModelArrayList.add(productsModelsList);
                        }
                        topRatedAdapter.setDataList(productsModelArrayList);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

                Log.i("onFailure: ", t.getMessage());
            }
        });
    }

    private void setupRecyclerTopRated() {

        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        rcvTopRated.setLayoutManager(linearLayoutManager);
        topRatedAdapter = new TopRatedAdapter(getActivity());
        rcvTopRated.setAdapter(topRatedAdapter);
        topRatedAdapter.notifyDataSetChanged();
    }

//    private void flipperImage(int image) {
//
//        ImageView imageView = new ImageView(getActivity());
//        imageView.setBackgroundResource(image);
//
//        imageSliderFlip.addView(imageView);
//        imageSliderFlip.setFlipInterval(5000);
//        imageSliderFlip.setAutoStart(true);
//
//        imageSliderFlip.setInAnimation(getActivity(),android.R.anim.slide_in_left);
//        imageSliderFlip.setOutAnimation(getActivity(),android.R.anim.slide_out_right);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
