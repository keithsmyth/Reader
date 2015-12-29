package com.keithsmyth.reader.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.keithsmyth.reader.R;
import com.keithsmyth.reader.data.controller.RssController;
import com.keithsmyth.reader.model.Entry;

import java.util.List;

import butterknife.ButterKnife;

public class RssAdapter extends RecyclerView.Adapter<RssAdapter.RssViewHolder> {

    private final AdapterDelegate<Entry> delegate;
    private final RssController entryStatus;

    public RssAdapter(RssController entryStatus) {
        this.entryStatus = entryStatus;
        delegate = new AdapterDelegate<>(this);
    }

    public void setListener(AdapterDelegate.Listener<Entry> listener) {
        delegate.setListener(listener);
    }

    public void setItems(List<Entry> items) {
        delegate.setItems(items);
    }

    @Override
    public RssViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RssViewHolder(delegate.inflateView(parent, R.layout.item_rss));
    }

    @Override
    public void onBindViewHolder(final RssViewHolder holder, int position) {
        final Entry entry = delegate.getItem(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entryStatus.setEntryRead(entry.id, true);
                notifyItemChanged(holder.getAdapterPosition());
                delegate.tryNotifyItemClick(entry);
            }
        });

        final int style = entry.isRead ? Typeface.NORMAL : Typeface.BOLD;
        holder.rss.setTypeface(null, style);
        holder.rss.setText(entry.title);

        Glide.with(holder.context)
                .load(entry.thumbnailUrl)
                .centerCrop()
                .crossFade()
                .into(holder.thumb);
    }

    @Override
    public int getItemCount() {
        return delegate.getItemCount();
    }

    public static class RssViewHolder extends RecyclerView.ViewHolder {

        private final Context context;
        private final TextView rss;
        private final ImageView thumb;

        public RssViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            rss = ButterKnife.findById(itemView, R.id.rss);
            thumb = ButterKnife.findById(itemView, R.id.thumb);
        }
    }
}

