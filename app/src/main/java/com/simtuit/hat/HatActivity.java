package com.simtuit.hat;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;



public class HatActivity extends Activity
        implements VoteFragment.OnFragmentInteractionListener, PickFragment.OnFragmentInteractionListener, EditVoteFragment.OnFragmentInteractionListener{

    VoteFragment mVoteFragment;

    PickFragment mPickFragment;

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

    /**
     * Called when the Pick Button is pressed in VoteFragment
     * Opens PickFragment which displays the shuffled list of Votes
     * @param picked    Array of Votes in shuffled order
     */
    @Override
    public void onPick(VoteContent.Vote[] picked) {
        mPickFragment = PickFragment.newInstance(extractVoteStrings(picked));
        getFragmentManager().beginTransaction()
                .replace(R.id.container, mPickFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Called when an item is pressed in VoteFragment
     * Opens up the Edit Vote Dialog (EditVoteFragment)
     * @param position      position of the Vote to be edited
     * @param old_content   old content of the Vote
     */
    @Override
    public void onEdit(int position, String old_content) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        EditVoteFragment fr = EditVoteFragment.newInstance(position, old_content);
        fr.show(ft, "dialog");
    }

    /**
     * Called when the Edit Hat button is pressed in PickFragment
     * Re-opens the VoteFragment
     */
    @Override
    public void onEditHat() {

        getFragmentManager().beginTransaction()
                .replace(R.id.container, mVoteFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Called when the RePick Hat button is pressed in PickFragment
     * Updates the PickFragment with a new list of results
     * @param view  The view of PickFragment that displays the results
     */
    @Override
    public void onRePick(View view) {
        mPickFragment.updatePick(view, extractVoteStrings(mVoteFragment.pick()), 0);
    }

    /**
     * Called when the Edit button is pressed in EditVoteFragment
     * Edits the Vote at the specified position
     * @param position      position of the Vote to be edited
     * @param newContent    String for the new content for the Vote
     */
    @Override
    public void onEdited(int position, String newContent) {

        mVoteFragment.editVote(position, newContent);
    }

    /**
     * Called when the Delete button is pressed in EditVoteFragment
     * Deletes the Vote at specified position
     * @param position  position of the Vote to be deleted
     */
    @Override
    public void onDeleted(int position) {
        mVoteFragment.deleteVote(position);
    }


    /**
     * Utility method that extracts the content of Votes from an array of Votes
     * @param votes Array of Votes to extract contents from
     * @return      Array of Strings that contain the contents of Votes from votes argument
     */
    protected String[] extractVoteStrings(VoteContent.Vote[] votes) {
        String[] extractedStrings;

        // if votes is null, then extractedStrings should be null too
        if (votes == null) {
            return null;

        } else {

            extractedStrings = new String[votes.length];
            for (int i = 0; i < votes.length; i++) {
                extractedStrings[i] = (votes[i]!= null ? votes[i].toString() : "");
            }
        }

        return extractedStrings;
    }
}
