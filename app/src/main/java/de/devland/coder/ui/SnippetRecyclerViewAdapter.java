package de.devland.coder.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.devland.coder.R;
import de.devland.coder.model.Snippet;
import lombok.Setter;

public class SnippetRecyclerViewAdapter extends RecyclerView.Adapter<SnippetRecyclerViewAdapter.ViewHolder> {

    @Setter
    private List<Snippet> values;

    public SnippetRecyclerViewAdapter(List<Snippet> items) {
        values = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_snippet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = values.get(position);
        holder.title.setText(holder.item.getUser() + "/" + holder.item.getRepository());
        holder.fileName.setText(holder.item.getFile());
        holder.thumb.setImageResource(holder.item.isElegant() ? R.drawable.ic_action_thumb_up : R.drawable.ic_thumb_down);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        @BindView(R.id.textView_title)
        public TextView title;
        @BindView(R.id.textView_file)
        public TextView fileName;
        @BindView(R.id.imageView_thumb)
        public ImageView thumb;
        public Snippet item;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }
}
