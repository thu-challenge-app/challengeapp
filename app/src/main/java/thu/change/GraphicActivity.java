package thu.change;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GraphicActivity extends AppCompatActivity {

    private TextView dateText_von;
    private TextView dateText_bis;
    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic);
        //Date-Picker
        dateText_von = findViewById(R.id.date_text_von);
        dateText_bis = findViewById(R.id.date_text_bis);


        findViewById(R.id.Anfang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog_Anfang();
            }
        });

        findViewById(R.id.Ende).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog_Ende();
            }
        });

        //PieChart
        PieChart piechart = findViewById(R.id.idPieChart);
        piechart.setUsePercentValues(true);
        Description desc = new Description();
        desc.setText("");
        desc.setTextSize(20f);
        piechart.setRotationEnabled(true);
        piechart.setHoleRadius(60f);
        piechart.setTransparentCircleAlpha(0);
        piechart.setCenterText("HEUTE");
        piechart.setHoleColor(R.color.colorBackground);
        piechart.setCenterTextSize(20);
        piechart.setDescription(desc);
        List<PieEntry> value = new ArrayList<>();
        value.add(new PieEntry(40f ,""));
        value.add(new PieEntry(50f ,""));
        PieDataSet pieDataSet = new PieDataSet(value,"");
        PieData pieData = new PieData(pieDataSet);
        piechart.setData(pieData);
        pieData.setDrawValues(false);
        //pieDataSet.setColor(Color.GREEN);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        //piechart.animateXY(1400,1400);
        Legend legend1 = piechart.getLegend();
        legend1.setEnabled(false);

        //LineChart
        mChart = (LineChart) findViewById(R.id.linechart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);


        //Durchschnitt
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
        mChart.setDescription(null);

        //Werte LineChart
        ArrayList<Entry> yValues = new ArrayList<>();
        yValues.add(new Entry(1576575734,50f));
        yValues.add(new Entry(1576662134,70f));
        yValues.add(new Entry(1576748534,30f));
        yValues.add(new Entry(1576834934,50f));
        yValues.add(new Entry(1576921334,60f));
        yValues.add(new Entry(1577007734,65f));
        yValues.add(new Entry(1577094134,60f));
        yValues.add(new Entry(1577180534,35f));
        yValues.add(new Entry(1577266934,90f));

        LineDataSet set1 = new LineDataSet(yValues, "");
        set1.setFillAlpha(110);
        set1.setColor(Color.RED);
        set1.setLineWidth(3f);
        set1.setValueTextSize(15f);
        set1.setValueTextColor(Color.RED);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        mChart.setData(data);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        Legend legend = mChart.getLegend();
        legend.setEnabled(false);
        xAxis.setValueFormatter(new MyValueFormatter());
        xAxis.setLabelRotationAngle(45f);

    }
    //Date-Picker
    private void showDatePickerDialog_Anfang(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date_von = "von: " + dayOfMonth + "."+ (month+1) + "."  + year;
                dateText_von.setText(date_von);
            }
        },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();

    }
    private void showDatePickerDialog_Ende(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date_bis = "bis: " + dayOfMonth + "."+ (month+1) + "."  + year;
                        dateText_bis.setText(date_bis);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();

    }

}

//LineChart
class MyValueFormatter extends ValueFormatter {
    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
        return sdf.format(new Date((long)value*1000));

    }
}

