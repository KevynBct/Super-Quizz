package fr.diginamic.formation.superquizz.ui.fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import fr.diginamic.formation.superquizz.R;
import fr.diginamic.formation.superquizz.broadcast.NetworkChangeReceiver;
import fr.diginamic.formation.superquizz.database.QuestionsDatabaseHelper;
import fr.diginamic.formation.superquizz.model.Question;


public class QuestionListFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private QuestionListListener mListener;
    private QuestionRecyclerViewAdapter adapter;
    private boolean networkConnected = true;
    private Menu menu;

    public QuestionListFragment() {
    }

    public static QuestionListFragment newInstance(int columnCount) {
        QuestionListFragment fragment = new QuestionListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);

        setHasOptionsMenu(true);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new QuestionRecyclerViewAdapter(QuestionsDatabaseHelper.getInstance(getContext()).getAllQuestions(), mListener);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.question_list_menu, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_download){
            if(networkConnected){
                QuestionsDatabaseHelper.getInstance(getContext()).downloadOnlineQuestions();
                SystemClock.sleep(2000);
                mListener.updateQuestionsListFragment();
            }else{
                Toast.makeText(getContext(), "Vous n'êtes pas connecté à internet", Toast.LENGTH_SHORT).show();
            }

        }else if (id == R.id.action_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Voulez vous supprimer toutes les questions ?")
                    .setTitle("Suppression");
            builder.setPositiveButton("Oui", (dialog1, which) -> {
                QuestionsDatabaseHelper.getInstance(getContext()).deleteAllQuestions();
                mListener.updateQuestionsListFragment();

            });
            builder.setNegativeButton("Non", (dialog1, which) -> Log.i("DIALOG", "Annuler"));
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof QuestionListListener) {
            mListener = (QuestionListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

        registerReceiver();
    }

    @Override
    public void onDetach() {
        try
        {
            getActivity().unregisterReceiver(internalNetworkChangeReceiver);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        super.onDetach();
        mListener = null;
    }

    private void registerReceiver()
    {
        try
        {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(NetworkChangeReceiver.NETWORK_CHANGE_ACTION);
            getActivity().registerReceiver(internalNetworkChangeReceiver, intentFilter);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    InternalNetworkChangeReceiver internalNetworkChangeReceiver = new InternalNetworkChangeReceiver();
    class InternalNetworkChangeReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            networkConnected = intent.getBooleanExtra("status", false);

            if(networkConnected){
                menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_load_white_24dp));
            }else{
                menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_load_problem_white_24dp));
            }
        }
    }


    public interface QuestionListListener {
        void onListFragmentInteraction(Question question);
        void onLongClickQuestion(Question question);
        void updateQuestionsListFragment();
    }
}
