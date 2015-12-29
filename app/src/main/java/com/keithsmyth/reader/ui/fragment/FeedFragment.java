package com.keithsmyth.reader.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keithsmyth.reader.AppModule;
import com.keithsmyth.reader.R;
import com.keithsmyth.reader.data.DaggerDataComponent;
import com.keithsmyth.reader.data.controller.FeedController;
import com.keithsmyth.reader.model.Feed;
import com.keithsmyth.reader.ui.activity.Navigatable;
import com.keithsmyth.reader.ui.adapter.AdapterDelegate;
import com.keithsmyth.reader.ui.adapter.FeedAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class FeedFragment extends Fragment implements AdapterDelegate.Listener<Feed> {

    private Navigatable navigatable;
    private FeedAdapter adapter;
    private Subscription feedSub;

    @Inject FeedController feedController;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigatable = (Navigatable) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerDataComponent.builder()
                .appModule(new AppModule(getActivity().getApplication()))
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView list = ButterKnife.findById(view, R.id.list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FeedAdapter();
        adapter.setListener(this);
        list.setAdapter(adapter);

        feedSub = feedController.getFeeds()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Feed>>() {
                    @Override
                    public void call(List<Feed> feeds) {
                        if (getView() == null) {
                            return;
                        }
                        adapter.setItems(feeds);
                    }
                });
    }

    @Override
    public void onItemClick(Feed item) {
        navigatable.startFragment(RssFragment.newInstance(item.id));
    }

    @Override
    public void onDestroy() {
        if (adapter != null) {
            adapter.setListener(null);
        }
        if (feedSub != null) {
            feedSub.unsubscribe();
        }
        super.onDestroy();
    }
}
