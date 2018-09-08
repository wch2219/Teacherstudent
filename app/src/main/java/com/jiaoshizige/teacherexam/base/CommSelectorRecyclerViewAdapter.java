package com.jiaoshizige.teacherexam.base;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**可以保存选中状态的adapter(只针对不同的对象使用，有相同地址的对象时慎用)
 * @param <T>
 */
public abstract class CommSelectorRecyclerViewAdapter<T> extends CommRecyclerViewAdapter<T> {

    private List<T> mSelectedList = new ArrayList<>();
    private ESelectMode mESelectorMode;

    public CommSelectorRecyclerViewAdapter(Context context, int layoutId, List<T> datas,ESelectMode mode) {
        super(context, layoutId,datas);
        mESelectorMode = mode;
    }

    public ESelectMode getESelectMode() {
        return mESelectorMode;
    }

    public void setESelectMode(ESelectMode mode) {
        mESelectorMode = mode;
    }

    /**
     * 设置该位置的选中状态
     * @param position
     * @param
     */
    public void doSelected(int position)
    {
        doSelected(getItem(position));
    }

    public void doSelected(T model){
        if (null == model)
        {
            return;
        }

        switch (mESelectorMode)
        {
            case SINGLE_MUST_ONE_SELECTED:
                if(mSelectedList.contains(model)){
                    return;
                }
                mSelectedList.clear();
                mSelectedList.add(model);
                break;
            case SINGLE:
                mSelectedList.clear();
                mSelectedList.add(model);
                break;
            case MULTI_MUST_ONE_SELECTED:
                if(mSelectedList.contains(model)){
                    if(mSelectedList.size() > 1){
                        mSelectedList.remove(model);
                    }
                }  else {
                    mSelectedList.add(model);
                }
                break;
            case MULTI:
                if (mSelectedList.contains(model)) {
                    mSelectedList.remove(model);
                } else {
                    mSelectedList.add(model);
                }
                break;
            default:
                break;
        }
        notifyDataSetChanged();
    }

    public void selectAll(){
        mSelectedList.clear();
        if(mDatas != null){
            mSelectedList.addAll(mDatas);
        }
        notifyDataSetChanged();
    }
    public void selectClear(){
        mSelectedList.clear();
        notifyDataSetChanged();
    }

    /**获取所有已经选择到的数据
     * @return ：
     */
    public List<T> getSelectedList() {
        return mSelectedList;
    }

    /**单选模式下，获取被选中的那个item
     * @return :
     */
    public T getSelected() {
        if (!mSelectedList.isEmpty()) {
            return mSelectedList.get(0);
        }
        return null;
    }

    /**判断参数item是否被选中了
     * @param item ：
     * @return ：true 选中，否则 false
     */
    public boolean isSelected(T item) {
        return mSelectedList.contains(item);
    }

    /**
     * @param position :
     * @return : true 选中，否则 false
     */
    public boolean isSelected(int position) {
        return isSelected(getItem(position));
    }

    /**单选模式下，获取被选中的那个item 在数据源中的 下标
     * @return :
     */
    public int getSelectedIndexOfDate() {
        if (!mSelectedList.isEmpty()) {
            return mDatas.indexOf(mSelectedList.get(0));
        }
        return -1;
    }



    /**
     * 选择模式
     */
    public enum ESelectMode {
        /**
         * 单选
         */
        SINGLE,
        /**
         * 单选
         */
        SINGLE_MUST_ONE_SELECTED,
        /**
         * 多选
         */
        MULTI,
        /**
         * 多选
         */
        MULTI_MUST_ONE_SELECTED,
    }
}
