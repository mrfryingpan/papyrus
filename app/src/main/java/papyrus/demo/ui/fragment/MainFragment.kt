package papyrus.demo.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import papyrus.adapter.DataSourceAdapter

import papyrus.demo.R
import papyrus.demo.ui.adapter.source.MainDataSource
import papyrus.ui.fragment.PapyrusFragment

class MainFragment : PapyrusFragment() {
    private val dataSource = MainDataSource().also {
        it.enablePagination(3)
        it.loadNext()
    }

    override fun getLayoutRes() = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.recycler)?.let { recycler ->
            recycler.adapter = DataSourceAdapter(dataSource)
            recycler.layoutManager = LinearLayoutManager(view.context)
        }
    }
}
