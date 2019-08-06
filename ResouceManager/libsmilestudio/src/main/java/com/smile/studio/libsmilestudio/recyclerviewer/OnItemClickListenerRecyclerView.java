package com.smile.studio.libsmilestudio.recyclerviewer;

import android.view.View;

/**
 * Created by admin on 24/07/2017.
 */

public interface OnItemClickListenerRecyclerView {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
