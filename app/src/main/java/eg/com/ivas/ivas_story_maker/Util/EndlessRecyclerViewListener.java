package eg.com.ivas.ivas_story_maker.Util;

import android.widget.AbsListView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessRecyclerViewListener extends RecyclerView.OnScrollListener {


   private GridLayoutManager gridLayoutManager;
   private boolean isScrolling=false;

    public EndlessRecyclerViewListener(GridLayoutManager gridLayoutManager) {
        this.gridLayoutManager = gridLayoutManager;
    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){

            isScrolling=true;

        }


    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int childCount=gridLayoutManager.getChildCount();
        int pastVisibleItem;
        int totalItem=gridLayoutManager.getItemCount();

        pastVisibleItem =gridLayoutManager.findFirstVisibleItemPosition();

        if (isScrolling&&((childCount+pastVisibleItem)>=totalItem)){

            isScrolling=false;
            onLoadMore(recyclerView);

        }
    }



    public abstract void onLoadMore(RecyclerView view);

}
