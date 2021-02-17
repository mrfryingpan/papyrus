package papyrus.demo.ui.activity

import android.os.Bundle
import android.widget.Toast
import papyrus.core.network.PapyrusNetwork
import papyrus.demo.ui.fragment.MainFragment
import papyrus.ui.activity.PapyrusToolbarActivity

class MainActivity : PapyrusToolbarActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(MainFragment())
        PapyrusNetwork.monitor {
            onNetworkConnected {
                Toast.makeText(this@MainActivity, "Connected", Toast.LENGTH_SHORT).show()
            }

            onNetworkDisconnected {
                Toast.makeText(this@MainActivity, "Disconnected", Toast.LENGTH_SHORT).show()

            }
        }
    }
}