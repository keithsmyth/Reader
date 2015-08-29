package com.keithsmyth.reader.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.keithsmyth.reader.R;
import com.keithsmyth.reader.data.BusProvider;
import com.keithsmyth.reader.data.GetRssTask;
import com.keithsmyth.reader.model.Entry;
import com.keithsmyth.reader.ui.adapter.RssAdapter;
import com.keithsmyth.reader.ui.event.RssItemClickEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.ButterKnife;

public class RssFragment extends Fragment {

    private final Bus bus = BusProvider.provide();
    private RssAdapter adapter;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rss, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView list = ButterKnife.findById(view, R.id.list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RssAdapter();
        list.setAdapter(adapter);

        final GetRssTask task = new GetRssTask(new GetRssTask.Listener() {
            @Override public void onGet(List<Entry> items) {
                if (getView() == null || items == null) { return; }
                adapter.setItems(items);
            }

            @Override public void onError(String msg) {
                if (getView() == null) { return; }
                Log.e(getClass().getSimpleName(), msg);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
        task.execute("http://feeds.feedburner.com/blogspot/hsDu?format=xml");
    }

    @Override public void onResume() {
        super.onResume();
        bus.register(this);
        adapter.register(bus);
    }

    @Override public void onPause() {
        super.onPause();
        bus.unregister(this);
        adapter.unregister();
    }

    @Subscribe public void onRssItemClick(RssItemClickEvent event) {
        final Entry entry = event.entry;
        if (TextUtils.isEmpty(entry.url)) { return; }
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(entry.url));
        startActivity(intent);
    }
}
