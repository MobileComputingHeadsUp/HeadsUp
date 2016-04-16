package group15.computing.mobile.headsup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Bradley on 4/16/16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Object> contents; // TODO: Use specific Objects here

    // TODO: Dont do this
    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    public RecyclerViewAdapter(List<Object> contents) { this.contents = contents; }

    // TODO: This wont be necessary either.
    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        // TODO: THis is where we should specify different layouts for differnt card types.
        // And have each type provide the funcitonality for bulding its view.
        switch(viewType){
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_big, parent, false );
                return new RecyclerView.ViewHolder(view){};
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_small, parent, false);
                return new RecyclerView.ViewHolder(view){};
            }
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // TODO: Do something more meaningful here?
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                break;
            case TYPE_CELL:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }
}
