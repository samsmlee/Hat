package com.simtuit.hat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * PickFragment
 * A fragment that represents a Pick/Result
 */
public class PickFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_INDEX = "mIndex";
    private static final String ARG_NUMRESULTS = "mNumResults";
    private static final String ARG_RESULT = "mResult";

    // String that contains the content of this Result
    private String mResult;

    // index of the current mResult
    private int mIndex;

    // Total number of results
    private int mNumResults;

    // TextView that displays the result
    private TextView mResultTextView;

    private int mLongAnimationDuration;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param result    (String) content of the picked result
     * @param index     (int) index at which this result was picked
     * @param numResults (int) number of all votes/results
     * @return
     */
    public static PickFragment newInstance(String result, int index, int numResults) {
        PickFragment fragment = new PickFragment();
        Bundle args = new Bundle();

        args.putString(ARG_RESULT, result);
        args.putInt(ARG_INDEX, index);
        args.putInt(ARG_NUMRESULTS, numResults);
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
        mResultTextView.setVisibility(View.GONE);

        // Retrieve and cache the system's default "short" animation time.
        mLongAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Initiailizes all member views from (@link view)
     * @param view  The root view for this (@link PickFragment)
     */
    private void initMemberViews(View view) {

        // (@link mResultTextView)
        mResultTextView = (TextView) view.findViewById(R.id.textview_result);
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
            mIndex = getArguments().getInt(ARG_INDEX);
            mResult = getArguments().getString(ARG_RESULT);
            mNumResults = getArguments().getInt(ARG_NUMRESULTS);
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
            case R.id.button_center_nav:
            {
                ((HatActivity)getActivity()).onEditHat();
                break;
            }
            case R.id.button_left_nav:
                if(mIndex > 0)
                    ((HatActivity)getActivity()).onMoveToResult(mIndex - 1);
                else if (mIndex == 0)
                    ((HatActivity)getActivity()).onEditHat();
                break;
            case R.id.button_right_nav:

                if(mIndex < mNumResults - 1)
                    ((HatActivity)getActivity()).onMoveToResult(mIndex + 1);
                break;
        }
    }

    /**
     * It shows the result(for the first time):
     *  Fades out, updates the result view and fade it back in.
     *
     */
    public void showResult() {

        // Fade Out, including the result view(if any result is showing) only if it's first result
        if(mIndex == 0)
            fadeOutResult();
        else
            // Set the result view to 100% opacity to prepare for Fade In
            mResultTextView.setAlpha(1f);
            mResultTextView.setVisibility(View.VISIBLE);


        // Show the result view
        // Check if mResult is null, or newIndex is an invalid index
        if (mResult == null || mIndex < 0 || mIndex >= mNumResults) {
            clearResult();
        } else {
            // Show the result
            mResultTextView.setText(mResult);
        }

        // Fade the result view back in only if it's first result
        if(mIndex == 0)
            fadeInResult();
    }

    /**
     * Fades in the result view (@link mResultTextView)
     */
    public void fadeInResult() {


        // Set the result view to 100% opacity to prepare for Fade In
        mResultTextView.setAlpha(0f);
        mResultTextView.setVisibility(View.VISIBLE);


        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        mResultTextView.animate()
                .alpha(1f)
                .setDuration(mLongAnimationDuration)
                .setListener(null);
    }


    /**
     * Fades out the result view (@link mResultTextView)
     */
    public void fadeOutResult() {

        // Set the result view to 100% opacity if already visible, or 0% if not visible currently
        //  to prepare for Fade Out
        mResultTextView.setAlpha(mResultTextView.getVisibility() == View.GONE ? 0f : 1f);
        mResultTextView.setVisibility(View.VISIBLE);

        // Animate the result view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        mResultTextView.animate()
                .alpha(0f)
                .setDuration(mLongAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mResultTextView.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * Clears the result from (@link view)
     */
    protected void clearResult() {
        if (mResultTextView != null) {
            mResultTextView.setText("");
        }
    }

    /**
     * Generates the Result Counter string: Result %d of %d
     * @return (CharSequence) Result Counter string
     */
    public CharSequence getResultCounterStr() {
        String resultCount = getActivity().getString(R.string.result_counter);
        return String.format(resultCount, mIndex + 1, mNumResults);
    }

    /**
     * Returns whether the current Picks is the last Pick
     * @return True if the current Pick/Result is the last one. False, otherwise
     */
    public boolean isLast() {
        return mIndex == mNumResults - 1;
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

        public void onMoveToResult(int index);

    }

}
