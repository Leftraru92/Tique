package uai.diploma.tique.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import uai.diploma.tique.fragment.CuentaFragment;
import uai.diploma.tique.fragment.FavFragment;
import uai.diploma.tique.fragment.HomeFragment;
import uai.diploma.tique.fragment.CategoryFragment;
import uai.diploma.tique.R;
import uai.diploma.tique.fragment.NotifFragment;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private View errorPanel, loading;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    replaceFragment(fragment);
                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.title_dashboard);
                    fragment = new CategoryFragment();
                    replaceFragment(fragment);
                    getSupportActionBar().setTitle(getString(R.string.title_dashboard));
                    return true;
                case R.id.navigation_fav:
                    fragment = new FavFragment();
                    replaceFragment(fragment);
                    getSupportActionBar().setTitle(getString(R.string.title_fav));
                    return true;
                case R.id.navigation_notifications:
                    fragment = new NotifFragment();
                    replaceFragment(fragment);
                    getSupportActionBar().setTitle(getString(R.string.title_notifications));
                    return true;
                case R.id.navigation_perfil:
                    fragment = new CuentaFragment();
                    replaceFragment(fragment);
                    getSupportActionBar().setTitle(getString(R.string.title_perfil));
                    return true;
            }
            return false;
        }
    };



    public void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_fragment, fragment).commit();
        errorPanel.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorPanel = findViewById(R.id.errorPanel);
        mTextMessage = (TextView) findViewById(R.id.message);
        loading = findViewById(R.id.loadingPanel);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }



}
