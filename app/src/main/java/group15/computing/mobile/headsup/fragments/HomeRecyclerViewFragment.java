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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import group15.computing.mobile.headsup.R;
import group15.computing.mobile.headsup.SpaceDash.RecyclerViewAdapter;

public class HomeRecyclerViewFragment extends RecyclerViewFragment {

    @Override
    public void generateContentFromJson(String data) {

        Log.d("BLAHLBLHLSDLKN", data);

        int ITEM_COUNT = 100;
        for(int i = 0; i < ITEM_COUNT; i++){
            mContentItems.add(new Object());
        }
    }
}
