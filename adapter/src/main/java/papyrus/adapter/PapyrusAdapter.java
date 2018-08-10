package papyrus.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import papyrus.util.PapyrusUtil;

public abstract class PapyrusAdapter<T extends ListItemModel> extends RecyclerView.Adapter<PapyrusViewHolder<T>>{


    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public void onBindViewHolder(@NonNull PapyrusViewHolder<T> holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public void onBindViewHolder(@NonNull PapyrusViewHolder<T> holder, int position, @NonNull List<Object> payloads) {
        if(PapyrusUtil.isEmpty(payloads)){
            onBindViewHolder(holder, position);
        } else {
            holder.onPayload(payloads.get(0));
        }
    }

    public abstract T getItem(int position);
}
