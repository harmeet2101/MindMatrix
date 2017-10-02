package com.cli.knowledgebase;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cli.knowledgebase.AppService.SendDataService;
import com.google.gson.Gson;

import java.util.ArrayList;

import Adapter.DrawerAdapter;
import AppFragment.FragmentDiscussion;
import AppFragment.FragmentProjects;
import AppFragment.FragmentSubjects;
import AppFragment.Fragment_BLOG;
import AppFragment.Fragment_Competition;
import Constants.Const;
import DataBase.DataBaseHelper;
import Interfaces.RecyclerViewItemClickListener;
import Model.DrawerMenuItem;
import Model.Login;
import Utils.PreferenceHelper;
import Utils.ViewUtils;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeScreen extends AppCompatActivity implements RecyclerViewItemClickListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    RecyclerView recycler_view_left_drawer;
    ArrayList<DrawerMenuItem> drawerMenuItems = new ArrayList<>();
    DrawerAdapter drawerAdapter;
    PreferenceHelper preferenceHelper;
    Login loginDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        preferenceHelper = new PreferenceHelper(this);

        Gson gson = new Gson();
        loginDetails = gson.fromJson(preferenceHelper.getUser_detials(), Login.class);

        widgets();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.app_name, R.string.app_name) {

            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
                //drawerOpened = false;
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
                //drawerOpened = true;
            }
        };
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        ViewUtils.addFragment(new FragmentSubjects(), false, null,
                Const.FRAGMENT_TAG.FRAGMENT_SUBJECT, getSupportFragmentManager(), R.id.container);

    }


    @Override
    protected void onResume() {
        super.onResume();
        loginDetails = new Gson().fromJson(preferenceHelper.getUser_detials(), Login.class);
        loadDrawerMenu();


    }

    private void loadDrawerMenu() {

        drawerMenuItems.clear();
        drawerMenuItems.add(new DrawerMenuItem(1, loginDetails.getUser().getFirstname() + " " + loginDetails.getUser().getLastname(), Const.MenuType.PROFILE, loginDetails.getUser().getProfileUrl()));

        drawerMenuItems.add(new DrawerMenuItem(2, getString(R.string.menu_subjects), Const.MenuType.LIST, "" + R.mipmap.ic_subjects));
        // drawerMenuItems.add(new DrawerMenuItem(3, getString(R.string.menu_project), Const.MenuType.LIST, "" + R.mipmap.ic_projects));
        drawerMenuItems.add(new DrawerMenuItem(6, getString(R.string.menu_blog), Const.MenuType.LIST, "" + R.mipmap.ic_blog));
        drawerMenuItems.add(new DrawerMenuItem(5, getString(R.string.menu_discussion), Const.MenuType.LIST, "" + R.mipmap.ic_discussion));
        drawerMenuItems.add(new DrawerMenuItem(7, getString(R.string.menu_competition), Const.MenuType.LIST, "" + R.mipmap.ic_compitation));
        drawerMenuItems.add(new DrawerMenuItem(4, getString(R.string.menu_logout), Const.MenuType.LIST, "" + R.mipmap.ic_logout));

        drawerAdapter = new DrawerAdapter(this, drawerMenuItems, this);
        recycler_view_left_drawer.setAdapter(drawerAdapter);

    }

    private void widgets() {

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
        recycler_view_left_drawer = (RecyclerView) findViewById(R.id.recycler_view_left_drawer);
        recycler_view_left_drawer.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onItemClick(int id) {

        drawerLayout.closeDrawers();
        switch (id) {
            case 1:
                ViewUtils.launchActivity(HomeScreen.this, null, ProfileActivity.class);
                break;
            case 2:
                ViewUtils.addFragment(new FragmentSubjects(), false, null, Const.FRAGMENT_TAG.FRAGMENT_SUBJECT, getSupportFragmentManager(), R.id.container);
                break;
            case 3:
                ViewUtils.addFragment(new FragmentProjects(), false, null, Const.FRAGMENT_TAG.FRAGMENT_PROJECT,getSupportFragmentManager(), R.id.container);
                break;
            case 5:
                ViewUtils.addFragment(new FragmentDiscussion(), false, null, Const.FRAGMENT_TAG.FRAGMENT_DISCUSSION,getSupportFragmentManager(), R.id.container);
                break;
            case 6:
                ViewUtils.addFragment(new Fragment_BLOG(), false, null, Const.FRAGMENT_TAG.FRAGMENT_BLOG,getSupportFragmentManager(), R.id.container);
                break;
            case 7:
                ViewUtils.addFragment(new Fragment_Competition(), false, null, Const.FRAGMENT_TAG.FRAGMENT_BLOG,getSupportFragmentManager(), R.id.container);
                break;
            case 4:
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(this.getString(R.string.logout_alert))
                        .setConfirmText(this.getString(R.string.text_ok))
                        .setCancelText(this.getString(R.string.text_cancel))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                sweetAlertDialog.dismiss();
                                preferenceHelper.putUserDetails("");
                                preferenceHelper.putApiToken("");

                                new DataBaseHelper(HomeScreen.this).clearData();
                                ViewUtils.launchActivity(HomeScreen.this, null, MainActivity.class);
                                SendDataService.stopService(HomeScreen.this);

                                finish();


                            }
                        });

                sweetAlertDialog.show();


                break;

        }

    }
}
