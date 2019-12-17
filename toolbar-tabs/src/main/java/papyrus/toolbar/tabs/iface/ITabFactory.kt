package papyrus.toolbar.tabs.iface

import papyrus.toolbar.tabs.Tab
import papyrus.toolbar.tabs.TabStrip

interface ITabFactory {
    fun getCount(): Int
    fun getTabView(parent: TabStrip, position: Int): Tab?
    fun onTabClicked(position: Int)
    fun onTabsCreated(tabs: List<Tab>)
}