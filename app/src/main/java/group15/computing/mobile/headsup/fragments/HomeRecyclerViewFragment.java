package group15.computing.mobile.headsup.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import group15.computing.mobile.headsup.R;
import group15.computing.mobile.headsup.SpaceDash.AdSpaceItem;
import group15.computing.mobile.headsup.SpaceDash.AnnouncementSpaceItem;
import group15.computing.mobile.headsup.SpaceDash.RecyclerViewAdapter;
import group15.computing.mobile.headsup.SpaceDash.SpaceDashContent;
import group15.computing.mobile.headsup.SpaceDash.UserSpaceItem;

public class HomeRecyclerViewFragment extends RecyclerViewFragment {

    @Override
    public void generateContentFromJson(String data) {

        Gson gson = new Gson();
        SpaceDashContent content = gson.fromJson(data, SpaceDashContent.class);

        for(AnnouncementSpaceItem announcement : content.getAnnouncementSpaceItems()){
            mContentItems.add(announcement);
        }
        for(AdSpaceItem ad : content.getAdSpaceItems()){
            mContentItems.add(ad);
        }
        for(UserSpaceItem user : content.getUserSpaceItems()){
            mContentItems.add(user);
        }
    }
}
