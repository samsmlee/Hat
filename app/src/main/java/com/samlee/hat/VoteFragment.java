package com.samlee.hat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * VoteFragment
 * A fragment where the User can view, add, edit, and delete Votes
 */
public class VoteFragment
        extends Fragment
        implements AbsListView.OnItemClickListener, View.OnClickListener, TextView.OnEditorActionListener, AdapterView.OnItemLongClickListener {

    private static final String ARG_SHUFFLED = "mShuffled";

    private OnFragmentInteractionListener mListener;

    private ImageButton mAddButton;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    /**
     * indicates whether the Votes have been shuffled yet
     */
    private boolean mShuffled;

    /**
     * Toast for Empty Hat alert
     */
    private Toast mEmptyHatToast;


    public static VoteFragment newInstance(boolean shuffled) {
        VoteFragment fragment = new VoteFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_SHUFFLED, shuffled);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VoteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAdapter = new ArrayAdapter<VoteContent.Vote>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, VoteContent.ITEMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vote, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);

        // Set onEditorActionListener to Vote Content EditText (R.id.vote_content)
        EditText voteContent = (EditText) view.findViewById(R.id.vote_content);
        voteContent.setOnEditorActionListener(this);


        // Set onClickListener to Add Button (R.id.add_button)
        mAddButton = (ImageButton) view.findViewById(R.id.add_button);
        mAddButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Save (@link mShuffled)
     * Cancel Empty Hat Alert
     */
    @Override
    public void onPause() {
        super.onPause();

        getArguments().putBoolean(ARG_SHUFFLED, mShuffled);

        if(mEmptyHatToast != null)
            mEmptyHatToast.cancel();
    }

    /**
     * Retrieve (@link mShuffled)
     */
    @Override
    public void onResume() {
        super.onResume();


        if (getArguments() != null) {
            setShuffled(getArguments().getBoolean(ARG_SHUFFLED, false));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            mListener.onEdit(position, ((VoteContent.Vote)mAdapter.getItem(position)).content);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * If add_button is clicked, the Vote is added.
     * If pick_button is clicked, Votes are picked.
     * If button_view_picked is clicked, PickFragment is shown
     * @param view clicked View
     */
    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.add_button:
            {
                this.addVote();
                break;
            }
            //case R.id.pick_button:
            case R.id.button_center_nav:
            {
                if (mAdapter.getCount() <= 0) {
                    alertEmptyHat();
                } else {

                    if ((getActivity()) != null) {
                        setShuffled(true);
                        ((HatActivity) getActivity()).onPick(pick());
                    }
                }
                break;

            }
            //case R.id.button_view_picked:
            case R.id.button_right_nav:

                if ((getActivity()) != null) {
                    ((HatActivity) getActivity()).onViewPicks();
                }
                break;
        }

    }

    public boolean isShuffled() {
        return mShuffled;
    }

    public void setShuffled(boolean shuffled) {
        this.mShuffled = shuffled;
    }

    /**
     * Shows a warning message that the Hat is empty
     * Used when Pick button is clicked while Hat is empty
     */
    protected void alertEmptyHat() {

        Context context = getActivity();

        CharSequence text = context.getString(R.string.empty_hat_warning);
        if(mEmptyHatToast == null)
            mEmptyHatToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        else
            mEmptyHatToast.setText(text);

        mEmptyHatToast.show();
    }


    /**
     * Picks out of the Hat
     * @return (VoteContent.Vote) picked Vote
     */
    protected VoteContent.Vote[] pick() {
        int currCount = mAdapter.getCount();
        int pick;
        VoteContent.Vote temp;
        VoteContent.Vote[] picked = new VoteContent.Vote[currCount];

        // Get seed from the nanoseconds of current time
        Random r = new Random(System.nanoTime());

        // Go through from 0 to mAdapter.getCount() and pick random Votes for each index
        // For each index, swap the randomly picked Vote and the original Vote at the index
        // Kind of like a selection sort, but instead of finding the index of the max/min value,
        //  we pick a random index
        for (int i = 0; i < picked.length; i++) {
            pick = r.nextInt(currCount);

            // Swap the Vote in the current index with the Vote in the picked index

            temp = picked[i];

            // Bring the picked Vote to the current index
            if (picked[i + pick] == null) {
                picked[i] = (VoteContent.Vote) mAdapter.getItem(i + pick);
            } else {
                picked[i] = picked[i + pick];
            }

            // Put the Vote that used to be in the current index to the randomly picked index.
            if (temp == null) {
                picked[i + pick] = (VoteContent.Vote) mAdapter.getItem(i);
            } else {
                picked[i + pick] = temp;
            }

            currCount--;
        }

        return picked;
    }

    /**
     * Adds a Vote if the text from R.id.vote_content EditText is not empty
     */
    protected void addVote() {
        View voteText = getView().findViewById(R.id.vote_content);

        if(null != voteText) {
            String voteContent = ((EditText) voteText).getText().toString();
            if(!voteContent.isEmpty())
            {
                VoteContent.addVote(voteContent);
                ((ArrayAdapter) mAdapter).notifyDataSetChanged();
                ((EditText) voteText).setText("");


            }

        }
    }

    /**
     * Deletes a Vote
     * @param position Position of the Vote to be deleted
     */
    protected void deleteVote(int position) {
        ((ArrayAdapter) mAdapter).remove(mAdapter.getItem(position));
        ((ArrayAdapter) mAdapter).notifyDataSetChanged();
    }

    /**
     * Handles Enter from soft keyboard on R.id.vote_content
     * @param v
     * @param actionId
     * @param event
     * @return true if no further action is required from onEditorAction
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if(event == null) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_DONE:
                    mAddButton.performClick();
                    return true;
            }

        }

        return false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    /**
     * Edits a Vote
     * @param position
     * @param newContent                               "
     */
    public void editVote(int position, String newContent) {
        ((VoteContent.Vote) mAdapter.getItem(position)).content = newContent;
        ((ArrayAdapter) mAdapter).notifyDataSetChanged();
    }

    /**
     * Clears the Hat.
     */
    public void clearHat() {
        ((ArrayAdapter) mAdapter).clear();
        ((ArrayAdapter) mAdapter).notifyDataSetChanged();
    }

    /**
     * Saves the Hat
     */
    public void saveHat() {

        Context context = getActivity();
        CharSequence hatToSave = VoteContent.serialize();

        // save the Hat
        SharedPreferences sharedPref = ((Activity)context).getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("myHat", hatToSave.toString());
        editor.commit();

        // Show message saying that it's saved
        if(mEmptyHatToast == null)
            mEmptyHatToast = Toast.makeText(context, context.getString(R.string.hat_saved_message), Toast.LENGTH_SHORT);
        else
            mEmptyHatToast.setText(context.getString(R.string.hat_saved_message));

        mEmptyHatToast.show();
    }


    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface OnFragmentInteractionListener {

        public void onPick(VoteContent.Vote[] picked);

        public void onEdit(int position, String old_content);

        public void onViewPicks();
    }

}
