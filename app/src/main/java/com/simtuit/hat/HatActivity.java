package com.simtuit.hat;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;



public class HatActivity extends Activity
        implements VoteFragment.OnFragmentInteractionListener, PickFragment.OnFragmentInteractionListener, EditVoteFragment.OnFragmentInteractionListener{

    VoteFragment mVoteFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hat);

        if (savedInstanceState == null) {
            mVoteFragment = new VoteFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mVoteFragment, "View")
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
        if (id == R.id.action_clear) {

            mVoteFragment.clearHat();
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
    public void onEdit(int position, String old_content) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        EditVoteFragment fr = EditVoteFragment.newInstance(position, old_content);
        fr.show(ft, "dialog");
    }


    @Override
    public void onEditHat() {

        getFragmentManager().beginTransaction()
                .replace(R.id.container, mVoteFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onEditted(int position, String newContent) {

        mVoteFragment.editVote(position, newContent);
    }
}
