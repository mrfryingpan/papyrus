package papyrus.demo.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import papyrus.adapter.PapyrusRecyclerView
import papyrus.demo.R
import papyrus.demo.ui.adapter.source.MainDataSource
import papyrus.ui.fragment.PapyrusFragment

class MainFragment : PapyrusFragment() {

    lateinit var layout: ViewGroup
    lateinit var recycler: PapyrusRecyclerView
    lateinit var refreshLayout: SwipeRefreshLayout

    private val dataSource = MainDataSource(this)

    override fun getLayoutRes() = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.findViewById(R.id.recycler)
        refreshLayout = view.findViewById(R.id.layoutRefresh)
        recycler.dataSource = dataSource
        recycler.layoutManager = LinearLayoutManager(view.context)
//
//        recycler.also { recycler ->
//            AlphaViewHolder(layout).also { header ->
//                layout.addView(header.itemView)
//                StickyHeaderDecoration(header, dataSource).also {
//                    recycler.addItemDecoration(it)
//                }
//            }
//        }
        refreshLayout.setOnRefreshListener {
            refreshLayout.isRefreshing = false
            dataSource.refresh()
        }
    }
}
