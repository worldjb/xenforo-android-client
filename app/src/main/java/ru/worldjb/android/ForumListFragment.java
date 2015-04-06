package ru.worldjb.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import java.util.ArrayList;

import ru.worldjb.android.adapter.ForumAdapter;
import ru.worldjb.android.api.API;
import ru.worldjb.android.model.Forum;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 */
public class ForumListFragment extends ListFragment {
    private int forum_id;

    // TODO: Rename and change types of parameters
    public static ForumListFragment newInstance(int _forum_id) {
        ForumListFragment fragment = new ForumListFragment();
        fragment.forum_id = _forum_id;
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ForumListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    setListAdapter(new ForumAdapter(new ArrayList<Forum>()));
	    API.getForums(this, forum_id);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
	    Forum f = (Forum) getListAdapter().getItem(position);
	    if(f.forum_thread_count == 0) {
		    getFragmentManager().beginTransaction()
				    .replace(R.id.container, ForumListFragment.newInstance(((Forum) getListAdapter().getItem(position)).forum_id))
				    .commit();
	    } else {
		    int page = f.forum_thread_count / 20;
		    if(f.forum_thread_count % 20 > 0) page++;
		    getFragmentManager().beginTransaction()
				    .replace(R.id.container, ThreadFragment.newInstance(f.forum_id, page))
				    .commit();
	    }
    }

}
