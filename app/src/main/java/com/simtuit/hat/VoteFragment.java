package com.simtuit.hat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class VoteFragment
        extends Fragment
        implements AbsListView.OnItemClickListener, View.OnClickListener, TextView.OnEditorActionListener{


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    private Button mAddButton;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static VoteFragment newInstance(String param1, String param2) {
        VoteFragment fragment = new VoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAdapter = new ArrayAdapter<VoteContent.Vote>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, VoteContent.ITEMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_vote, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        EditText voteContent = (EditText) view.findViewById(R.id.vote_content);
        voteContent.setOnEditorActionListener(this);


        mAddButton = (Button) view.findViewById(R.id.add_button);
        mAddButton.setOnClickListener(this);

        Button pickButton = (Button) view.findViewById(R.id.pick_button);
        pickButton.setOnClickListener(this);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
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

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.add_button:
            {
                this.addVote();
                break;
            }
            case R.id.pick_button:
            {
                if(mAdapter.getCount() <= 0)
                    alertEmptyHat();
                else
                    ((HatActivity)getActivity()).onPick(pick());
                break;
            }
        }

    }

    protected void alertEmptyHat() {

        Context context = getActivity();

        CharSequence text = context.getString(R.string.empty_hat_warning);
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }


    /**
     * Picks out of the Hat
     * @return (VoteContent.Vote) picked Vote
     */
    protected VoteContent.Vote pick()
    {
        Random r = new Random(System.nanoTime());
        return (VoteContent.Vote)mAdapter.getItem(r.nextInt(mAdapter.getCount()));
    }

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

        public void onPick(VoteContent.Vote picked);
    }

}