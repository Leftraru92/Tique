package uai.diploma.tique.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import uai.diploma.tique.CuentaFragment;
import uai.diploma.tique.fragment.FavFragment;
import uai.diploma.tique.fragment.HomeFragment;
import uai.diploma.tique.fragment.CategoryFragment;
import uai.diploma.tique.R;
import uai.diploma.tique.fragment.NotifFragment;

import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

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
                    return true;
                case R.id.navigation_fav:
                    fragment = new FavFragment();
                    replaceFragment(fragment);
                    return true;
                case R.id.navigation_notifications:
                    fragment = new NotifFragment();
                    replaceFragment(fragment);
                    return true;
                case R.id.navigation_perfil:
                    fragment = new CuentaFragment();
                    replaceFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    public void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_fragment, fragment).commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
