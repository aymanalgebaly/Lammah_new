package com.compubase.tasaoq.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.compubase.tasaoq.R;
import com.compubase.tasaoq.model.ProductsModel;
import com.compubase.tasaoq.ui.fragments.AboutUsFragment;
import com.compubase.tasaoq.ui.fragments.CartFragment;
import com.compubase.tasaoq.ui.fragments.CategoriesFragment;
import com.compubase.tasaoq.ui.fragments.FavoritesFragment;
import com.compubase.tasaoq.ui.fragments.HistoryFragment;
import com.compubase.tasaoq.ui.fragments.HomeFragment;
import com.compubase.tasaoq.ui.fragments.MostSalesFragment;
import com.compubase.tasaoq.ui.fragments.ProfileFragment;
import com.compubase.tasaoq.ui.fragments.SelectedItemFragment;
import com.compubase.tasaoq.ui.fragments.TopRatedFragment;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
//import io.realm.Realm;
//import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.cart_img)
    ImageView cartImg;
    @BindView(R.id.cart_badge)
    NotificationBadge cartBadge;
    @BindView(R.id.rel_toolbar)
    LinearLayout relToolbar;
    private SharedPreferences preferences;

    int mCartItemCount;

    private Realm realm;
//    @BindView(R.id.imageSlider_flip)
//    ViewFlipper imageSliderFlip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        preferences = getSharedPreferences("user", MODE_PRIVATE);
        String name1 = preferences.getString("name", "");
        String email = preferences.getString("email", "");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        ImageView img_profile = header.findViewById(R.id.img_profile);
        TextView name = header.findViewById(R.id.header_name);
        TextView mail = header.findViewById(R.id.textView);

        name.setText(name1);
        mail.setText(email);

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileFragment profileFragment = new ProfileFragment();
                displaySelectedFragment(profileFragment);
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        HomeFragment homeFragment = new HomeFragment();
        displaySelectedFragment(homeFragment);


        RealmResults<ProductsModel> all = realm.where(ProductsModel.class).findAll();
        cartBadge.setNumber(all.size());

//        deleteImg.setVisibility(View.GONE);
//        deleteImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final RealmResults<ProductsModel> all = realm.where(ProductsModel.class).findAll();
//                realm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        all.deleteAllFromRealm();
//                        SelectedItemFragment selectedItemFragment = new SelectedItemFragment();
//                        displaySelectedFragmentWithBack(selectedItemFragment);
//                        deleteImg.setVisibility(View.GONE);
////                        finish();
//                    }
//                });
//            }
//        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            HomeFragment homeFragment = new HomeFragment();
            displaySelectedFragment(homeFragment);
            // Handle the camera action
        } else if (id == R.id.nav_favorite) {

            FavoritesFragment favoritesFragment = new FavoritesFragment();
            displaySelectedFragmentWithBack(favoritesFragment);

        } else if (id == R.id.nav_categories) {

            CategoriesFragment categoriesFragment = new CategoriesFragment();
            displaySelectedFragmentWithBack(categoriesFragment);

        } else if (id == R.id.nav_star) {

            TopRatedFragment topRatedFragment = new TopRatedFragment();
            displaySelectedFragmentWithBack(topRatedFragment);

        } else if (id == R.id.nav_most_sales) {

            MostSalesFragment mostSalesFragment = new MostSalesFragment();
            displaySelectedFragmentWithBack(mostSalesFragment);

        } else if (id == R.id.nav_about_us) {

            AboutUsFragment aboutUsFragment = new AboutUsFragment();
            displaySelectedFragmentWithBack(aboutUsFragment);

        }else if (id == R.id.nav_orderHistory) {

            HistoryFragment historyFragment = new HistoryFragment();
            displaySelectedFragmentWithBack(historyFragment);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @OnClick({R.id.cart_img, R.id.cart_badge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cart_img:
                emptyCart();
//                deleteImg.setVisibility(View.VISIBLE);
                break;
            case R.id.cart_badge:
                break;
        }
    }

    private void emptyCart() {
        cartBadge.setText("0");
        CartFragment cartFragment = new CartFragment();
        displaySelectedFragmentWithBack(cartFragment);
    }

    public void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    public void displaySelectedFragmentWithBack(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        RealmResults<ProductsModel> all = realm.where(ProductsModel.class).findAll();
        cartBadge.setNumber(all.size());
    }
}
