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
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;


public class HatActivity extends Activity
        implements VoteFragment.OnFragmentInteractionListener, PickFragment.OnFragmentInteractionListener, EditVoteFragment.OnFragmentInteractionListener {

    // Fragment where User can view all the Votes
    VoteFragment mVoteFragment;

    // List of all PickFragments to display
    List<PickFragment> mPickFragments;

    private ScreenSlidePagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private ImageButton mCenterNavButton;
    private ImageButton mLeftNavButton;
    private ImageButton mRightNavButton;


    // The index for the first result view
    private final int mPosResults = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hat);

        if (savedInstanceState == null) {
            // Create a VoteFragment
            if(mVoteFragment == null)
                mVoteFragment = VoteFragment.newInstance(false);
            // Instantiate mPickFragments
            if(mPickFragments == null)
                mPickFragments = new ArrayList<>();
        }


        // PagerAdapter that holds all the fragments to navigate
        mSectionsPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());


        mCenterNavButton = (ImageButton) findViewById(R.id.button_center_nav);
        mLeftNavButton = (ImageButton) findViewById(R.id.button_left_nav);
        mRightNavButton = (ImageButton) findViewById(R.id.button_right_nav);





        // Set up the ViewPager with the sections adapter.
        // Swipe navigation with animation
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        setTitle(mSectionsPagerAdapter.getPageTitle(position));

                        updateControls(position);
                    }
                });


        // Update the controls according to the current Fragment
        updateControls(mViewPager.getCurrentItem());

    }

    /**
     * Updates the controls depending on the current Fragment
     * @param position Indicates the position of the current Fragment
     */
    private void updateControls(int position) {

        View.OnClickListener l = (View.OnClickListener) mSectionsPagerAdapter.getItem(position);

        mCenterNavButton.setOnClickListener(l);
        mLeftNavButton.setOnClickListener(l);
        mRightNavButton.setOnClickListener(l);

        // update for VoteFragment
        if (l instanceof VoteFragment) {

            mCenterNavButton.setImageResource(R.drawable.ic_action_pick);
            mLeftNavButton.setVisibility(View.GONE);


            if (mVoteFragment == null || !mVoteFragment.isShuffled()) {
                mRightNavButton.setVisibility(View.GONE);
            } else {

                mRightNavButton.setVisibility(View.VISIBLE);
            }

        // update for PickFragment
        } else if (l instanceof PickFragment) {
            mCenterNavButton.setImageResource(R.drawable.ic_action_edit_hat);
            mLeftNavButton.setVisibility(View.VISIBLE);

            if(((PickFragment) l).isLast())
                mRightNavButton.setVisibility(View.GONE);
            else
                mRightNavButton.setVisibility(View.VISIBLE);

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

        mPickFragments.clear();

        for (int i = 0; i < picked.length; i++) {
            mPickFragments.add(PickFragment.newInstance(picked[i].content, i, picked.length));
        }

        mSectionsPagerAdapter.notifyDataSetChanged();
        moveToFragment(mPosResults);
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
     * (@link mPickFragments) are restored
     */
    @Override
    public void onViewPicks() {

        // if mPickFragments aren't there, do nothing
        if (mPickFragments == null || mPickFragments.size() == 0) {
            return;
        }

        moveToFragment(mPosResults);

    }

    /**
     * Called when the Edit Hat button is pressed in PickFragment
     * Re-opens the VoteFragment
     */
    @Override
    public void onEditHat() {

        moveToFragment(0);
    }

    @Override
    public void onMoveToResult(int resultIndex) {
        moveToFragment(resultIndex + 1);
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


    private void moveToFragment(int index) {
        mViewPager.setCurrentItem(index);
    }


    /**
     * A {@link android.support.v13.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Returns the Fragment at a certain position
         * @param position
         * @return (@link mVoteFragment) if position == 0
         *  otherwise, corresponding (@link PickFragment) in (@link mPickFragments)
         */
        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                return mVoteFragment;
            } else {
                return mPickFragments.get(position - 1);
            }

        }

        /**
         * Returns total number of all fragments
         * @return 1 if (@link mPickFragments) aren't there yet
         * 1 + (@link mPickFragments#size()), otherwise.
         */
        @Override
        public int getCount() {
            return (mPickFragments == null ? 1 : mPickFragments.size() + 1);
        }

        /**
         * Triggers update of each view every time it's called.
         * @param object Object representing an item
         * @return Always returns that (@link object) does not exist
         */
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        /**
         * Return the title of the page at position.
         * Returns (@link R.string.app_name) if position == 0
         * Return
         * @param position The position of the title requested
         * @return (CharSequence) (@link R.string.app_name) if position == 0. Otherwise, returns
         *  the Result Counter of the (@link PickFragment) at position.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0)
                return getString(R.string.app_name);
            else
                return mPickFragments.get(position - 1).getResultCounterStr();
        }


    }


}
