package papyrus.toolbar.tabs.iface

import papyrus.pager.PageItem
import papyrus.pager.iface.IPageCreator
import papyrus.toolbar.tabs.Tab
import papyrus.toolbar.tabs.TabStrip

interface ITabbedPageCreator : IPageCreator {
    fun createTab(parent: TabStrip?, item: PageItem?): Tab
}