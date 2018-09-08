package com.jiaoshizige.teacherexam.base;

import android.content.Context;

import java.util.List;

/**RecyclerViewAdapter
 * @param <T>
 */
public abstract class CommRecyclerViewAdapter<T> extends CommRecyclerViewBaseAdapter<T> {


    public CommRecyclerViewAdapter(Context context,int layoutId,List<T> datas ) {
        super(context,layoutId,datas);

    }

    public T getItem(int position){
        if(isPositionLegal(position)){
            return mDatas.get(position);
        }
        return null;
    }


    private void setData(List<T> list)
    {
        mDatas.clear();
        if (list != null && list.size() > 0)
        {
            mDatas.addAll(list);
        }
    }

    public void updateData(List<T> list)
    {
        setData(list);
        notifyDataSetChanged();
    }

    public void clearData()
    {
        updateData(null);
    }

    public void appendData(T model)
    {
        if (model != null)
        {
            mDatas.add(model);
            notifyItemInserted(mDatas.size() - 1);
        }
    }

    public void appendData(List<T> list)
    {
        if (list != null && !list.isEmpty())
        {
            int positionStart = mDatas.size();
            int itemCount = list.size();

            mDatas.addAll(list);
            notifyItemRangeInserted(positionStart, itemCount);
        }
    }

    public void removeData(T model)
    {
        if (model != null)
        {
            int position = mDatas.indexOf(model);
            removeData(position);
        }
    }

    public T removeData(int position)
    {
        T model = null;
        if (isPositionLegal(position))
        {
            model = mDatas.remove(position);
            notifyItemRemoved(position);
        }
        return model;
    }

    public void insertData(int position, T model)
    {
        if (model != null && isPositionLegal(position))
        {
            mDatas.add(position, model);
            notifyItemInserted(position);
        }
    }

    public void insertData(int position, List<T> list)
    {
        if (list != null && !list.isEmpty() && isPositionLegal(position))
        {
            int positionStart = position;
            int itemCount = list.size();

            mDatas.addAll(position, list);
            notifyItemRangeInserted(positionStart, itemCount);
        }
    }

    public void updateData(int position, T model)
    {
        if (model != null && isPositionLegal(position))
        {
            mDatas.set(position, model);
            notifyItemChanged(position);
        }
    }

    public void updateData(int position)
    {
        if (isPositionLegal(position))
        {
            notifyItemChanged(position);
        }
    }

    public boolean isPositionLegal(int position)
    {
        if (position >= 0 && position < mDatas.size())
        {
            return true;
        }
        return false;
    }
}
