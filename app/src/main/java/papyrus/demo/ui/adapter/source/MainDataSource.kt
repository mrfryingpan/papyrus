package papyrus.demo.ui.adapter.source

import android.Manifest
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import papyrus.adapter.DataItem
import papyrus.adapter.EmptyViewHolder
import papyrus.adapter.PapyrusViewHolder
import papyrus.adapter.differ.DifferDataSource
import papyrus.alerts.DialogBuilder
import papyrus.core.Papyrus
import papyrus.demo.R
import papyrus.demo.module.AlphaModule
import papyrus.demo.module.ButtonModule
import papyrus.demo.module.HeaderModule
import papyrus.demo.module.PickerModule
import papyrus.demo.ui.activity.QueryActivity
import papyrus.demo.ui.adapter.DataTypes
import papyrus.demo.ui.adapter.holder.*
import papyrus.demo.ui.adapter.item.LabelItem
import papyrus.demo.ui.dialog.TestDialog
import papyrus.spanner.OnClickSpan
import papyrus.spanner.Spanner
import papyrus.util.PapyrusExecutor

class MainDataSource(owner: LifecycleOwner) : DifferDataSource(
        HeaderModule(),
        ButtonModule(3, "Test Result") {
            Papyrus.navigate()
                    .onResult { resultCode, _ ->
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
                        putParcelable("message",
                                Spanner()
                                        .appendSpan("Say Hello")
                                        .appendSpan("\n")
                                        .appendSpan("Red", ForegroundColorSpan(Color.RED))
                                        .appendSpan("\n")
                                        .appendSpan("click me", OnClickSpan {
                                            Toast.makeText(Papyrus.app, "Clicked", Toast.LENGTH_SHORT).show()
                                        })
                        )
                        putString("positive", "Ok")
                        putString("negative", "Nah")
                    }
                    .viewBinder(TestDialog::class)
                    .callback(R.id.button_positive) {
                        Toast.makeText(Papyrus.app, "Positive Response", Toast.LENGTH_SHORT).show()
                    }
                    .callback(R.id.button_negative) {
                        Toast.makeText(Papyrus.app, "Negative Response", Toast.LENGTH_SHORT).show()
                    }
                    .show()
        },
        AlphaModule(2, 5),
        PickerModule(9, owner)
) {
    var next = 0

    init {
        paginationThreshold = 3
    }

    override fun createViewHolder(parent: ViewGroup, viewType: Int): PapyrusViewHolder<out DataItem<*>> {
        return when (DataTypes.values().getOrNull(viewType)) {
            DataTypes.HEADER -> HeaderViewHolder(parent)
            DataTypes.BUTTON -> ButtonViewHolder(parent)
            DataTypes.PICKER -> PickerViewHolder(parent)
            DataTypes.ALPHA -> AlphaViewHolder(parent)
            DataTypes.LABEL -> LabelViewHolder(parent)
            else -> EmptyViewHolder(parent)
        }
    }

    override fun createDefaultDataItem(index: Int, data: Any): DataItem<out Any> {
        return LabelItem(index, data.toString())
    }

    override fun makeNextRequest() {
        PapyrusExecutor.background(1000) {
            val page = IntRange(0, 120).fold(ArrayList<Int>()) { acc, _ ->
                acc.apply {
                    add(next++)
                }
            }
            update(page)
        }

    }
}