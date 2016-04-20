package group15.computing.mobile.headsup.SpaceDash;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import group15.computing.mobile.headsup.R;

/**
 * Created by Bradley on 4/16/16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<SpaceItem> contents; //

    public RecyclerViewAdapter(List<SpaceItem> contents) { this.contents = contents; }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        SpaceItem spaceItem = contents.get(viewType);

        view = spaceItem.getInflatedLayout(parent);
        spaceItem.configView(view);
        return new RecyclerView.ViewHolder(view){};
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {}

    @Override
    public int getItemCount() {
        return contents.size();
    }
}
