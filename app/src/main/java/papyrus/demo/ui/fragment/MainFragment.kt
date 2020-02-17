package papyrus.demo.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main.view.*
import papyrus.adapter.DataSourceAdapter
import papyrus.demo.R
import papyrus.demo.ui.adapter.source.MainDataSource
import papyrus.ui.fragment.PapyrusFragment

class MainFragment : PapyrusFragment() {


    private val dataSource = MainDataSource(this).also {
        it.enablePagination(3)
        it.loadNext()
    }

    override fun getLayoutRes() = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.recycler.also { recycler ->
            recycler.adapter = DataSourceAdapter(dataSource)
            recycler.layoutManager = LinearLayoutManager(view.context)
        }
        view.layoutRefresh.setOnRefreshListener {
            dataSource.refresh {
                view.layoutRefresh.isRefreshing = false
            }
        }
    }
}
