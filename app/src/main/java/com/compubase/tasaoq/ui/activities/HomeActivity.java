package com.compubase.tasaoq.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.compubase.tasaoq.R;
import com.compubase.tasaoq.model.ProductsModel;
import com.compubase.tasaoq.ui.fragments.AboutUsFragment;
import com.compubase.tasaoq.ui.fragments.CartFragment;
import com.compubase.tasaoq.ui.fragments.FavoritesFragment;
import com.compubase.tasaoq.ui.fragments.HistoryFragment;
import com.compubase.tasaoq.ui.fragments.HomeFragment;
import com.compubase.tasaoq.ui.fragments.ProfileFragment;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.cart_img)
    ImageView cartImg;
//    @BindView(R.id.cart_badge)
    @BindView(R.id.rel_toolbar)
    LinearLayout relToolbar;
    private SharedPreferences preferences;

    int mCartItemCount;

    public TextView cartBadge;

    private Realm realm;
//    @BindView(R.id.imageSlider_flip)
//    ViewFlipper imageSliderFlip;

    private static final String TAG = "HomeActivity";

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);


        Realm.init(this);
        realm = Realm.getDefaultInstance();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        cartBadge = findViewById(R.id.cart_badge);

        preferences = getSharedPreferences("user", MODE_PRIVATE);
        String name1 = preferences.getString("name", "");
        String email = preferences.getString("email", "");
        String id = preferences.getString("id", "");

//        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView name = header.findViewById(R.id.header_name);
        TextView mail = header.findViewById(R.id.textView);

        name.setText(name1);
        mail.setText(email);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        RealmResults<ProductsModel> all = realm.where(ProductsModel.class).findAll();
        cartBadge.setText(String.valueOf(all.size()));

        Log.i(TAG, "onCreateSize: " + all.size());


        HomeFragment homeFragment = new HomeFragment();
        displaySelectedFragment(homeFragment);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
//            alartExit();
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            ProfileFragment homeFragment = new ProfileFragment();
            displaySelectedFragment(homeFragment);
            // Handle the camera action
        } else if (id == R.id.nav_favorite) {

            FavoritesFragment favoritesFragment = new FavoritesFragment();
            displaySelectedFragmentWithBack(favoritesFragment);

        }  else if (id == R.id.nav_about_us) {

            AboutUsFragment aboutUsFragment = new AboutUsFragment();
            displaySelectedFragmentWithBack(aboutUsFragment);

        }else if (id == R.id.nav_orderHistory) {

            HistoryFragment historyFragment = new HistoryFragment();
            displaySelectedFragmentWithBack(historyFragment);

        }else if (id == R.id.nav_logout) {

            SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();

            preferences = getSharedPreferences("user", MODE_PRIVATE);

            editor.putBoolean("login", false);

            editor.apply();
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();

//            alartExit();
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
//        cartBadge.setText("0");
        CartFragment cartFragment = new CartFragment();
//        displaySelectedFragmentWithBack(cartFragment);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame, cartFragment);
        fragmentTransaction.addToBackStack(null).commit();
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

    public void alartExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit ?").setCancelable(false).setPositiveButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);

                //Main2Activity.this.finish();
            }
        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

}
