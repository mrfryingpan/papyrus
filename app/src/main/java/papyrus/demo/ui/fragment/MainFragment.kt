package papyrus.demo.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main.view.*
import papyrus.adapter.DataSourceAdapter
import papyrus.adapter.StickyHeaderDecoration
import papyrus.demo.R
import papyrus.demo.ui.adapter.holder.AlphaViewHolder
import papyrus.demo.ui.adapter.source.MainDataSource
import papyrus.ui.fragment.PapyrusFragment

class MainFragment : PapyrusFragment() {


    private val dataSource = MainDataSource(this).also {
        it.loadNext()
    }

    override fun getLayoutRes() = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layout: ViewGroup = view.findViewById(R.id.layout)
        view.recycler.also { recycler ->
            recycler.layoutManager = LinearLayoutManager(view.context)
            AlphaViewHolder(layout).also { header ->
                layout.addView(header.itemView)
                StickyHeaderDecoration(header, dataSource).also {
                    recycler.addItemDecoration(it)
                    recycler.adapter = DataSourceAdapter(dataSource, it)
                }
            }
        }
        view.layoutRefresh.setOnRefreshListener {
            dataSource.refresh {
                view.layoutRefresh.isRefreshing = false
            }
        }
    }
}
