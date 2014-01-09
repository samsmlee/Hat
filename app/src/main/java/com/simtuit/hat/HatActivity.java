package com.simtuit.hat;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;


public class HatActivity extends Activity
        implements VoteFragment.OnFragmentInteractionListener, PickFragment.OnFragmentInteractionListener, EditVoteFragment.OnFragmentInteractionListener{

    VoteFragment mVoteFragment;

    PickFragment mPickFragment;

    private ScreenSlidePagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    // The index for the first result view
    private final int mPosResults = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hat);


        // PagerAdapter that holds all the fragments to navigate
        mSectionsPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        // Swipe navigation with animation
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        if (savedInstanceState == null) {
            // Create a VoteFragment
            mVoteFragment = VoteFragment.newInstance(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hat, menu);
        return true;
    }

    /**
     * Overrides onBackPressed() to simulate the Back navigation within the ScreenSlidePagerAdapter
     */
    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            moveTaskToBack(true);
        } else {
            // Otherwise, select the previous step.
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
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

        mPickFragment = PickFragment.newInstance(extractVoteStrings(picked), 0);

        mSectionsPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mPosResults);
    }

    /**
     * Called when an item is pressed in VoteFragment
     * Opens up the Edit Vote Dialog (@link EditVoteFragment)
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
     * Called when user wants to view the picks (NOT a new shuffle)
     * (@link mPickFragment) is restored
     */
    @Override
    public void onViewPicks() {

        // if mPickFragment isn't there, do nothing
        if (mPickFragment == null) {
            return;
        }

        mViewPager.setCurrentItem(mPosResults);
    }

    /**
     * Called when the Edit Hat button is pressed in PickFragment
     * Re-opens the VoteFragment
     */
    @Override
    public void onEditHat() {

        mViewPager.setCurrentItem(0);
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


    /**
     * A {@link android.support.v13.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return (position == 0 ? mVoteFragment : mPickFragment);

        }

        @Override
        public int getCount() {
            return (mPickFragment == null ? 1 : 2);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


}
