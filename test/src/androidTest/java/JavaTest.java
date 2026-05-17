import androidx.test.core.app.ActivityScenario;

import com.crescenzi.jintonic.test.JavaActivity;

import org.junit.Test;

import kotlin.Suppress;


/**
 * Test for JavaActivity
 */
@Suppress(names = "FunctionName")
public class JavaTest {


    /*
    ==== See Logcat ====
    It will throw an Exception if there isn't available Internet
 */
    @Test
    public void internet() {
        try (ActivityScenario<JavaActivity> scenario = ActivityScenario.launch(JavaActivity.class)) {
            scenario.onActivity(JavaActivity::javaApiCall);
        }


    }

}
