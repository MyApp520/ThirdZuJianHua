package com.example.find.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.find.R;
import com.example.find.R2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PoliceAlarmVideoFragment extends Fragment {


    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;

    private final String TAG = getClass().getSimpleName();
    private Context context;

    public static PoliceAlarmVideoFragment newInstance(int flag) {
        // Required empty public constructor
        PoliceAlarmVideoFragment instance = new PoliceAlarmVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("flag", flag);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_police_alarm_video, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
        Log.e(TAG, "onActivityCreated: 子fragment");
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        BaseQuickAdapter baseQuickAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_simple_text, null) {

            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.textView, item);
            }
        };
        recyclerView.setAdapter(baseQuickAdapter);

        int flag = getArguments().getInt("flag");
        List<String> data = new ArrayList<>();
        for (int i = 0; i < flag; i++) {
            if (flag == 99) {
                data.add("解放军第 " + i + " 兵团");
            } else if (flag == 88) {
                data.add("八路军第 " + i + " 纵队");
            } else {
                data.add("新四军第 " + i + " 支队");
            }
        }
        baseQuickAdapter.setNewData(data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
