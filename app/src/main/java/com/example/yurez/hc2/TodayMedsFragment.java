package com.example.yurez.hc2;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class TodayMedsFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;
    ListView listView;
    public TodayListAdapter todayListAdapter;

    public TodayMedsFragment()
    {
        // Required empty public constructor
    }

    public static TodayMedsFragment newInstance()
    {
        return new TodayMedsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_today_meds, container, false);

        todayListAdapter = new TodayListAdapter(getContext(), MedDataHolder.aTodayMeds);

        listView = (ListView) view.findViewById(R.id.listTodayMeds);
        listView.setAdapter(todayListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                onTodayMedsListItemClicked(i);
            }
        });
        return view;
    }

    public void onTodayMedsListItemClicked(Integer index)
    {
        if (mListener != null)
        {
            mListener.onFragmentTodayMedsListItemClicked(index);
        }
    }

    public void refreshTodayMedsList()
    {
        if (todayListAdapter != null)
            todayListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        void onFragmentTodayMedsListItemClicked(Integer index);
    }
}
