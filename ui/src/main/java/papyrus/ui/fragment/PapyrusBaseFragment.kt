package papyrus.ui.fragment

import android.content.Context
import android.support.v4.app.Fragment
import android.view.View

import papyrus.ui.iface.IBaseView
import papyrus.util.WeakDelegate

abstract class PapyrusBaseFragment : Fragment(), IBaseView {

    private var callThrough: IBaseView? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callThrough = context as? IBaseView
    }

    override fun onDetach() {
        super.onDetach()
        callThrough = null
    }

    override fun showShortSnackbar(message: String) {
        callThrough?.showShortSnackbar(message)
    }

    override fun showLongSnackbar(message: String) {
        callThrough?.showLongSnackbar(message)
    }

    override fun showActionSnackbar(message: String, actionText: String, actionClick: View.OnClickListener) {
        callThrough?.showActionSnackbar(message, actionText, actionClick)
    }

    override fun dismissSnackbar() {
        callThrough?.dismissSnackbar()
    }

    override fun dismissKeyboard() {
        callThrough?.dismissKeyboard()
    }

    override fun finish() {
        callThrough?.finish()
    }

    override fun simulateBackPress() {
        callThrough?.simulateBackPress()
    }

    override fun addContentToBackstack(fragment: PapyrusFragment, vararg animations: Int) {
        callThrough?.addContentToBackstack(fragment, *animations)
    }
}
