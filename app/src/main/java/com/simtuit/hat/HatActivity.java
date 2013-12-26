package com.simtuit.hat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class HatActivity extends Activity implements VoteFragment.OnFragmentInteractionListener, PickFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hat);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new VoteFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPick(VoteContent.Vote picked) {

        getFragmentManager().beginTransaction()
                .replace(R.id.container, PickFragment.newInstance(picked.toString()))
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onEditHat() {

        getFragmentManager().beginTransaction()
                .replace(R.id.container, new VoteFragment())
                .addToBackStack(null)
                .commit();
    }
}
