package ru.worldjb.android.api;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStreamReader;
import java.util.ArrayList;

import ru.worldjb.android.ForumListFragment;
import ru.worldjb.android.PostFragment;
import ru.worldjb.android.ThreadFragment;
import ru.worldjb.android.adapter.CategoryAdapter;
import ru.worldjb.android.adapter.ForumAdapter;
import ru.worldjb.android.adapter.RecyclerViewPostAdapter;
import ru.worldjb.android.adapter.ThreadAdapter;
import ru.worldjb.android.model.Category;
import ru.worldjb.android.model.Forum;
import ru.worldjb.android.model.Post;
import ru.worldjb.android.model.Thread;
import ru.worldjb.android.model.User;

/**
 * API
 * Created by alex_xpert on 25.02.2015.
 */
public class API {
    private static final String API_URL = "http://worldjb.ru/api/index.php?";
    private static final HttpClient client = new DefaultHttpClient();

    private static final Handler handler = new Handler();

    private static ArrayList<Category> clist = null;

    public static void getCategories(final ListView categories) {
        java.lang.Thread t = new java.lang.Thread(new Runnable() {
           public void run() {
               try {
                   HttpGet get = new HttpGet(API_URL + "/categories");
                   HttpResponse response = client.execute(get);
                   JsonArray result = new JsonParser().parse(new InputStreamReader(response.getEntity().getContent()))
                           .getAsJsonObject().get("categories").getAsJsonArray();
                   Gson gson = new Gson();
                   clist = new ArrayList<>();
                   for(int i=0;i<result.size();i++) {
                       int id = result.get(i).getAsJsonObject().get("category_id").getAsInt();
                       if (id == 1 || id == 107 || id == 109 || id == 110 || id == 111 || id == 112 || id == 148 || id == 108 || id == 12) {
                           clist.add(gson.fromJson(result.get(i), Category.class));
                       }
                   }
                   handler.post(new Runnable() {
                       public void run() {
                           categories.setAdapter(new CategoryAdapter(clist));
                           ((CategoryAdapter) categories.getAdapter()).notifyDataSetChanged();
                       }
                   });
               } catch(Exception ex) {
                   ex.printStackTrace();
                   handler.post(new Runnable() {
                       public void run() {
                           Toast toast = Toast.makeText(categories.getContext(), "Отсутствует подключение к Интернет", Toast.LENGTH_SHORT);
                           toast.show();
                       }
                   });
               }
           }
        });
        t.setDaemon(true);
        t.start();
    }

