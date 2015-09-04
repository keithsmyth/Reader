package com.keithsmyth.reader.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.keithsmyth.reader.R;
import com.keithsmyth.reader.data.RssController;
import com.keithsmyth.reader.model.Entry;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class RssAdapter extends RecyclerView.Adapter<RssAdapter.RssViewHolder> {

    private final List<Entry> items = new ArrayList<>();
    private final RssController entryStatus;
    private Listener listener;

    public RssAdapter(RssController entryStatus) {
        this.entryStatus = entryStatus;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<Entry> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public RssViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        final View view = inflater.inflate(R.layout.item_rss, viewGroup, false);
        return new RssViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RssViewHolder holder, final int position) {
        final Entry entry = items.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entryStatus.setEntryRead(entry.id, true);
                notifyItemChanged(holder.getAdapterPosition());
                if (listener != null) {
                    listener.onEntryClicked(entry);
                }
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

    public interface Listener {
        void onEntryClicked(Entry entry);
    }
}

