package com.simtuit.hat;

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

        ImageButton restartButton = (ImageButton) view.findViewById(R.id.edit_hat_button);
        restartButton.setOnClickListener(this);

        ImageButton repickbutton = (ImageButton) view.findViewById(R.id.button_repick_hat);
        repickbutton.setOnClickListener(this);

        updatePick(view, mPicked, 0);

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
     * If button_repick_hat was clicked,
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
            case R.id.button_repick_hat:
                ((HatActivity)getActivity()).onRePick(this.getView());
                break;
        }
    }

    /**
     * Updates the result
     * @param view      The main view of (@link PickFragment)
     * @param picked    Array of Strings that contains the shuffled list of (@VoteFragment.Vote)
     * @param index     index in (@link picked) to show
     */
    public void updatePick(View view, String[] picked, int index) {
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

    public void showShuffle(View view) {
        clearResult(view);

        if (view == null) {
            return;
        }

        TextView resultCounter = (TextView) view.findViewById(R.id.textview_result_counter);
        String shuffling = getActivity().getString(R.string.shuffling);

        if (resultCounter != null) {
            resultCounter.setText(shuffling);


        }

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

        public void onRePick(View view);

    }

}
