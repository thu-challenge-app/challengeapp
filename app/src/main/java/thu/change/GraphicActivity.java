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

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GraphicActivity extends AppCompatActivity {
    private final int MIN_DAYS_IN_GRAPH = 5;
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
        TextView tthisChallenge = (TextView) findViewById(R.id.textView_GraphicChallenge);
        tthisChallenge.setText(ch.getName());

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
        TextView tweekly = findViewById(R.id.textView_GraphicWeekly);
        if (ch.getWeekly() || (ch.getAbove() && ch.getMaximum() == 0)) {
            piechart.setVisibility(View.INVISIBLE);
            findViewById(R.id.divider7).setVisibility(View.VISIBLE);
            findViewById(R.id.textView_GraphicWeekly).setVisibility(View.VISIBLE);
            findViewById(R.id.textView4).setVisibility(View.INVISIBLE);
            if (ch.getMaximum() ==  0)
                tweekly.setText("Du willst\n" + ch.getMaximum() + " " + ch.getUnit() + "\nerreichen");
            if (ch.getWeekly())
                tweekly.setText("Diese Challenge\nmachst du\nwöchentlich");
        }
        else {
            piechart.setUsePercentValues(true);
            Description desc = new Description();
            desc.setText("");
            desc.setTextSize(12f);
            piechart.setRotationEnabled(true);
            piechart.setHoleRadius(60f);
            piechart.setTransparentCircleAlpha(0);
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
            int percent;

            if (!ch.getAbove()) {
                if(dayvalue >= max){
                    value.add(new PieEntry(0, ""));
                    value.add(new PieEntry(max, ""));
                }
                else{
                    value.add(new PieEntry(100, ""));
                    value.add(new PieEntry(0, ""));
                }
            }
            else {
                value.add(new PieEntry(dayvalue, ""));
                value.add(new PieEntry(max - dayvalue, ""));
            }
            if (ch.getAbove())
                percent = (dayvalue * 100) / max;
            else
                percent = (dayvalue >= max) ? 0 : 100;

            // Prozent in Pie Chart darstellen

            piechart.setCenterText("Ziel zu\n" + String.valueOf(percent) + " %\nerreicht");
            PieDataSet pieDataSet = new PieDataSet(value, "");
            pieDataSet.setColors(new ArrayList<Integer>(Arrays.asList(ContextCompat.getColor(this, R.color.pieChartGreen), ContextCompat.getColor(this, R.color.pieChartRed))));
            PieData pieData = new PieData(pieDataSet);
            piechart.setData(pieData);
            pieData.setDrawValues(false);
            Legend legend1 = piechart.getLegend();
            legend1.setEnabled(false);
        }

        //LineChart
        mChart = (LineChart) findViewById(R.id.linechart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.enableAxisLineDashedLine(10f, 10f, 0);
        leftAxis.setDrawGridLinesBehindData(true);
        mChart.getAxisRight().setEnabled(false);
        mChart.setDescription(null);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new MyValueFormatter());
        xAxis.setLabelRotationAngle(270f);

        Legend legend = mChart.getLegend();
        legend.setEnabled(true);
        String legendstring = ch.getName();
        if (!ch.getUnit().isEmpty())
            legendstring += " (" + ch.getUnit() + ")";
        legend.setCustom(new LegendEntry[]{new LegendEntry(legendstring, Legend.LegendForm.DEFAULT, 10f, 2f, null, Color.parseColor("#D56214")),
                                           new LegendEntry("Durchschnitt", Legend.LegendForm.DEFAULT, 10f, 2f, null, Color.parseColor("#00FF00"))});
        legend.setTextSize(15);
        legend.setWordWrapEnabled(true);

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
        startdate.set(Calendar.YEAR, year);
        startdate.set(Calendar.MONTH, month);
        startdate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        if (startdate.getTimeInMillis() > datepicker_Start.getDatePicker().getMaxDate())
            startdate.setTimeInMillis(datepicker_Start.getDatePicker().getMaxDate());

        String date_von = "von: " + startdate.get(Calendar.DAY_OF_MONTH) + "."+ (startdate.get(Calendar.MONTH)+1) + "."  + startdate.get(Calendar.YEAR);
        dateText_von.setText(date_von);

        datepicker_End.getDatePicker().setMinDate(0);
        datepicker_End.getDatePicker().setMinDate(startdate.getTimeInMillis() + MIN_DAYS_IN_GRAPH * 24 * 60 * 60 * 1000);
        datepicker_End.getDatePicker().invalidate();
        updateGraph();
    }
    private void onSetDate_End(DatePicker view, int year, int month, int dayOfMonth){
        enddate.set(Calendar.YEAR, year);
        enddate.set(Calendar.MONTH, month);
        enddate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        if (enddate.getTimeInMillis() < datepicker_End.getDatePicker().getMinDate())
            enddate.setTimeInMillis(datepicker_End.getDatePicker().getMinDate());

        String date_von = "bis: " + enddate.get(Calendar.DAY_OF_MONTH) + "."+ (enddate.get(Calendar.MONTH)+1) + "."  + enddate.get(Calendar.YEAR);
        dateText_bis.setText(date_von);

        datepicker_Start.getDatePicker().setMaxDate(Long.MAX_VALUE);
        datepicker_Start.getDatePicker().setMaxDate(enddate.getTimeInMillis() - MIN_DAYS_IN_GRAPH * 24 * 60 * 60 * 1000);
        datepicker_Start.getDatePicker().invalidate();
        updateGraph();
    }

    private void updateGraph() {
        //Werte LineChart
        long starttime = startdate.getTimeInMillis() / 1000L;
        long endtime = enddate.getTimeInMillis() / 1000L;
        ArrayList<Entry> yValues = db.getChallengeValueListBetween(ch, starttime, endtime);

        LineDataSet set1 = new LineDataSet(yValues, "");
        set1.setFillAlpha(110);
        set1.setColor(Color.parseColor("#D56214"));
        set1.setLineWidth(3f);
        set1.setValueTextSize(15f);
        set1.setValueTextColor(Color.parseColor("#D56214"));
        set1.setValueFormatter(new IntValueFormatter());

        ArrayList<Entry> averageValues = new ArrayList<Entry>();
        averageValues.add(new Entry(starttime, ch.getAverage()));
        averageValues.add(new Entry(endtime, ch.getAverage()));
        LineDataSet set2 = new LineDataSet(averageValues, "");
        set2.setValueTextSize(0.00001f);
        set2.setCircleRadius(1f);
        set2.setColor(Color.parseColor("#00FF00"));

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.getXAxis().setAxisMinimum(starttime);
        mChart.getXAxis().setAxisMaximum(endtime);
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

class IntValueFormatter extends ValueFormatter {
    @Override
    public String getPointLabel(Entry entry) {
        return String.valueOf((int)entry.getY());
    }
}
