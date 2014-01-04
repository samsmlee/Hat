package com.simtuit.hat;

import android.app.Activity;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditVoteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditVoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class EditVoteFragment extends DialogFragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_VOTECONTENT = "vote_content";
    private static final String VOTE_POSITION = "vote_position";

    private String mVoteContent;
    private int mVotePosition;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     *
     *
     *
     * @param vote_position
     * @param vote_content Parameter 1.
     * @return A new instance of fragment EditVoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditVoteFragment newInstance(int vote_position, String vote_content) {
        EditVoteFragment fragment = new EditVoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VOTECONTENT, vote_content);
        args.putInt(VOTE_POSITION, vote_position);
        fragment.setArguments(args);
        return fragment;
    }
    public EditVoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the style of the dialog
        setStyle(STYLE_NORMAL, R.style.EditVoteDialogTheme);
        if (getArguments() != null) {
            mVoteContent = getArguments().getString(ARG_VOTECONTENT);
            mVotePosition = getArguments().getInt(VOTE_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_vote, container, false);


        // Set up the dialog
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().setTitle(getResources().getString(R.string.edit_vote_dialog_title));
        ((EditText)view.findViewById(R.id.vote_edittext)).setText(mVoteContent);

        // Set this as onClickListener for its buttons
        ((ImageButton)view.findViewById(R.id.edit_button)).setOnClickListener(this);
        ((ImageButton)view.findViewById(R.id.delete_button)).setOnClickListener(this);
        

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_button:
                EditText vote_content = (EditText)getView().findViewById(R.id.vote_edittext);
                mListener.onEdited(mVotePosition, vote_content.getText().toString());
                getDialog().dismiss();
                break;
            case R.id.delete_button:
                mListener.onDeleted(mVotePosition);
                getDialog().dismiss();
                break;
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

        public void onEdited(int position, String newContent);

        public void onDeleted(int position);
    }

}
