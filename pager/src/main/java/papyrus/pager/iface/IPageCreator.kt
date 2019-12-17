package papyrus.pager.iface

import papyrus.pager.PageItem

interface IPageCreator {
    val pages: List<PageItem>

    val defaultPage: Int

    fun onContentUpdated()
}
