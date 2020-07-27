package com.sibdever.algo_android;

import android.view.View;

@FunctionalInterface
public interface RecyclerViewClickListener {
   void recyclerViewListClicked(View v, int position);
}