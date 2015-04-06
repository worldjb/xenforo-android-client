package ru.worldjb.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ru.worldjb.android.adapter.RecyclerViewPostAdapter;
import ru.worldjb.android.api.API;
import ru.worldjb.android.model.Post;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class PostFragment extends Fragment {
	private static int thread_id;
	private static String thread_title;
	private static int page;

	private OnFragmentInteractionListener mListener;

	private static RecyclerView recyclerView;

	public static PostFragment newInstance(int _thread_id, String _thread_title, int _page) {
		PostFragment fragment = new PostFragment();
		fragment.thread_id = _thread_id;
		fragment.thread_title = _thread_title;
		fragment.page = _page;
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public PostFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ArrayList<Post> posts = new ArrayList<Post>();
		recyclerView = (RecyclerView) inflater.inflate(R.layout.forum_list, container, false);
		RecyclerViewPostAdapter adapter = new RecyclerViewPostAdapter(posts);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
		RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setItemAnimator(itemAnimator);
		API.getPosts(this, this.thread_id, this.page);
		return recyclerView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//try {
			//mListener = (OnFragmentInteractionListener) activity;
		//} catch (ClassCastException e) {
			//throw new ClassCastException(activity.toString()
					//+ " must implement OnFragmentInteractionListener");
		//}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		//public void onFragmentInteraction(String id);
	}

	public static RecyclerViewPostAdapter getAdapter() {
		return (RecyclerViewPostAdapter) recyclerView.getAdapter();
	}

	public static void setAdapter(RecyclerViewPostAdapter adapter) {
		recyclerView.setAdapter(adapter);
	}

}
