package papyrus.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PapyrusViewHolder<T> extends RecyclerView.ViewHolder {
    public PapyrusViewHolder(View itemView) {
        super(itemView);
    }

    public PapyrusViewHolder(ViewGroup parent, @LayoutRes int layoutRes) {
        this(LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false));
    }

    public void onPayload(Object object){

    }

    public void bind(T t){

    }

    public void recycle(){

    }
}
