package com.habosa.notificationbox;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.habosa.notificationbox.adapter.AppAdapter;
import com.habosa.notificationbox.model.AppDisplayInfo;
import com.habosa.notificationbox.util.Resource;
import com.habosa.notificationbox.viewmodel.AppSelectionActivityViewModel;

import java.util.List;

/**
 * Activity for selecting which apps to monitor.
 */
public class AppSelectionActivity extends AppCompatActivity {

    private static final String TAG = "AppSelectionActivity";

    private AppSelectionActivityViewModel mViewModel;

    private RecyclerView mAppRecycler;
    private ProgressBar mProgressBar;
    private AppAdapter mAppAdapter = new AppAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_selection);

        mAppRecycler = findViewById(R.id.recycler_app);
        mProgressBar = findViewById(R.id.progress_app);

        mViewModel = ViewModelProviders.of(this).get(AppSelectionActivityViewModel.class);

        mAppRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAppRecycler.setAdapter(mAppAdapter);

        mViewModel.getApps().observe(this, new Observer<Resource<List<AppDisplayInfo>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<AppDisplayInfo>> listResource) {
                onApps(listResource);
            }
        });

        mViewModel.startLoadApps();
    }

    private void onApps(@Nullable Resource<List<AppDisplayInfo>> listResource) {
        if (listResource == null) {
            Log.w(TAG, "listResource == null");
            return;
        }

        switch (listResource.getState()) {
            case LOADING:
                mProgressBar.setVisibility(View.VISIBLE);
                mAppRecycler.setVisibility(View.GONE);
                break;
            case FAILURE:
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(this, "App loading failed...", Toast.LENGTH_LONG).show();
                break;
            case SUCCESS:
                mProgressBar.setVisibility(View.GONE);
                mAppRecycler.setVisibility(View.VISIBLE);
                mAppAdapter.setApps(listResource.getResult());
                break;
        }
    }


}
