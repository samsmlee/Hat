package com.simtuit.hat;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PICKED = "mPicked";

    private String mPicked;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param picked (String) picked Vote.
     * @return A new instance of fragment PickFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PickFragment newInstance(String picked) {
        PickFragment fragment = new PickFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PICKED, picked);
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
            mPicked = getArguments().getString(ARG_PICKED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_pick, container, false);

        ImageButton restartButton = (ImageButton) view.findViewById(R.id.edit_hat_button);
        restartButton.setOnClickListener(this);

        updatePick(view, mPicked);

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onEditHat();
        }
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

    public void updatePick(View view, String picked)
    {
        ((TextView)view.findViewById(R.id.result_textview)).setText(picked);
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
