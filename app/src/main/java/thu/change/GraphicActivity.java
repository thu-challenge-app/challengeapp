package thu.change;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
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
import com.github.mikephil.charting.components.LegendEntry;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GraphicActivity extends AppCompatActivity {
    private Challenge ch;
    private DatabaseHelper db;
    private TextView dateText_von;
    private TextView dateText_bis;
    private LineChart mChart;
    private DatePickerDialog datepicker_Start;
    private DatePickerDialog datepicker_End;
    Calendar startdate;
    Calendar enddate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic);

        // Darzustellende Challenge aus DB abfragen
        Intent myIntent = getIntent();
        int id=myIntent.getIntExtra("Challenge_id",0);
        db = new DatabaseHelper(this);
        ch = db.getChallenge(id);

        // Textfelder für Datumsanzeige
        dateText_von = findViewById(R.id.date_text_von);
        dateText_bis = findViewById(R.id.date_text_bis);

        // Start und Enddatum ermitteln
        startdate = Calendar.getInstance();
        enddate = Calendar.getInstance();
        if (ch.getWeekly())
            startdate.add(Calendar.DATE, -28);
        else
            startdate.add(Calendar.DATE, -7);

        // Datepicker
        datepicker_Start = new DatePickerDialog(
                this, R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        onSetDate_Start(view, year, month, dayOfMonth);
                    }
                },
                startdate.get(Calendar.YEAR),
                startdate.get(Calendar.MONTH),
                startdate.get(Calendar.DAY_OF_MONTH)
        );
        datepicker_End = new DatePickerDialog(
                this, R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        onSetDate_End(view, year, month, dayOfMonth);
                    }
                },
                enddate.get(Calendar.YEAR),
                enddate.get(Calendar.MONTH),
                enddate.get(Calendar.DAY_OF_MONTH)
        );

        //PieChart
        PieChart piechart = findViewById(R.id.idPieChart);
        if (ch.getWeekly() || ch.getAbove())
            piechart.setVisibility(View.INVISIBLE);
        else {
            piechart.setUsePercentValues(true);
            Description desc = new Description();
            desc.setText("");
            desc.setTextSize(20f);
            piechart.setRotationEnabled(true);
            piechart.setHoleRadius(60f);
            piechart.setTransparentCircleAlpha(0);
            piechart.setCenterText("HEUTE");
            piechart.setHoleColor(ContextCompat.getColor(this, R.color.colorBackground));
            piechart.setCenterTextSize(10);
            piechart.setDescription(desc);
            List<PieEntry> value = new ArrayList<>();
            int max = ch.getMaximum();
            int dayvalue = max;
            if (db.isTodaysChallengeValueSet(ch))
                dayvalue = db.getTodaysChallengeValue(ch);
            if (dayvalue > max)
                dayvalue = max;
            value.add(new PieEntry(max - dayvalue, ""));
            value.add(new PieEntry( dayvalue, ""));
            PieDataSet pieDataSet = new PieDataSet(value, "");
            pieDataSet.setColors(new ArrayList<Integer>(Arrays.asList(ContextCompat.getColor(this, R.color.pieChartGreen), ContextCompat.getColor(this, R.color.pieChartRed))));
            PieData pieData = new PieData(pieDataSet);
            piechart.setData(pieData);
            pieData.setDrawValues(false);
            //pieDataSet.setColor(Color.GREEN);
            //piechart.animateXY(1400,1400);
            Legend legend1 = piechart.getLegend();
            legend1.setEnabled(false);
        }

        //LineChart
        mChart = (LineChart) findViewById(R.id.linechart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        //Durchschnitt
        LimitLine upper_limit = new LimitLine(ch.getAverage(), "Durchschnitt");
        upper_limit.setLineWidth(4f);
        upper_limit.enableDashedLine(10f,10f,0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(15f);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(upper_limit);
        //leftAxis.setAxisMaximum(100f);
        //leftAxis.setAxisMinimum(ch.getAverage());
        leftAxis.enableAxisLineDashedLine(10f, 10f, 0);
        leftAxis.setDrawGridLinesBehindData(true);
        //leftAxis.set
        mChart.getAxisRight().setEnabled(false);
        mChart.setDescription(null);


        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        Legend legend = mChart.getLegend();
        //legend.setEnabled(false);
        legend.setEnabled(true);
        String legendstring = ch.getName() + " (" + ch.getUnit() + ")";
        legend.setCustom(new LegendEntry[]{new LegendEntry(legendstring, Legend.LegendForm.DEFAULT, 10f, 2f, null, Color.RED)});
        legend.setTextSize(15);
        xAxis.setValueFormatter(new MyValueFormatter());
        xAxis.setLabelRotationAngle(45f);

        onSetDate_Start(null, startdate.get(Calendar.YEAR), startdate.get(Calendar.MONTH), startdate.get(Calendar.DAY_OF_MONTH));
        onSetDate_End(null, enddate.get(Calendar.YEAR), enddate.get(Calendar.MONTH), enddate.get(Calendar.DAY_OF_MONTH));
    }

    public void onClick_Anfang (View view) {
        datepicker_Start.show();
    }
    public void onClick_Ende (View view) {
        datepicker_End.show();
    }

    //Date-Picker
    private void onSetDate_Start(DatePicker view, int year, int month, int dayOfMonth){
        String date_von = "von: " + dayOfMonth + "."+ (month+1) + "."  + year;
        dateText_von.setText(date_von);

        startdate.set(Calendar.YEAR, year);
        startdate.set(Calendar.MONTH, month);
        startdate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        datepicker_End.getDatePicker().setMinDate(startdate.getTimeInMillis());
        updateGraph();
    }
    private void onSetDate_End(DatePicker view, int year, int month, int dayOfMonth){
        String date_von = "bis: " + dayOfMonth + "."+ (month+1) + "."  + year;
        dateText_bis.setText(date_von);

        enddate.set(Calendar.YEAR, year);
        enddate.set(Calendar.MONTH, month);
        enddate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        datepicker_Start.getDatePicker().setMaxDate(enddate.getTimeInMillis());
        updateGraph();
    }

    private void updateGraph() {
        //Werte LineChart
        ArrayList<Entry> yValues = db.getChallengeValueListBetween(startdate.getTimeInMillis() / 1000L, enddate.getTimeInMillis() / 1000L);

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
        mChart.invalidate();
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

