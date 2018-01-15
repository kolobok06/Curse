package com.example.yurez.hc2;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HistoryFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;
    ListView listView;
    public HistoryListAdapter historyListAdapter;

    public HistoryFragment()
    { // Required empty public constructor
    }

    public static HistoryFragment newInstance()
    {
        return new HistoryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        refreshHistoryList();
    }

    public void refreshHistoryList()
    {
        if (historyListAdapter != null)
        {
            historyListAdapter.notifyDataSetChanged();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        historyListAdapter = new HistoryListAdapter(getContext(), MedDataHolder.aHistory);
        listView = (ListView) view.findViewById(R.id.listHistory);
        listView.setAdapter(historyListAdapter);
        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
            mListener = (OnFragmentInteractionListener) context;
        else
            throw new RuntimeException(context.toString() +
                    " must implement OnFragmentInteractionListener");
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
    }
}
