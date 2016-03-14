package com.example.ilia.fancycalendar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ilia Kurtov on 11.03.2016.
 */
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    public static final int DAYS_IN_WEEK = 7;
    public static final long ONE_WEEK_LENGTH = TimeUnit.DAYS.toMillis(7);
    public static final long ONE_DAY_LENGTH = TimeUnit.DAYS.toMillis(1);
    private int shift;
    private int emptyShift;
    private boolean isWeekShifted;
    private boolean isMonthStarted;
    private List<CalendarRange> calendarRanges;

    private Calendar startDate;
    private Calendar endDate;

    public void setRange(@NonNull final Calendar startDate, @NonNull final Calendar endDate) {
        this.startDate = getCleanDate(startDate);
        this.endDate = getCleanDate(endDate);

        fillRanges();
        getItemCount();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Long date = find(position);
        if (date == null) {
            holder.bindItem(null);
        } else {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(date * ONE_DAY_LENGTH));
            holder.bindItem(calendar);
        }
    }

    @Nullable
    public Long find(final long position) {
        if (calendarRanges != null) {
            int low = 0;
            int high = calendarRanges.size() - 1;
            while (true) {
                final int mid = (low + high) / 2;
                if (position < calendarRanges.get(mid).getStartRange()) {
                    if (mid == 0 || position > calendarRanges.get(mid - 1).getEndRange()) {
                        break;
                    }
                    high = mid - 1;
                } else if (position > calendarRanges.get(mid).getEndRange()) {
                    if (mid == calendarRanges.size() || position < calendarRanges.get(mid + 1).getStartRange()) {
                        break;
                    }
                    low = mid + 1;
                } else {
                    return calendarRanges.get(mid).getFirstDayReal() + (position - calendarRanges.get(mid).getStartRange());
                }
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return calendarRanges.isEmpty() ? 0 : calendarRanges.get(calendarRanges.size() - 1).getEndRange();
    }

    private void fillRanges() {
        calendarRanges = new ArrayList<>();

        int shift = getFirstDateStart();

        final Calendar startRangeCalendar = Calendar.getInstance(Locale.getDefault());
        startRangeCalendar.setTime(startDate.getTime());
        final int totalDaysCount = (int) ((endDate.getTimeInMillis() - startDate.getTimeInMillis()) / ONE_DAY_LENGTH + 1);

        int additionalShift = 0;
        long firstRangeDate = startRangeCalendar.getTimeInMillis() / ONE_DAY_LENGTH + 1;

        int firstRange = startRangeCalendar.get(Calendar.DAY_OF_MONTH) - 1;
        int daysEnded = 0;

        while (true) {
            final int daysInCurrentMonth = startRangeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            // check if last day in current month less then end of set date range
            if ((daysEnded + (daysInCurrentMonth - firstRange) + additionalShift) <= totalDaysCount) {
                startRangeCalendar.set(startRangeCalendar.get(Calendar.YEAR), startRangeCalendar.get(Calendar.MONTH),
                        startRangeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                // start to start range first date of the next month
                startRangeCalendar.setTime(new Date(startRangeCalendar.getTimeInMillis() + ONE_DAY_LENGTH));
                // check if current day is not first day of the week (monday)
                if (startRangeCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                    calendarRanges.add(new CalendarRange(firstRangeDate, shift + daysEnded, shift + daysEnded + (daysInCurrentMonth - firstRange) + additionalShift - 1));
                    daysEnded += daysInCurrentMonth - firstRange + additionalShift;
                    shift += DAYS_IN_WEEK;
                    firstRangeDate = startRangeCalendar.getTimeInMillis() / ONE_DAY_LENGTH + 1;
                    // erase additional shift
                    additionalShift = 0;
                    firstRange = 0;
                } else {
                    // additional shift needed if first day in the week equals first day in the month
                    additionalShift += daysInCurrentMonth;
                }
            } else {
                calendarRanges.add(new CalendarRange(firstRangeDate, shift + daysEnded, shift + totalDaysCount));
                break;
            }
        }
    }

    private int getFirstDateStart() {
        int firstDateStart = startDate.get(Calendar.DAY_OF_WEEK) - 2;
        if (firstDateStart == -1) {
            firstDateStart += DAYS_IN_WEEK;
        }
        return firstDateStart;
    }

    @NonNull
    private static Calendar getCleanDate(@NonNull final Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView dayText;

        public ViewHolder(final View itemView) {
            super(itemView);
            dayText = (TextView) itemView.findViewById(R.id.day_text);
        }

        public void bindItem(@Nullable final Calendar calendar) {
            if (calendar != null) {
                dayText.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            } else {
                dayText.setText(null);
            }
        }
    }

    public static class CalendarRange {

        private final long firstDayReal;
        private final int startRange;
        private final int endRange;

        public CalendarRange(final long firstDayReal, final int startRange, final int endRange) {
            this.firstDayReal = firstDayReal;
            this.startRange = startRange;
            this.endRange = endRange;
        }

        public long getFirstDayReal() {
            return firstDayReal;
        }

        public int getStartRange() {
            return startRange;
        }

        public int getEndRange() {
            return endRange;
        }

    }

}
