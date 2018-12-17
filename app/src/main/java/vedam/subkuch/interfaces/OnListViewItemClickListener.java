package vedam.subkuch.interfaces;

import android.view.View;

import vedam.subkuch.utils.ListItemClickAction;


/**
 * Created by nansari on 6/24/2016.
 * For project RelayServices
 */
public interface OnListViewItemClickListener {

    /**
     * Implement this listener in your fragment/Activity to get the callbacks for items clicked in recycler view
     *
     * @param <E>      Generic class
     * @param item     The model class used to populate the recycler view
     * @param position position of the item clicked
     * @param view     clicked view
     * @param action   Click action
     */
    <E> void onItemClick(E item, int position, View view, ListItemClickAction action);
}
