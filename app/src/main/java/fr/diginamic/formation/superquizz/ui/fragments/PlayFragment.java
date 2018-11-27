package fr.diginamic.formation.superquizz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.diginamic.formation.superquizz.R;

public class PlayFragment extends Fragment {

    private PlayFragmentListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_play, container, false);
        view.findViewById(R.id.play_button).setOnClickListener(v -> {
            mListener.onPlayButton();
        });

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PlayFragmentListener) {
            mListener = (PlayFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface PlayFragmentListener {
        void onPlayButton();
    }


}
