package com.example.ilia.fancycalendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ilia Kurtov on 11.03.2016.
 */
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    public static final int DAYS_IN_WEEK = 7;
    private final List<Calendar> dates;
    private int shift;
    private int emptyShift;
    private boolean isWeekShifted;
    private boolean isMonthStarted;
    private int firstDayOfWeek;
    private List<Range<Integer>> emptyViewsRanges;

    private Calendar startDate;
    private Calendar endDate;


    public CalendarAdapter(final Context context, final List<Calendar> dates) {
        this.dates = dates;

        if (dates != null && !dates.isEmpty()) {
            firstDayOfWeek = dates.get(0).get(Calendar.DAY_OF_WEEK);
            if (firstDayOfWeek == Calendar.SUNDAY) {
                firstDayOfWeek += DAYS_IN_WEEK;
            }
            firstDayOfWeek -= 2;
        }
    }

    public void setRange(@NonNull final Calendar startDate, @NonNull final Calendar endDate) {
        this.startDate = startDate;
        this.endDate = endDate;

        getItemCount();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (emptyViewsRanges.size() > position - emptyShift && position == emptyViewsRanges.get(position - emptyShift)) {
            shift += 1;
            return;
        }

        holder.bindItem(dates.get(position - shift));
        emptyShift += 1;
    }

    @Override
    public int getItemCount() {
        emptyViewsRanges = new ArrayList<>();

        final int firstDateStart = getFirstDateStart();
        if (firstDateStart != 0) {
            emptyViewsRanges.add(new Range<>(0, firstDateStart));
        }

        int shiftCount = firstDateStart;

        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTime(startDate.getTime());

        while (tempCalendar.ge) {
            if (i + 1 < dates.size() && dates.get(i + 1).get(Calendar.MONTH) != dates.get(i).get(Calendar.MONTH)) {
                if (dates.get(i).get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    addEmptyIds(i + 1 + shiftCount, i + 1 + shiftCount + DAYS_IN_WEEK);
                    shiftCount += DAYS_IN_WEEK;
                    finalSize += DAYS_IN_WEEK - dates.get(i).get(Calendar.DAY_OF_WEEK) + 1;
                }
            }
        }
        return finalSize;
    }

    private int getFirstDateStart() {
        int firstDateStart = startDate.get(Calendar.DAY_OF_WEEK) - 2;
        if (firstDateStart == -1) {
            firstDateStart += DAYS_IN_WEEK;
        }
        return firstDateStart;
    }

    private void addEmptyIds(final int firstItem, final int lastItem) {
        if (firstItem > lastItem) {
            Log.d("DANGER", "firstItem cannot be greater then lastItem");
        }
        for (int i = firstItem; i < lastItem; i++) {
            emptyViewsRanges.add(new Range<>(firstItem, lastItem));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView dayText;

        public ViewHolder(final View itemView) {
            super(itemView);
            dayText = (TextView) itemView.findViewById(R.id.day_text);
        }

        public void bindItem(final Calendar calendar) {
            dayText.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        }
    }

}
