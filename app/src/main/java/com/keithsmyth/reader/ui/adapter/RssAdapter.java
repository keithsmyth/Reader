package com.keithsmyth.reader.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.keithsmyth.reader.R;
import com.keithsmyth.reader.data.EntryStatusProvider;
import com.keithsmyth.reader.model.Entry;
import com.keithsmyth.reader.ui.event.RssItemClickEvent;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class RssAdapter extends RecyclerView.Adapter<RssAdapter.RssViewHolder> {

    private final List<Entry> items = new ArrayList<>();
    private final EntryStatusProvider entryStatus;
    private Bus bus;

    public RssAdapter(EntryStatusProvider entryStatus) {
        this.entryStatus = entryStatus;
    }

    public void register(Bus bus) {
        this.bus = bus;
        this.bus.register(this);
    }

    public void unregister() {
        if (bus == null) { return; }
        bus.unregister(this);
    }

    public void setItems(List<Entry> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override public RssViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        final View view = inflater.inflate(R.layout.item_rss, viewGroup, false);
        return new RssViewHolder(view);
    }

    @Override public void onBindViewHolder(RssViewHolder holder, final int position) {
        final Entry entry = items.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                entryStatus.setRead(entry.id);
                notifyItemChanged(position);
                if (bus != null) {
                    bus.post(new RssItemClickEvent(entry));
                }
            }
        });

        final int style = entryStatus.isRead(entry.id) ? Typeface.NORMAL : Typeface.BOLD;
        holder.rss.setTypeface(null, style);
        holder.rss.setText(entry.title);

        Glide.with(holder.context)
            .load(entry.thumbnailUrl)
            .centerCrop()
            .crossFade()
            .into(holder.thumb);
    }

    @Override public int getItemCount() {
        return items.size();
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

