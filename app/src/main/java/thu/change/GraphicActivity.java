package thu.change;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class GraphicActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChart = (LineChart) findViewById(R.id.linechart);

        //mChart.setOnChartGestureListener(MainActivity.this);
        //mChart.setOnChartValueSelectedListener(MainActivity.this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        LimitLine upper_limit = new LimitLine(65f, "Durchschnitt");
        upper_limit.setLineWidth(4f);
        upper_limit.enableDashedLine(10f,10f,0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(15f);



        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(upper_limit);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(25f);
        leftAxis.enableAxisLineDashedLine(10f, 10f, 0);
        leftAxis.setDrawGridLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);
        //mChart.getAxisLeft().setEnabled(false);
        //mChart.getXAxis().setEnabled(false);

        ArrayList<Entry> yValues = new ArrayList<>();


        yValues.add(new Entry(1576575734,50f));
        yValues.add(new Entry(1576662134,70f));
        yValues.add(new Entry(1576748534,30f));
//        yValues.add(new Entry(1576834934,50f));
//        yValues.add(new Entry(1576921334,60f));
//        yValues.add(new Entry(1577007734,65f));
//        yValues.add(new Entry(1577094134,60f));
//        yValues.add(new Entry(1577180534,35f));
//        yValues.add(new Entry(1577266934,90f));
//        yValues.add(new Entry(10,65f));
//        yValues.add(new Entry(11,65f));
//        yValues.add(new Entry(12,65f));
//        yValues.add(new Entry(13,65f));
//        yValues.add(new Entry(14,65f));
//        yValues.add(new Entry(15,65f));
//        yValues.add(new Entry(16,65f));
//        yValues.add(new Entry(17,65f));
//        yValues.add(new Entry(18,65f));
//        yValues.add(new Entry(19,65f));
//        yValues.add(new Entry(20,65f));
//        yValues.add(new Entry(21,65f));
//        yValues.add(new Entry(22,65f));
//        yValues.add(new Entry(23,65f));





        LineDataSet set1 = new LineDataSet(yValues, "Data set 1");

        set1.setFillAlpha(110);
        set1.setColor(Color.RED);
        set1.setLineWidth(3f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.GREEN);



        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        mChart.setData(data);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        Legend legend = mChart.getLegend();
        legend.setEnabled(true);

        xAxis.setValueFormatter(new MyValueFormatter());


        //xAxis.setValueFormatter(new MyXAxisValueFormatter(values));
        //xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(45f);

    }


}

class MyValueFormatter extends ValueFormatter{
    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        //return super.getAxisLabel(value, axis)
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
        return sdf.format(new Date((long)value*1000));

    }
}