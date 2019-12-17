package papyrus.pager

import androidx.fragment.app.Fragment

abstract class PageItem(val title: String) {
    abstract fun newInstance(): Fragment
}