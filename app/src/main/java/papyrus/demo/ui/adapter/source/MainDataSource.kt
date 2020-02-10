package papyrus.demo.ui.adapter.source

import android.Manifest
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import papyrus.adapter.DataItem
import papyrus.adapter.DataSource
import papyrus.adapter.PapyrusViewHolder
import papyrus.alerts.DialogBuilder
import papyrus.core.Papyrus
import papyrus.demo.App
import papyrus.demo.R
import papyrus.demo.module.AlphaModule
import papyrus.demo.module.ButtonModule
import papyrus.demo.ui.TestDialog
import papyrus.demo.ui.activity.QueryActivity
import papyrus.demo.ui.adapter.DataTypes
import papyrus.demo.ui.adapter.holder.ButtonViewHolder
import papyrus.demo.ui.adapter.holder.LabelViewHolder
import papyrus.demo.ui.adapter.item.LabelItem
import papyrus.util.PapyrusExecutor

class MainDataSource : DataSource<DataItem>(
        ButtonModule(3, "Test Result") {
            Papyrus.navigate()
                    .onResult { resultCode, data ->
                        Log.wtf("PAPYRUS", "Result: $resultCode")
                    }
                    .start(QueryActivity::class.java)
        },
        ButtonModule(5, "Test Permissions") {
            Papyrus.requestPermissions(Manifest.permission.CAMERA) { granted, denied ->
                granted?.forEach { Log.wtf("PAPYRUS", "Permission $it GRANTED") }
                denied?.forEach { Log.wtf("PAPYRUS", "Permission $it DENIED") }
            }
        },
        ButtonModule(6, "Alert Me!") {
            DialogBuilder()
                    .configuration {
                        putString("title", "Test Dialog")
                        putString("message", "This is a test Dialog, and this is the message")
                        putString("positive", "Ok")
                        putString("negative", "Nah")
                    }
                    .viewBinder(TestDialog::class)
                    .callback(R.id.button_positive) {
                        Toast.makeText(App.get(), "Positive Response", Toast.LENGTH_SHORT).show()
                    }
                    .callback(R.id.button_negative) {
                        Toast.makeText(App.get(), "Negative Response", Toast.LENGTH_SHORT).show()
                    }
                    .show()
        },
        AlphaModule(2, 5)
) {
    var next = 0

    override fun createViewHolder(parent: ViewGroup, viewType: Int): PapyrusViewHolder<out DataItem> {
        return when (DataTypes.values().getOrNull(viewType)) {
            DataTypes.BUTTON -> ButtonViewHolder(parent)
            else -> LabelViewHolder(parent)
        }
    }

    override fun createDefaultDataItem(index: Int, data: Any): DataItem {
        return LabelItem(index, data.toString())
    }

    override fun makeNextRequest(onNewPage: (ArrayList<out Any>) -> Unit) {
        PapyrusExecutor.background {
            Thread.sleep(1000)
            val page = IntRange(0, 12).fold(ArrayList<Int>()) { acc, item ->
                acc.add(next++)
                acc
            }
            PapyrusExecutor.ui {
                onNewPage(page)
            }
        }

    }
}