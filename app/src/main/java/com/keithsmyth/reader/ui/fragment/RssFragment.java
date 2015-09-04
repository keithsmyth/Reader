package com.keithsmyth.reader.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keithsmyth.reader.AppModule;
import com.keithsmyth.reader.R;
import com.keithsmyth.reader.data.DaggerDataComponent;
import com.keithsmyth.reader.data.RssController;
import com.keithsmyth.reader.data.local.provider.FeedProvider;
import com.keithsmyth.reader.model.Entry;
import com.keithsmyth.reader.model.Feed;
import com.keithsmyth.reader.ui.adapter.RssAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RssFragment extends Fragment implements RssAdapter.Listener {

    @Inject RssController rssController;
    @Inject FeedProvider feedProvider;

    private RssAdapter adapter;
    private Subscription feedSub;
    private Subscription entriesSub;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rss, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView list = ButterKnife.findById(view, R.id.list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RssAdapter(rssController);
        adapter.setListener(this);
        list.setAdapter(adapter);

        // feed
        feedSub = feedProvider.get("tag:blogger.com,1999:blog-6755709643044947179")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Feed>() {
                    @Override
                    public void call(Feed feed) {
                        if (getView() == null) {
                            return;
                        }
                        subscribeEntries(feed);
                    }
                });
    }

    private void subscribeEntries(Feed feed) {
        if (entriesSub != null) {
            return;
        }
        entriesSub = rssController.getRssFeed(feed)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Entry>>() {
                    @Override
                    public void call(List<Entry> entries) {
                        if (getView() == null) {
                            return;
                        }
                        adapter.setItems(entries);
                    }
                });
    }

    @Override
    public void onEntryClicked(Entry entry) {
        if (TextUtils.isEmpty(entry.url)) {
            return;
        }
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(entry.url));
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        if (adapter != null) {
            adapter.setListener(null);
        }
        if (entriesSub != null) {
            entriesSub.unsubscribe();
        }
        if (feedSub != null) {
            feedSub.unsubscribe();
        }
        super.onDestroy();
    }
}
