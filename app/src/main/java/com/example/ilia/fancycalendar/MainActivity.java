package com.example.ilia.fancycalendar;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

//    private final View startButton;
//    private final View endButton;
    private RecyclerView recyclerView;
    private CalendarAdapter calendarAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        startButton = findViewById(R.id.starting_calendar_button);
//        endButton = findViewById(R.id.ending_calendar_button);

        final List<Calendar> calendars = new ArrayList<>();
        final Date currentDate = new Date();

        for (int i = 0; i < 1000; i++) {
            final Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(new Date(currentDate.getTime() + TimeUnit.DAYS.toMillis(i)));
            calendars.add(calendar);
        }

        calendarAdapter = new CalendarAdapter(this, calendars);

        recyclerView = (RecyclerView) findViewById(R.id.calendar_list);
        recyclerView.setAdapter(calendarAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 7));

    }





}
