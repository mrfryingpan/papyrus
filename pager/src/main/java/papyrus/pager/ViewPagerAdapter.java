package papyrus.pager;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

import papyrus.util.PapyrusUtil;

public abstract class ViewPagerAdapter<VH extends ViewPagerAdapter.ViewHolder> extends PagerAdapter {

    private SparseArray<LinkedList<VH>> viewPool = new SparseArray<>();

    private VH retreiveViewHolderOfTypeFromPool(int viewType) {
        if (PapyrusUtil.isEmpty(viewPool.get(viewType))) {
            return null;
        } else {
            return viewPool.get(viewType).removeFirst();
        }
    }

    private void stashViewHolderForReuse(VH holder) {
        if (viewPool.get(holder.viewType) == null) {
            viewPool.put(holder.viewType, new LinkedList<VH>());
        }
        viewPool.get(holder.viewType).push(holder);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int viewType = getItemViewType(position);
        VH holder = retreiveViewHolderOfTypeFromPool(viewType);
        if (holder == null) {
            holder = (VH) onCreateViewHolder(container, viewType);
            holder.viewType = viewType;
        }

        holder.adapterPosition = position;
        onBindViewHolder(holder, position);
        container.addView(holder.view);
        return holder;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        onViewRecycled((VH) object);
        container.removeView(((VH) object).view);
        stashViewHolderForReuse((VH) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((VH) object).view == view;
    }

    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(VH holder, int position);

    public void onViewRecycled(VH holder) {
    }

    public int getItemViewType(int position) {
        return 1;
    }

    public static abstract class ViewHolder {
        View view;
        int viewType = 1;
        int adapterPosition;

        public ViewHolder(View view) {
            this.view = view;
        }

        public int getAdapterPosition() {
            return adapterPosition;
        }
    }
}

