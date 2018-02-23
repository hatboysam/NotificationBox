package com.habosa.notificationbox.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.habosa.notificationbox.R;
import com.habosa.notificationbox.model.AppDisplayInfo;
import com.habosa.notificationbox.util.PreferenceUtils;

import java.util.List;

/**
 * RecyclerView adapter for {@link com.habosa.notificationbox.AppSelectionActivity}.
 */
public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {

    private List<AppDisplayInfo> mInfos;
    private final PreferenceUtils mPrefUtils;

    public AppAdapter(PreferenceUtils prefUtils) {
        mPrefUtils = prefUtils;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_app, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AppDisplayInfo info = mInfos.get(position);
        holder.bind(info);
    }

    @Override
    public int getItemCount() {
        if (mInfos == null) {
            return 0;
        }

        return mInfos.size();
    }

    public void setApps(List<AppDisplayInfo> infos) {
        mInfos = infos;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        private ImageView mIconView;
        private TextView mTitleView;
        private CheckBox mCheckBox;

        private String packageName;

        public ViewHolder(View itemView) {
            super(itemView);

            mIconView = itemView.findViewById(R.id.item_app_icon);
            mTitleView = itemView.findViewById(R.id.item_app_title);
            mCheckBox = itemView.findViewById(R.id.item_app_checkbox);

            mCheckBox.setOnCheckedChangeListener(this);
        }

        public void bind(AppDisplayInfo info) {
            // TODO: Does this need to be done on a bg thread?
            mTitleView.setText(info.name);
            mIconView.setImageDrawable(info.icon);

            // Record package name
            this.packageName = info.packageName;

            // Set initial checked state
            boolean selected = mPrefUtils.getAppSelected(packageName);

            mCheckBox.setOnCheckedChangeListener(null);
            mCheckBox.setChecked(selected);
            mCheckBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mPrefUtils.setAppSelected(packageName, isChecked);
        }
    }
}
