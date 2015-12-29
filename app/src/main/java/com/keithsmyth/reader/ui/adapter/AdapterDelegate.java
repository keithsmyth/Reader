package com.keithsmyth.reader.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class AdapterDelegate<T> {

    private final List<T> items = new ArrayList<>();
    private final RecyclerView.Adapter adapter;
    private Listener<T> listener;

    public AdapterDelegate(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    public void setListener(Listener<T> listener) {
        this.listener = listener;
    }

    public void setItems(List<T> items) {
        this.items.clear();
        this.items.addAll(items);
        adapter.notifyDataSetChanged();
    }

    public View inflateView(ViewGroup viewGroup, @LayoutRes int resource) {
        final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return inflater.inflate(resource, viewGroup, false);
    }

    public int getItemCount() {
        return items.size();
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public void tryNotifyItemClick(T item) {
        if (listener != null) {
            listener.onItemClick(item);
        }
    }

    public interface Listener<T> {
        void onItemClick(T item);
    }
}
