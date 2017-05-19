package com.seu.magiccamera.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seu.magiccamera.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luoyin on 2017/5/18.
 */

public class PoemAdapter extends RecyclerView.Adapter<PoemAdapter.Holder> {
    @NonNull
    private ArrayList concepts = new ArrayList<>();
    public PoemAdapter setData(@NonNull ArrayList concepts) {
        this.concepts = concepts;
        notifyDataSetChanged();
        return this;
    }

    @Override public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_concept, parent, false));

    }

    @Override public void onBindViewHolder(Holder holder, int position) {
//    final Concept concept = concepts.get(position);
//    holder.label.setText(concept.name() != null ? concept.name() : concept.id());
//    holder.probability.setText(String.valueOf(concept.value()));
        holder.label.setText(concepts.get(position).toString());
    }

    @Override public int getItemCount() {
        return concepts.size();
    }

    static final class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.label)
        TextView label;
//    @BindView(R.id.probability) TextView probability;

        public Holder(View root) {
            super(root);
            ButterKnife.bind(this, root);
        }
    }
}
