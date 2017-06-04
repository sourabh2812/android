package com.tcs.wba.wbaindiavisit;

import android.net.Uri;
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
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AboutFragment.OnFragmentInteractionListener,
        ContactsFragment.OnFragmentInteractionListener, AgendaFragment.OnFragmentInteractionListener, ItineraryFragment.OnFragmentInteractionListener,
        ExecutivesFragment.OnFragmentInteractionListener {

    CarouselView carouselView;

    int[] tcsLocations = {R.drawable.tcs_noida, R.drawable.tcs_chennai, R.drawable.tcs_mexico, R.drawable.tcs_bangalore};

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(tcsLocations[position]);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(tcsLocations.length);
        carouselView.setImageListener(imageListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;
        // To display App title by default
        String title = getString(R.string.app_name);

        // Get an instance of header text
        TextView headerText = (TextView) findViewById(R.id.headerText);

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                headerText.setVisibility(View.VISIBLE);
                carouselView.setVisibility(View.VISIBLE);
                findViewById(R.id.content_frame).setVisibility(View.GONE);
                break;

            case R.id.nav_agenda:
                findViewById(R.id.content_frame).setVisibility(View.VISIBLE);
                fragment = null;
                fragment = new AgendaFragment();
                title = "Agenda";
                break;

            case R.id.nav_about:
                findViewById(R.id.content_frame).setVisibility(View.VISIBLE);
                fragment = null;
                fragment = new AboutFragment();
                title = "About US";
                break;

            case R.id.nav_executive:
                findViewById(R.id.content_frame).setVisibility(View.VISIBLE);
                fragment = null;
                fragment = new ExecutivesFragment();
                title = "Executive";
                break;

            case R.id.nav_itinerary:
                findViewById(R.id.content_frame).setVisibility(View.VISIBLE);
                fragment = null;
                fragment = new ItineraryFragment();
                title = "Itinerary";
                break;

            case R.id.nav_contacts:
                findViewById(R.id.content_frame).setVisibility(View.VISIBLE);
                fragment = null;
                fragment = new ContactsFragment();
                title = "Contacts";
                break;

            default:
                break;
        }

        if (fragment != null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
            // Hide home screen views
            headerText.setVisibility(View.GONE);
            carouselView.setVisibility(View.GONE);
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
