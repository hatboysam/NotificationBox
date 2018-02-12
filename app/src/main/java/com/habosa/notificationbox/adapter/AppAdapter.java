package com.habosa.notificationbox.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.habosa.notificationbox.R;
import com.habosa.notificationbox.model.AppDisplayInfo;

import java.util.List;

/**
 * RecyclerView adapter for {@link com.habosa.notificationbox.AppSelectionActivity}.
 */
public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {

    private List<AppDisplayInfo> mInfos;

    public AppAdapter() {
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIconView;
        private TextView mTitleView;
        private CheckBox mCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);

            mIconView = itemView.findViewById(R.id.item_app_icon);
            mTitleView = itemView.findViewById(R.id.item_app_title);
            mCheckBox = itemView.findViewById(R.id.item_app_checkbox);

            // TODO: Checked change listener?
        }

        public void bind(AppDisplayInfo info) {
            // TODO: Does this need to be done on a bg thread?
            mTitleView.setText(info.title);
            mIconView.setImageDrawable(info.icon);
        }
    }
}
