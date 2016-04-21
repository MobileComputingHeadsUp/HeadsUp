package group15.computing.mobile.headsup.SpaceDash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import group15.computing.mobile.headsup.Forms.SpaceProfile;
import group15.computing.mobile.headsup.R;
import group15.computing.mobile.headsup.SpaceDash.RecyclerViewAdapter;
import group15.computing.mobile.headsup.SpaceDash.SpaceItem;
import group15.computing.mobile.headsup.activities.MainActivity;
import group15.computing.mobile.headsup.activities.SpaceDashboard;

public abstract class RecyclerViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private SpaceDashboard parentActivity;
    private SwipeRefreshLayout swipeContainer;

    protected List<SpaceItem> mContentItems = new ArrayList<>();
    protected PriorityQueue<SpaceItem> itemSorter = new PriorityQueue<>(5, new SpaceItemComparator());

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new RecyclerViewMaterialAdapter(new RecyclerViewAdapter(mContentItems));
        mRecyclerView.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);

        // Setup the swipe refresh cointainer
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                parentActivity.initFeedRefresh();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.primary);
    }

    public abstract void generateContentFromJson(String data);

    public void refreshContent(String data){

        // Generate content.
        generateContentFromJson(data);
        swipeContainer.setRefreshing(false);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    public void setParentActivity(SpaceDashboard parentActivity){
        this.parentActivity = parentActivity;
    }

    public void startLoadingIcon(){
        if(swipeContainer!=null)
            swipeContainer.setRefreshing(true);
    }
}
