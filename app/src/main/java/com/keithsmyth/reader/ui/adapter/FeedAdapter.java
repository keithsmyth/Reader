package com.keithsmyth.reader.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keithsmyth.reader.R;
import com.keithsmyth.reader.model.Feed;

import java.util.List;

import butterknife.ButterKnife;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private final AdapterDelegate<Feed> delegate;

    public FeedAdapter() {
        delegate = new AdapterDelegate<>(this);
    }

    public void setListener(AdapterDelegate.Listener<Feed> listener) {
        delegate.setListener(listener);
    }

    public void setItems(List<Feed> items) {
        delegate.setItems(items);
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedViewHolder(delegate.inflateView(parent, R.layout.item_feed));
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        final Feed feed = delegate.getItem(position);
        holder.nameText.setText(feed.name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.tryNotifyItemClick(feed);
            }
        });
    }

    @Override
    public int getItemCount() {
        return delegate.getItemCount();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameText;

        public FeedViewHolder(View itemView) {
            super(itemView);
            nameText = ButterKnife.findById(itemView, R.id.name);
        }
    }
}
