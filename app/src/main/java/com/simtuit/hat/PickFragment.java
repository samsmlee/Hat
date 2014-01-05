package com.simtuit.hat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PickFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PickFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class PickFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PICKED = "mPicked";
    private static final String ARG_INDEX = "mIndex";

    // Array of strings of (@link VoteContent.Vote)
    private String[] mPicked;

    // Current index of mPicked
    private int mIndex;

    // View which contains the Result views (@link mResultTextView)
    //  and (@link mResultCounterTextView)
    private View mResultView;

    // TextView that displays the result
    private TextView mResultTextView;

    // TextView that displays the result counter
    private TextView mResultCounterTextView;

    // ImageButton that edits the Hat
    private ImageButton mEditHatButton;


    // ImageButton that navigates to the previous button
    private ImageButton mPrevPickButton;


    // ImageButton that navigates to the next button
    private ImageButton mNextPickButton;

    private int mLongAnimationDuration;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param picked (String) picked Vote.
     * @return A new instance of fragment PickFragment.
     */
    public static PickFragment newInstance(String[] picked, int index) {
        PickFragment fragment = new PickFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PICKED, picked);
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    public PickFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pick, container, false);

        // Initialize all member fields that represent views by extracting them from the layout
        initMemberViews(view);

        // Initially hide the result view.
        mResultView.setVisibility(View.GONE);

        // Retrieve and cache the system's default "short" animation time.
        mLongAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);

        // Set as onClickListener: (@link mEditHatButton), (@link mPrevPickButton),
        //  (@link mNextPickButton)
        mEditHatButton.setOnClickListener(this);
        mPrevPickButton.setOnClickListener(this);
        mNextPickButton.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Initiailizes all member views from (@link view)
     * @param view  The root view for this (@link PickFragment)
     */
    private void initMemberViews(View view) {

        // (@link mResultView)
        mResultView = view.findViewById(R.id.view_result);

        // (@link mResultCounterTextView)
        mResultCounterTextView = (TextView) view.findViewById(R.id.textview_result_counter);

        // (@link mResultTextView)
        mResultTextView = (TextView) view.findViewById(R.id.textview_result);

        // (@link mEditHatButton)
        mEditHatButton = (ImageButton) view.findViewById(R.id.edit_hat_button);

        // (@link mPrevPickButton)
        mPrevPickButton = (ImageButton) view.findViewById(R.id.button_prev_pick);

        // (@link mNextPickButton)
        mNextPickButton = (ImageButton) view.findViewById(R.id.button_next_pick);
    }

    /**
     * Saves (@link mIndex)
     */
    @Override
    public void onPause() {
        super.onPause();
        getArguments().putInt(ARG_INDEX, mIndex);

    }

    /**
     *
     */
    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            mPicked = getArguments().getStringArray(ARG_PICKED);
            mIndex = getArguments().getInt(ARG_INDEX);
        }

        // Show the result
        showResult();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Called when v is clicked.
     * If edit_hat_button was clicked, (@link HatActivity#onEditHat()) is called
     * @param v view that was Clicked
     */
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.edit_hat_button:
            {
                ((HatActivity)getActivity()).onEditHat();
                break;
            }
            case R.id.button_prev_pick:
                if(mIndex > 0)
                    updateResultView(mIndex - 1);
                else if (mIndex == 0)
                    ((HatActivity)getActivity()).onEditHat();
                break;
            case R.id.button_next_pick:

                if(mIndex < mPicked.length - 1)
                    updateResultView(mIndex + 1);
                break;
        }
    }

    /**
     * It shows the result(for the first time):
     *  Fades out, updates the result view and fade it back in.
     *
     */
    public void showResult() {

        // Fade Out, including the result view(if any result is showing)
        fadeOutResult();
        // Update the result view
        updateResultView(mIndex);
        // Fade the result view back in
        fadeInResult();
    }

    /**
     * Updates the result
     * @param newIndex  New index in (@link mPicked) to show
     *
     */
    protected void updateResultView(int newIndex) {
        // Check if mPicked is null, or newIndex is an invalid index
        if (mPicked == null || newIndex < 0 || newIndex >= mPicked.length) {
            clearResult();
        } else {
            mIndex = newIndex;

            // Show the result counter: Result %d of %d
            if (mResultCounterTextView != null) {
                String resultCount = getActivity().getString(R.string.result_counter);
                mResultCounterTextView.setText(String.format(resultCount, mIndex + 1, mPicked.length));
            }

            // Show the result
            mResultTextView.setText(mPicked[mIndex]);
        }

    }

    /**
     * Fades in the result view (@link mResultView)
     */
    public void fadeInResult() {


        // Set the result view to 100% opacity to prepare for Fade In
        mResultView.setAlpha(0f);
        mResultView.setVisibility(View.VISIBLE);


        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        mResultView.animate()
                .alpha(1f)
                .setDuration(mLongAnimationDuration)
                .setListener(null);
    }


    /**
     * Fades out the result view (@link mResultView)
     */
    public void fadeOutResult() {

        // Set the result view to 100% opacity if already visible, or 0% if not visible currently
        //  to prepare for Fade Out
        mResultView.setAlpha(mResultView.getVisibility() == View.GONE ? 0f : 1f);
        mResultView.setVisibility(View.VISIBLE);

        // Animate the result view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        mResultView.animate()
                .alpha(0f)
                .setDuration(mLongAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mResultView.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * Clears the result from (@link view)
     */
    protected void clearResult() {
        if (mResultCounterTextView != null) {
            mResultCounterTextView.setText("");
        }
        if (mResultTextView != null) {
            mResultTextView.setText("");
        }
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
        public void onEditHat();

    }

}
