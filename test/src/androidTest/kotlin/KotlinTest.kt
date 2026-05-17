@file:Suppress("FunctionName")

import androidx.lifecycle.lifecycleScope
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.crescenzi.jintonic.test.KotlinActivity
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.runner.RunWith


/**
Test for KotlinActivity
 */
@RunWith(AndroidJUnit4::class)
class KotlinTest {


    /*
    ==== See Logcat ====
     */
    @Test
    fun timed() {
        val scenario = launchActivity<KotlinActivity>()
        scenario.onActivity {
            it?.lifecycleScope?.launch {
                it.hello()
            }
        }
    }

    @Test
    fun minBattery() {
        val scenario = launchActivity<KotlinActivity>()
        scenario.onActivity {
            it?.longOperation()
        }
    }

    /*
   The test fails if there is no active VPN
     */
    @Test
    fun vpn() {
        val scenario = launchActivity<KotlinActivity>()
        scenario.onActivity {
            it?.vpnMethod()
        }
    }

    /*
    Throws an Exception if the device is not rooted
     */
    @Test
    fun root() {
        val scenario = launchActivity<KotlinActivity>()
        scenario.onActivity {
            it?.rootMethod()
        }
    }

}