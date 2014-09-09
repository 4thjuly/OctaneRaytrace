package ianet.octaneraytrace;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import ianet.octaneraytrace.Flog.Ray;
import ianet.octaneraytrace.Flog.RayTracer;

public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    public class Data {
        long runs;
        long elapsed;
    }

    public void Measure(Data data) {
        // Run for a second
        long start = System.currentTimeMillis();
        long elapsed = 0;
        int i = 0;

        while (elapsed < 1000) {
            RayTracer.renderScene();
            i++;
            elapsed = System.currentTimeMillis() - start;
        }

        if (data != null) {
            data.runs += i;
            data.elapsed += elapsed;
        }
    }

    // Pulled from the Octane sources
    static int MIN_ITERATIONS = 32;
    static long REFERENCE_SCORE = 739989;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_run) {
            // Warmup
            Measure(null);
            // Benchmark
            Data data = new Data();
            while (data.runs < MIN_ITERATIONS) {
                Measure(data);
            }
            TextView tv = (TextView) findViewById(R.id.textview);
            long usec = (data.elapsed * 1000) / data.runs;
            long formated = (REFERENCE_SCORE / usec) * 100;
            tv.append("\r\n");
            tv.append("Result: " + formated);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
