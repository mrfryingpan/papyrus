package papyrus.toolbar.tabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import papyrus.pager.PageAdapter
import papyrus.pager.iface.IPageCreator
import papyrus.toolbar.tabs.iface.ITabFactory
import papyrus.toolbar.tabs.iface.ITabbedPageCreator
import papyrus.toolbar.tabs.iface.ITabbedPageStateCallback
import papyrus.util.PapyrusUtil

class TabbedPageAdapter : PageAdapter, ITabFactory {
    var tabs: List<Tab>? = null

    constructor(fm: FragmentManager, creator: IPageCreator) : super(fm, creator) {}
    constructor(fm: FragmentManager, creator: IPageCreator, initialPosition: Int) : super(fm, creator, initialPosition) {}

    override fun onPageCreated(fragment: Fragment, position: Int) {
        super.onPageCreated(fragment, position)

        (fragment as? ITabbedPageStateCallback)?.let { stateCallback ->
            tabs?.getOrNull(position)?.let(stateCallback::setTab)
        }
    }

    override fun getTabView(parent: TabStrip, position: Int): Tab? {
        return (creator as? ITabbedPageCreator)?.createTab(parent, contents[position])
    }

    override fun onTabClicked(position: Int) {
        (fragments[position]?.get() as? ITabbedPageStateCallback)?.onTabClicked(position == currentPosition)
    }

    override fun onTabsCreated(tabs: List<Tab>) {
        this.tabs = tabs
        for (i in 0 until count) {
            (fragments[i]?.get() as? ITabbedPageStateCallback)?.setTab(tabs[i])
        }
    }
}