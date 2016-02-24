package waikato.karun.comp591.spontaneousvocabulary.project.unscramble;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HintFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HintFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HintFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView heading;
    private TextView hint;
    private View v;
    private ArrayList<String> hints;


    private OnFragmentInteractionListener mListener;

    public HintFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HintFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HintFragment newInstance(String param1, String param2) {
        HintFragment fragment = new HintFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        hints = new ArrayList<String>();
        addHints();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_hint, container, false);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        heading = (TextView) v.findViewById(R.id.heading_fragment);
        hint = (TextView) v.findViewById(R.id.hint_fragment);
        addCursiveTypeface(heading);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void addCursiveTypeface(TextView textView) {
        // Font path
        String fontPath = "fonts/Frosting-for-Breakfast_regular.otf";
        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
        // Applying font
        textView.setTypeface(tf);
    }

    public void updateHints() {
        if (!hints.isEmpty()) {
            innerUpdateHints();
        }
        else{
            addHints();
            innerUpdateHints();
        }

    }

    private void addHints() {
        hints.add("It can refer to a movie that spoofs real events");
        hints.add("It is synonymous with parody");
        hints.add("It rhymes with vampire");
    }

    private void innerUpdateHints() {
        hint.setText(hints.get(0));
        hints.remove(0);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
