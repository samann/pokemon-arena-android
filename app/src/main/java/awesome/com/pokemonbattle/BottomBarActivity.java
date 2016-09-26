package awesome.com.pokemonbattle;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;

import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import awesome.com.pokemonbattle.fragments.BattleHomeFragment;
import awesome.com.pokemonbattle.fragments.ChatHomeFragment;
import awesome.com.pokemonbattle.fragments.TeamsHomeFragment;

public class BottomBarActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottombar);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.setDefaultTab(R.id.tab_battle);

        final BattleHomeFragment battleFragment = new BattleHomeFragment();
        final TeamsHomeFragment teamFragment = new TeamsHomeFragment();
        final ChatHomeFragment chatFragment = new ChatHomeFragment();
        final android.app.FragmentManager manger = getFragmentManager();

        manger.beginTransaction()
                .add(R.id.container, battleFragment, "battle")
                .commit();

        // Listens for a tab touch (Only first touch of new tab)
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_teams:
                        manger.beginTransaction()
                                .replace(R.id.container, teamFragment, "team")
                                .commit();
                        break;
                    case R.id.tab_battle:
                        manger.beginTransaction()
                                .replace(R.id.container, battleFragment, "battle")
                                .commit();
                        break;
                    case R.id.tab_chat:
                        manger.beginTransaction()
                                .replace(R.id.container, chatFragment, "chat")
                                .commit();
                        break;
                    default:
                        break;
                }
            }
        });

        // Listens for a tab touch (Only when 'reselected')
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_teams:
                        Toast.makeText(BottomBarActivity.this, "Teams Again", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab_battle:
                        Toast.makeText(BottomBarActivity.this, "Battle Again", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab_chat:
                        Toast.makeText(BottomBarActivity.this, "Chat Again", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
