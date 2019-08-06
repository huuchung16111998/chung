package com.smile.studio.libsmilestudio.recyclerviewer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    RecyclerView.LayoutManager mLayoutManager;
    int totalItemCount;
    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private int startingPageIndex = 0;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager layoutManager, int page) {
        this.mLayoutManager = layoutManager;
        currentPage = page;
        startingPageIndex = currentPage;
    }

    public EndlessRecyclerOnScrollListener(GridLayoutManager layoutManager, int page) {
        this.mLayoutManager = layoutManager;
        currentPage = page;
        startingPageIndex = currentPage;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public EndlessRecyclerOnScrollListener(StaggeredGridLayoutManager layoutManager, int page) {
        this.mLayoutManager = layoutManager;
        currentPage = page;
        startingPageIndex = currentPage;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    private int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (null != mLayoutManager) {
            int lastVisibleItemPosition = 0;
            totalItemCount = mLayoutManager.getItemCount();
            if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
            } else if (mLayoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            } else if (mLayoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            }
            if (loading && (totalItemCount > previousTotalItemCount)) {
                loading = false;
                previousTotalItemCount = totalItemCount;
            }
            if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount && recyclerView.getAdapter().getItemCount() > visibleThreshold) {
                currentPage++;
                onLoadMore(currentPage);
                loading = true;
            }
        } else {
            throw new IllegalArgumentException("The layoutManager must be one of LinearLayoutManager, GridLayoutManager or StaggeredGridLayoutManager");
        }
    }

    public void resetState() {
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    public abstract void onLoadMore(int currentPage);
}

