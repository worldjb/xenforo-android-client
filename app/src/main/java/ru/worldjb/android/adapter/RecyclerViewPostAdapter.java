package ru.worldjb.android.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.worldjb.android.R;
import ru.worldjb.android.api.API;
import ru.worldjb.android.model.Post;
import ru.worldjb.android.model.User;
import ru.worldjb.android.model.UserGroup;

/**
 * Adapter for Post
 * Created by alex_xpert on 30.03.2015.
 */
public class RecyclerViewPostAdapter extends RecyclerView.Adapter<RecyclerViewPostAdapter.ViewHolder> {
	private Context context;
	private ArrayList<Post> posts;

	public RecyclerViewPostAdapter(ArrayList<Post> _posts) {
		this.posts = _posts;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post, viewGroup, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int i) {
		final Post post = posts.get(i);

		User user = API.getUser(post.poster_user_id);

		final Vector<Byte> ava = new Vector();
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					URLConnection con = new URL(post.links.poster_avatar).openConnection();
					con.connect();
					InputStream in = con.getInputStream();
					int b;
					while((b = in.read()) != -1) ava.add((byte) b);
					in.close();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		t.start();
		while(t.isAlive()) {
			try {
				java.lang.Thread.sleep(10);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		byte[] userpic = new byte[ava.size()];
		for(int j=0;j<ava.size();j++) userpic[j] = ava.get(j);

		viewHolder.avatar.setImageBitmap(BitmapFactory.decodeByteArray(userpic, 0, userpic.length));

		Date date = new Date(post.post_create_date * 1000L);
		Date now = new Date(System.currentTimeMillis());
		String time = "";
		if(date.getYear() == now.getYear() && date.getMonth() == now.getMonth()) {
			switch(now.getDate() - date.getDate()) {
				case 2:
					time += "Позавчера";
					break;
				case 1:
					time += "Вчера";
					break;
				case 0:
					time += "Сегодня";
					break;
			}
		}
		if(time.equals("")) {
			String day = date.getDate() > 9 ? String.valueOf(date.getDate()) : "0" + String.valueOf(date.getDate());
			String month = date.getMonth() + 1 > 9 ? String.valueOf(date.getMonth() + 1) : "0" + String.valueOf(date.getMonth() + 1);
			String year = String.valueOf(date.getYear() + 1900);
			time += day + "." + month + "." + year;
		}
		String hour = date.getHours() > 9 ? String.valueOf(date.getHours()) : "0" + String.valueOf(date.getHours());
		String minute = date.getMinutes() > 9 ? String.valueOf(date.getMinutes()) : "0" + String.valueOf(date.getMinutes());
		time += ", " + hour + ":" + minute;
		viewHolder.time.setText(time);

		viewHolder.username.setText(post.poster_username);

		String group = "";
		if(user.user_groups != null) {
			for (UserGroup ug : user.user_groups) {
				if (ug.is_primary_group) {
					group = ug.user_group_title;
				}
			}
		} else {
			group = "Гости не видят группу";
		}
		viewHolder.usergroup.setText(group);

		viewHolder.post.setText(post.post_body_plain_text);
	}

	/**
	 * How many items are in the data set represented by this Adapter.
	 *
	 * @return Count of items.
	 */
	@Override
	public int getItemCount() {
		return posts.size();
	}

	/**
	 * Get the row id associated with the specified position in the list.
	 *
	 * @param position The position of the item within the adapter's data set whose row id we want.
	 * @return The id of the item at the specified position.
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}


	class ViewHolder extends RecyclerView.ViewHolder {
		private CircleImageView avatar;
		private TextView time;
		private TextView username;
		private TextView usergroup;
		private TextView post;

		public ViewHolder(View itemView) {
			super(itemView);
			avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
			time = (TextView) itemView.findViewById(R.id.time);
			username = (TextView) itemView.findViewById(R.id.username);
			usergroup = (TextView) itemView.findViewById(R.id.usergroup);
			post = (TextView) itemView.findViewById(R.id.post);
		}
	}

}
