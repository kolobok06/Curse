package com.example.yurez.hc2;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class AllMedsFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;
    ListView listView;
    public AllMedsListAdapter allMedsListAdapter;

    public AllMedsFragment()
    { // Required empty public constructor
    }

    public static AllMedsFragment newInstance()
    {
        return new AllMedsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        refreshAllMedsList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_all_meds, container, false);

        allMedsListAdapter = new AllMedsListAdapter(getContext(), MedDataHolder.aAllMeds);

        listView = (ListView) view.findViewById(R.id.listAllMeds);
        listView.setAdapter(allMedsListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                onAllMedsListItemClicked(i);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                onAllMedsListItemLongClicked(position);
                return true;
            }
        });
        return view;
    }

    public void onAllMedsListItemClicked(Integer index)
    {
        if (mListener != null)
            mListener.onFragmentAllMedsListItemClicked(index);
    }

    public void onAllMedsListItemLongClicked(int index)
    {
        if (mListener != null)
            mListener.onFragmentAllMedsListItemLongClicked(index);
    }

    public void refreshAllMedsList()
    {
        if (allMedsListAdapter != null)
        {
            allMedsListAdapter.notifyDataSetChanged();
        }
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
        void onFragmentAllMedsListItemClicked(Integer index);

        void onFragmentAllMedsListItemLongClicked(int index);
    }
}
