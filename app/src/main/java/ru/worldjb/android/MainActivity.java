package ru.worldjb.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v7.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ru.worldjb.android.adapter.CategoryAdapter;
import ru.worldjb.android.api.API;
import ru.worldjb.android.model.Category;


public class MainActivity extends ActionBarActivity {

	/**
	 * Remember the position of the selected item.
	 */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	/**
	 * Per the design guidelines, you should show the drawer on launch until the user manually
	 * expands it. This shared preference tracks this.
	 */
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	private android.support.v4.app.ActionBarDrawerToggle mDrawerToggle;
	private ActionBarDrawerToggle mNewDrawerToggle;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerListView;
	private View mFragmentContainerView;

	private int mCurrentSelectedPosition = 0;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;

	private Toolbar toolbar;

	private LinearLayout container;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
	private RelativeLayout custombar;
	private DrawerLayout drawer;

	private PopupMenu pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ECLAIR_MR1) {
		    setContentView(R.layout.activity_main);
		    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	    } else {
		    setContentView(R.layout.start);
		    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    mDrawerLayout = (DrawerLayout) inflater.inflate(R.layout.activity_main, null); // "null" is important.
		    // HACK: "steal" the first child of decor view
		    ViewGroup decor = (ViewGroup) getWindow().getDecorView();
		    View child = decor.getChildAt(0);
		    decor.removeView(child);
		    container = (LinearLayout) mDrawerLayout.findViewById(R.id.drawer_content); // This is the container we defined just now.
		    container.addView(child, 0);
		    mDrawerLayout.findViewById(R.id.drawer).setPadding(0, getStatusBarHeight(), 0, 0);

		    // Make the drawer replace the first child
		    decor.addView(mDrawerLayout);
	    }

	    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
		    getWindow().setStatusBarColor(Color.parseColor("#ff48678b"));
	    }

	    mDrawerListView = (ListView) mDrawerLayout.findViewById(R.id.drawer);
	    mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			    selectItem(position);
		    }
	    });
	    mDrawerListView.setAdapter(new ArrayAdapter<String>(
			    this,
			    android.R.layout.simple_list_item_1,
			    android.R.id.text1,
			    new String[]{
					    getString(R.string.title_section1),
					    getString(R.string.title_section2),
					    getString(R.string.title_section3),
			    }));
	    mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

	    mTitle = getTitle();

	    // Set up the drawer.
	    mFragmentContainerView = mDrawerLayout.findViewById(R.id.drawer);

	    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ECLAIR_MR1) {
		    toolbar = (Toolbar) mDrawerLayout.findViewById(R.id.toolbar);
		    setSupportActionBar(toolbar);
		    toolbar.setTitleTextColor(getResources().getColor(android.R.color.primary_text_dark));
		    toolbar.setPopupTheme(R.style.Theme_AppCompat_Light_NoActionBar);
	    } else {
		    custombar = (RelativeLayout) getLayoutInflater().inflate(R.layout.custom_actionbar, null);
		    ImageView icon = (ImageView) custombar.findViewById(R.id.icon);
		    icon.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
				    if(!isDrawerOpen()) {
					    openDrawer();
				    }
			    }
		    });
		    ImageView menuicon = (ImageView) custombar.findViewById(R.id.menuicon);
		    TextView popup_anchor = (TextView) custombar.findViewById(R.id.popup_anchor);
		    pm = new PopupMenu(getActivity(), popup_anchor, Gravity.TOP);
		    menuicon.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
				    pm.show();
			    }
		    });

		    getSupportActionBar().setDisplayShowTitleEnabled(false);
		    getSupportActionBar().setCustomView(custombar);
		    getSupportActionBar().setDisplayShowCustomEnabled(true);
		    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff6089bb")));
	    }

	    //ActionBar actionBar = getActionBar();
	    //actionBar.setDisplayHomeAsUpEnabled(true);
	    //actionBar.setHomeButtonEnabled(true);

	    // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
	    // per the navigation drawer design guidelines.
	    if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
		    openDrawer();
	    }

	    // Defer code dependent on restoration of previous instance state.
	    mDrawerLayout.post(new Runnable() {
		    @Override
		    public void run() {
			    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ECLAIR_MR1) {
				    mNewDrawerToggle.syncState();
			    } else {
				    mDrawerToggle.syncState();
			    }
		    }
	    });

	    mDrawerLayout.setDrawerListener(createDrawerToggle());

	    // set a custom shadow that overlays the main content when the drawer opens
	    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
	    // set up the drawer's list view with items and click listener

	    mDrawerLayout.setScrimColor(Color.TRANSPARENT);

	    //mDrawerLayout.setStatusBarBackgroundColor(Color.parseColor("#42000000"));

	    // Read in the flag indicating whether or not the user has demonstrated awareness of the
	    // drawer. See PREF_USER_LEARNED_DRAWER for details.
	    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
	    mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

	    if (savedInstanceState != null) {
		    mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
		    mFromSavedInstanceState = true;
	    }

	    // Select either the default item (0) or the last selected item.
	    selectItem(mCurrentSelectedPosition);
    }

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	private DrawerLayout.DrawerListener createDrawerToggle() {
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ECLAIR_MR1) {
			// ActionBarDrawerToggle ties together the the proper interactions
			// between the navigation drawer and the action bar app icon.
			mNewDrawerToggle = new ActionBarDrawerToggle(
					this,                    /* host Activity */
					mDrawerLayout,                    /* DrawerLayout object */
					toolbar,
					R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
					R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
			) {
				@Override
				public void onDrawerClosed(View drawerView) {
					super.onDrawerClosed(drawerView);

					getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
				}

				@Override
				public void onDrawerOpened(View drawerView) {
					super.onDrawerOpened(drawerView);

					if (!mUserLearnedDrawer) {
						// The user manually opened the drawer; store this flag to prevent auto-showing
						// the navigation drawer automatically in the future.
						mUserLearnedDrawer = true;
						SharedPreferences sp = PreferenceManager
								.getDefaultSharedPreferences(getActivity());
						sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).commit();
					}

					getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
				}
			};
		} else {
			mDrawerToggle = new android.support.v4.app.ActionBarDrawerToggle(
					getActivity(),                    /* host Activity */
					mDrawerLayout,                    /* DrawerLayout object */
					R.drawable.ic_menu_white_24dp,
					R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
					R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
			) {
				@Override
				public void onDrawerClosed(View drawerView) {
					super.onDrawerClosed(drawerView);

					getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
				}

				@Override
				public void onDrawerOpened(View drawerView) {
					super.onDrawerOpened(drawerView);

					if (!mUserLearnedDrawer) {
						// The user manually opened the drawer; store this flag to prevent auto-showing
						// the navigation drawer automatically in the future.
						mUserLearnedDrawer = true;
						SharedPreferences sp = PreferenceManager
								.getDefaultSharedPreferences(getActivity());
						sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).commit();
					}

					getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
				}
			};
			mDrawerToggle.setDrawerIndicatorEnabled(false);
		}
		return mDrawerToggle == null ? mNewDrawerToggle : mDrawerToggle;
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	public void openDrawer() {
		mDrawerLayout.openDrawer(mFragmentContainerView);
	}

	public void closeDrawer() {
		mDrawerLayout.closeDrawer(mFragmentContainerView);
	}

	private void selectItem(int position) {
		mCurrentSelectedPosition = position;
		if (mDrawerListView != null) {
			mDrawerListView.setItemChecked(position, true);
		}
		if (mDrawerLayout != null) {
			closeDrawer();
		}
		onNavigationDrawerItemSelected(position);
	}

    //@Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        //FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction()
                //.replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                //.commit();
	    final ListView categories = (ListView) /*rootView.*/findViewById(R.id.categories);
	    categories.setAdapter(new CategoryAdapter(new ArrayList<Category>()));
	    categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			    //getFragmentManager().beginTransaction()
					    //.replace(R.id.container, ForumListFragment.newInstance(((Category) categories.getAdapter().getItem(position)).category_id))
					    //.commit();
		    }
	    });
	    API.getCategories(categories);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
	    if(Build.VERSION.SDK_INT == Build.VERSION_CODES.ECLAIR_MR1) {
		    TextView title = (TextView) custombar.findViewById(R.id.title);
		    title.setText(mTitle);
	    } else {
		    toolbar.setTitle(mTitle);
	    }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
	        if(pm == null) {
		        getMenuInflater().inflate(R.menu.main, menu);
	        } else {
		        pm.inflate(R.menu.main);
		        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			        @Override
			        public boolean onMenuItemClick(MenuItem menuItem) {
				        return onOptionsItemSelected(menuItem);
			        }
		        });
	        }
        }
	    // If the drawer is open, show the global app actions in the action bar. See also
	    // showGlobalContextActionBar, which controls the top-left area of the action bar.
	    if (mDrawerLayout != null && isDrawerOpen()) {
		    if (pm == null) {
			    getMenuInflater().inflate(R.menu.global, menu);
		    } else {
			    pm.inflate(R.menu.global);
			    pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
				    @Override
				    public boolean onMenuItemClick(MenuItem menuItem) {
					    return onOptionsItemSelected(menuItem);
				    }
			    });
		    }
	    }
	    restoreActionBar();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

	    if (mDrawerToggle.onOptionsItemSelected(item)) {
		    return true;
	    }

        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onBackPressed() {
		if(Build.VERSION.SDK_INT == Build.VERSION_CODES.ECLAIR_MR1 && isDrawerOpen()) {
			closeDrawer();
		} else {
			super.onBackPressed();
		}
	}

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            final ListView categories = (ListView) rootView.findViewById(R.id.categories);
            categories.setAdapter(new CategoryAdapter(new ArrayList<Category>()));
            categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, ForumListFragment.newInstance(((Category) categories.getAdapter().getItem(position)).category_id))
                            .commit();
                }
            });
            API.getCategories(categories);
            return rootView;
        }

        public void onAttach(ActionBarActivity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private ActionBarActivity getActivity() {
		return this;
	}

}
