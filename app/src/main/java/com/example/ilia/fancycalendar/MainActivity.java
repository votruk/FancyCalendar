package com.example.ilia.fancycalendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<Calendar> calendars = new ArrayList<>();
        final Date currentDate = new Date();

        final CalendarAdapter calendarAdapter = new CalendarAdapter();

        final Calendar startDate = Calendar.getInstance();
        startDate.set(2016, 2, 11);

        final Calendar endDate = Calendar.getInstance();
        endDate.set(2217, 2, 10);

        calendarAdapter.setRange(startDate, endDate);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.calendar_list);
        recyclerView.setAdapter(calendarAdapter);
        final StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);

    }

}
