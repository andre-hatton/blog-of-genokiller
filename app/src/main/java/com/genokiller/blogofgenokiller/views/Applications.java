package com.genokiller.blogofgenokiller.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.genokiller.blogofgenokiller.controllers.Application_Controller;
import com.genokiller.blogofgenokiller.controllers.Articles_Controller;
import com.genokiller.blogofgenokiller.controllers.R;
import com.genokiller.blogofgenokiller.controllers.Search_Controller;

/**
 * @since 10/10/20013
 * @author Hatton André Vue générale de l'application
 */
public class Applications extends ListView implements OnScrollListener
{
	/**
	 * Position x lors de l'appui sur l'ecran
	 */
	private float				firstX;
	/**
	 * Taille de l'ecran
	 */
	private int		width;
	/**
	 * Controller de la vue
	 */
	private Application_Controller	main;
	/**
	 * Page en cours
	 */
	private int					page;
	/**
	 * Données de la recherche
	 */
	private String				search;
	/**
	 * @deprecated Nombre de page dans l'application web
	 */
	private int					max_page;
	/**
	 * Nombre d'item dans la liste
	 */
	private int					countItem	= 0;

	public Applications(Context context)
	{
		super(context);
		init(context);
	}

	public Applications(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public Applications(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}

	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			firstX = event.getX();
		}
		if (event.getAction() == MotionEvent.ACTION_UP)
		{
			float lastX = event.getX();
			if (Math.abs(firstX - lastX) > width / 2)
			{
				if (firstX > lastX)
				{
					if (getMax_page() < page)
					{
						Intent intent = null;
						if (getSearch().length() > 0)
							intent = new Intent(main, Search_Controller.class);
						else
							intent = new Intent(main, Articles_Controller.class);
						Bundle bundle = new Bundle();
						bundle.putString("search", getSearch());
						bundle.putInt("page", page + 1);
						bundle.putInt("max_page", getMax_page());
						intent.putExtras(bundle);
						main.startActivity(intent);
						main.finish();
						main.overridePendingTransition(R.xml.translate_right_center, R.xml.translate_center_left);
					}
				}
				else
				{
					if (page > 1)
					{
						Intent intent = null;
						if (getSearch().length() > 0)
							intent = new Intent(main, Search_Controller.class);
						else
							intent = new Intent(main, Articles_Controller.class);
						Bundle bundle = new Bundle();
						bundle.putInt("page", page - 1);
						bundle.putString("search", getSearch());
						bundle.putInt("max_page", getMax_page());
						intent.putExtras(bundle);
						main.startActivity(intent);
						main.finish();
						main.overridePendingTransition(R.xml.translate_left_center, R.xml.translate_center_right);
					}
				}
			}
			firstX = 0f;
		}
		return super.onTouchEvent(event);
	}

	public void setWidthScreen(int width_screen)
	{
		this.width = width_screen;
	}

	public void setActivity(Application_Controller application_Controller)
	{
		this.main = application_Controller;
	}

	/**
	 * @return the main
	 */
	public Application_Controller getMain()
	{
		return main;
	}

	public int getWidthScreen()
	{
		return this.width;
	}

	public void setPage(int page)
	{
		this.page = page;

	}

	/**
	 * @return the search
	 */
	public String getSearch()
	{
		return search;
	}

	/**
	 * @param search the search to set
	 */
	public void setSearch(String search)
	{
		this.search = search;
	}

	/**
	 * @return the max_page
	 */
	public int getMax_page()
	{
		return max_page;
	}

	/**
	 * @param max_page the max_page to set
	 */
	public void setMax_page(int max_page)
	{
		this.max_page = max_page;
	}


	/**
	 * Ecoute lors du deplacement de la scroll bar
	 */
	private OnScrollListener	mOnScrollListener;
	private LayoutInflater		mInflater;

	// Vue du pied de page de la liste
	private RelativeLayout		mFooterView;
	// bare de progretion sur chargement
	private ProgressBar			mProgressBarLoadMore;

	// ecoute sur la fin de liste
	private OnLoadMoreListener	mOnLoadMoreListener;
	// verifie si la liste est chargé
	private boolean				mIsLoadingMore	= false;
	/**
	 * Recupert l'etat courrant de la scrollBar
	 */
	private int					mCurrentScrollState;

	/**
	 * Initialise le chargement de la liste
	 * 
	 * @param context
	 *        Contexte actuel
	 */
	private void init(Context context)
	{

		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// footer
		mFooterView = (RelativeLayout) mInflater.inflate(R.layout.load_more_footer, this, false);
		// bar de prograssion
		mProgressBarLoadMore = (ProgressBar) mFooterView.findViewById(R.id.load_more_progressBar);

		// ajout du pied de page
		addFooterView(mFooterView);

		super.setOnScrollListener(this);
	}

	public RelativeLayout getFooter()
	{
		return mFooterView;
	}


	/**
	 * Set the listener that will receive notifications every time the list
	 * scrolls.
	 * 
	 * @param l
	 *        The scroll listener.
	 */
	@Override
	public void setOnScrollListener(AbsListView.OnScrollListener l)
	{
		mOnScrollListener = l;
	}

	/**
	 * Register a callback to be invoked when this list reaches the end (last
	 * item be visible)
	 * 
	 * @param onLoadMoreListener
	 *        The callback to run.
	 */
	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener)
	{
		mOnLoadMoreListener = onLoadMoreListener;
	}

	/**
	 * Action sur le deplacement dans la liste
	 */
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		if (totalItemCount != countItem)
		{
			countItem = totalItemCount;
			mIsLoadingMore = false;
		}
		if (mOnScrollListener != null)
		{
			mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}

		if (mOnLoadMoreListener != null)
		{

			if (visibleItemCount == totalItemCount)
			{
				mProgressBarLoadMore.setVisibility(View.GONE);
				// mLabLoadMore.setVisibility(View.GONE);
				return;
			}
			boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

			if (!mIsLoadingMore && loadMore && mCurrentScrollState != SCROLL_STATE_IDLE)
			{
				mProgressBarLoadMore.setVisibility(View.VISIBLE);
				// mLabLoadMore.setVisibility(View.VISIBLE);
				mIsLoadingMore = true;
				onLoadMore();

			}

		}

	}

	/**
	 * Changement d'etat de la scoll bar
	 */
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		mCurrentScrollState = scrollState;

		if (mOnScrollListener != null)
		{
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}

	}

	public void onLoadMore()
	{
		if (mOnLoadMoreListener != null)
		{
			mOnLoadMoreListener.onLoadMore();
			mOnLoadMoreListener.onLoadMoreComplete();
		}
	}

	/**
	 * Notify the loading more operation has finished
	 */
	public void onLoadMoreComplete()
	{
		mIsLoadingMore = false;
		mProgressBarLoadMore.setVisibility(View.GONE);
	}

	/**
	 * Interface definition for a callback to be invoked when list reaches the
	 * last item (the user load more items in the list)
	 */
	public interface OnLoadMoreListener
	{
		/**
		 * Called when the list reaches the last item (the last item is visible
		 * to the user)
		 */
		public void onLoadMore();

		public void onLoadMoreComplete();
	}

}
