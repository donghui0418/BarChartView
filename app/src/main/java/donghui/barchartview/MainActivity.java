package donghui.barchartview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private BarChartView barChartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.select).setOnClickListener(this);
        findViewById(R.id.unselect).setOnClickListener(this);
        findViewById(R.id.activate).setOnClickListener(this);
        findViewById(R.id.unactivate).setOnClickListener(this);

        barChartView = findViewById(R.id.bcv);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                barChartView.startAnimator();
                break;
            case R.id.cancel:
                barChartView.cancelAnimator();
                break;
            case R.id.select:
                barChartView.setSelected(true);
                break;
            case R.id.unselect:
                barChartView.setSelected(false);
                break;
            case R.id.activate:
                barChartView.setActivated(true);
                break;
            case R.id.unactivate:
                barChartView.setActivated(false);
                break;
            default:
                break;
        }
    }
}