    public static void getForums(final ForumListFragment forum_list, final int parent_id) {
        java.lang.Thread t = new java.lang.Thread(new Runnable() {
            public void run() {
                try {
                    HttpGet get = new HttpGet(API_URL + "/forums&parent_forum_id=" + parent_id);
                    HttpResponse response = client.execute(get);
                    JsonObject answer = new JsonParser().parse(new InputStreamReader(response.getEntity().getContent()))
                            .getAsJsonObject();
                    //if(answer.get("forums_total").getAsInt() == 0) {
                        //handler.post(new Runnable() {
                            //public void run() {
                                //Toast toast = Toast.makeText(forum_list.getActivity(), "В этом разделе нет подразделов", Toast.LENGTH_SHORT);
                                //toast.show();
                            //}
                        //});
                        //return;
                    //}
                    JsonArray result = answer.get("forums").getAsJsonArray();
                    Gson gson = new Gson();
                    final ArrayList<Forum> flist = new ArrayList<>();
                    for(int i=0;i<result.size();i++) {
                        flist.add(gson.fromJson(result.get(i), Forum.class));
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            forum_list.setListAdapter(new ForumAdapter(flist));
                            ((ForumAdapter) forum_list.getListAdapter()).notifyDataSetChanged();
                        }
                    });
                } catch(Exception ex) {
                    ex.printStackTrace();
                    boolean post = handler.post(new Runnable() {
                        public void run() {
                            Toast toast = Toast.makeText(forum_list.getActivity(), "Отсутствует подключение к Интернет", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public static void getThreads(final ThreadFragment thread_list, final int parent_id, final int page) {
        java.lang.Thread t = new java.lang.Thread(new Runnable() {
            public void run() {
                try {
                    HttpGet get = new HttpGet(API_URL + "/threads&forum_id=" + parent_id + "&order=thread_update_date_reverse");
                    HttpResponse response = client.execute(get);
                    final JsonObject answer = new JsonParser().parse(new InputStreamReader(response.getEntity().getContent()))
                            .getAsJsonObject();
                    handler.post(new Runnable() {
                        public void run() {
                            Toast toast = Toast.makeText(thread_list.getActivity(), "Загружено " + answer.get("threads_total").getAsString() + " тем", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                    JsonArray result = answer.get("threads").getAsJsonArray();
                    Gson gson = new Gson();
                    final ArrayList<Thread> tlist = new ArrayList<>();
                    for(int i=0;i<result.size();i++) {
                        tlist.add(gson.fromJson(result.get(i), Thread.class));
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            thread_list.setListAdapter(new ThreadAdapter(tlist));
                            ((ThreadAdapter) thread_list.getListAdapter()).notifyDataSetChanged();
                        }
                    });
                } catch(Exception ex) {
                    ex.printStackTrace();
                    boolean post = handler.post(new Runnable() {
                        public void run() {
                            Toast toast = Toast.makeText(thread_list.getActivity(), "Отсутствует подключение к Интернет", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

	public static void getPosts(final PostFragment post_list, final int parent_id, final int page) {
		java.lang.Thread t = new java.lang.Thread(new Runnable() {
			public void run() {
				try {
					HttpGet get = new HttpGet(API_URL + "/posts&thread_id=" + parent_id + "&page=" + page);
					HttpResponse response = client.execute(get);
					final JsonObject answer = new JsonParser().parse(new InputStreamReader(response.getEntity().getContent()))
							.getAsJsonObject();
					handler.post(new Runnable() {
						public void run() {
							Toast toast = Toast.makeText(post_list.getActivity(), "Загружено " + answer.get("posts_total").getAsString() + " постов", Toast.LENGTH_SHORT);
							toast.show();
						}
					});
					JsonArray result = answer.get("posts").getAsJsonArray();
					Gson gson = new Gson();
					final ArrayList<Post> plist = new ArrayList<>();
					for(int i=0;i<result.size();i++) {
						plist.add(gson.fromJson(result.get(i), Post.class));
					}
					handler.post(new Runnable() {
						public void run() {
							post_list.setAdapter(new RecyclerViewPostAdapter(plist));
							((RecyclerViewPostAdapter) post_list.getAdapter()).notifyDataSetChanged();
						}
					});
				} catch(Exception ex) {
					ex.printStackTrace();
					boolean post = handler.post(new Runnable() {
						public void run() {
							Toast toast = Toast.makeText(post_list.getActivity(), "Отсутствует подключение к Интернет", Toast.LENGTH_SHORT);
							toast.show();
						}
					});
				}
			}
		});
		t.setDaemon(true);
		t.start();
	}

	public static User getUser(final int userid) {
		final User[] user = new User[1];
		java.lang.Thread t = new java.lang.Thread(new Runnable() {
			public void run() {
				try {
					HttpGet get = new HttpGet(API_URL + "/users/" + userid);
					HttpResponse response = client.execute(get);
					final JsonObject answer = new JsonParser().parse(new InputStreamReader(response.getEntity().getContent()))
							.getAsJsonObject();
					JsonObject juser = answer.get("user").getAsJsonObject();
					Gson gson = new Gson();
					user[0] = gson.fromJson(juser, User.class);
				} catch (Exception ex) {
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
		return user[0];
	}
}
