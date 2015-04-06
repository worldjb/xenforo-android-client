package ru.worldjb.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.worldjb.android.adapter.ThreadAdapter;
import ru.worldjb.android.api.API;
import ru.worldjb.android.model.Thread;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * interface.
 */
public class ThreadFragment extends ListFragment {
    private int thread_id;
	private int page;

    // TODO: Rename and change types of parameters
    public static ThreadFragment newInstance(int _thread_id, int _page) {
        ThreadFragment fragment = new ThreadFragment();
        fragment.thread_id = _thread_id;
	    fragment.page = _page;
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ThreadFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Change Adapter to display your content
        setListAdapter(new ThreadAdapter(new ArrayList<Thread>()));
        API.getThreads(this, thread_id, this.page);
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
        Thread t = (Thread) getListAdapter().getItem(position);
	    int page = t.thread_post_count / 20;
	    if(t.thread_post_count % 2 > 0) page++;
	    getFragmentManager().beginTransaction()
			    .replace(R.id.container, PostFragment.newInstance(t.thread_id, t.thread_title, page))
			    .commit();
    }

}
