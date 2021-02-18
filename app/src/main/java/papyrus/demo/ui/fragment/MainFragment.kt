package papyrus.demo.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import papyrus.adapter.DataSourceAdapter
import papyrus.adapter.StickyHeaderDecoration
import papyrus.demo.R
import papyrus.demo.ui.adapter.holder.AlphaViewHolder
import papyrus.demo.ui.adapter.source.MainDataSource
import papyrus.ui.fragment.PapyrusFragment

class MainFragment : PapyrusFragment() {

    lateinit var layout: ViewGroup
    lateinit var recycler: RecyclerView
    lateinit var refreshLayout: SwipeRefreshLayout

    private val dataSource = MainDataSource(this).also {
        it.loadNext()
    }

    override fun getLayoutRes() = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layout = view.findViewById(R.id.layout)
        recycler = view.findViewById(R.id.recycler)
        refreshLayout = view.findViewById(R.id.layoutRefresh)
        recycler.also { recycler ->
            recycler.layoutManager = LinearLayoutManager(view.context)
            AlphaViewHolder(layout).also { header ->
                layout.addView(header.itemView)
                StickyHeaderDecoration(header, dataSource).also {
                    recycler.addItemDecoration(it)
                    recycler.adapter = DataSourceAdapter(dataSource, it)
                }
            }
        }
        refreshLayout.setOnRefreshListener {
            dataSource.refresh {
                refreshLayout.isRefreshing = false
            }
        }
    }
}
