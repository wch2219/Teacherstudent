package com.jiaoshizige.teacherexam.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**RecyclerViewAdapter
 * @param <T>
 */
public abstract class CommRecyclerViewBaseAdapter<T> extends RecyclerView.Adapter<ViewHolderZhy> {

    protected Context mContext;
    protected List<T> mDatas = new ArrayList<>();
    protected CommRecyclerViewBaseAdapter.OnItemClickListener mOnItemClickListener;
    private int mLayoutId;

    public CommRecyclerViewBaseAdapter(Context context,int layoutId,List<T> datas) {
        mLayoutId = layoutId;
        mContext = context;
        if (datas != null) {
            mDatas.addAll(datas);
        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        return 0;
//    }


    @Override
    public ViewHolderZhy onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderZhy holder = ViewHolderZhy.createViewHolder(mContext, parent, mLayoutId);
        onViewHolderCreated(holder,holder.getConvertView());
        setListener(parent, holder, viewType);
        return holder;
    }

    public void onViewHolderCreated(ViewHolderZhy holder, View itemView){

    }

    protected void setListener(final ViewGroup parent, final ViewHolderZhy viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, viewHolder , position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
                }
                return false;
            }
        });
    }


    protected abstract void convert(ViewHolderZhy holder, T item,int position);

    protected boolean isEnabled(int viewType) {
        return true;
    }


    @Override
    public void onBindViewHolder(ViewHolderZhy holder, int position) {
        convert(holder, mDatas.get(position),position);
    }

    @Override
    public int getItemCount() {
        int itemCount = mDatas.size();
        return itemCount;
    }


    public List<T> getDatas() {
        return mDatas;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ViewHolderZhy holder, int position);

        boolean onItemLongClick(View view, ViewHolderZhy holder, int position);
    }

    public void setOnItemClickListener(CommRecyclerViewBaseAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
