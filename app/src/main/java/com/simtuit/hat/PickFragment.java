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

    private String[] mPicked;

    private OnFragmentInteractionListener mListener;

    private View mResultView;

    private int mLongAnimationDuration;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param picked (String) picked Vote.
     * @return A new instance of fragment PickFragment.
     */
    public static PickFragment newInstance(String[] picked) {
        PickFragment fragment = new PickFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PICKED, picked);
        fragment.setArguments(args);
        return fragment;
    }
    public PickFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPicked = getArguments().getStringArray(ARG_PICKED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pick, container, false);

        // Initially hide the result view.
        mResultView = view.findViewById(R.id.view_result);
        mResultView.setVisibility(View.GONE);

        // Retrieve and cache the system's default "short" animation time.
        mLongAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);

        ImageButton restartButton = (ImageButton) view.findViewById(R.id.edit_hat_button);
        restartButton.setOnClickListener(this);

        // Show the result
        showResult(view, mPicked);

        // Inflate the layout for this fragment
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        }
    }

    /**
     * It shows the result: Fades out, updates the result view and fade it back in.
     * @param view      The main view of (@link PickFragment)
     * @param picked    Array of Strings that contains the shuffled list of (@VoteFragment.Vote)
     */
    public void showResult(View view, String[] picked) {

        // Fade Out, including the result view(if any result is showing)
        fadeOutResult();
        // Update the result view
        updatePick(view, picked, 0);
        // Fade the result view back in
        fadeInResult();
    }

    /**
     * Updates the result
     * @param view      The main view of (@link PickFragment)
     * @param picked    Array of Strings that contains the shuffled list of (@VoteFragment.Vote)
     * @param index     index in (@link picked) to show
     */
    protected void updatePick(View view, String[] picked, int index) {
        if (view == null) {
            return;
        }

        if (picked == null) {
            clearResult(view);
        } else {

            // Show the result counter: Result %d of %d
            TextView resultCounter = (TextView) view.findViewById(R.id.textview_result_counter);

            if (resultCounter != null) {
                String resultCount = getActivity().getString(R.string.result_counter);

                resultCounter.setText(String.format(resultCount, index + 1, picked.length));
            }

            // Show the result
            ((TextView) view.findViewById(R.id.textview_result)).setText(picked[index]);
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
     * @param view  The main view of (@link PickFragment)
     */
    protected void clearResult(View view) {
        if (view == null) {
            return;
        }

        TextView resultCounter = ((TextView) view.findViewById(R.id.textview_result_counter));
        if (resultCounter != null) {

            resultCounter.setText("");
        }

        TextView result = ((TextView) view.findViewById(R.id.textview_result));

        if (result != null) {
            result.setText("");
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
