package com.smile.studio.libsmilestudio.recyclerviewer;

import android.view.View;

/**
 * Created by admin on 24/07/2017.
 */

public interface OnItemFocusListenerRecyclerView {
    void onFocusIn(View view, int position);

    void onFocusOut(View view, int position);
}
