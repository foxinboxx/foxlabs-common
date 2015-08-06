/* 
 * Copyright (C) 2012 FoxLabs
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.foxlabs.util;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

import static java.util.Calendar.*;

import org.foxlabs.util.resource.Service;

/**
 * This class allows to evaluate period string for dates. It supports past and
 * future dates.
 * 
 * <p>Additional locales are supported through SPI interface.
 * The <code>META-INF/services/org.foxlabs.util.PeriodCounter</code> file
 * should be used to list new counters.</p>
 * 
 * @author Fox Mulder
 */
public class PeriodCounter {
    
    /**
     * Locale of this counter.
     */
    protected final Locale locale;
    
    /**
     * Constructs a new <code>PeriodCounter</code> with the specified locale.
     * 
     * @param locale Locale of this counter.
     */
    protected PeriodCounter(Locale locale) {
        this.locale = locale;
    }
    
    /**
     * Returns locale of this counter.
     * 
     * @return Locale of this counter.
     */
    public final Locale getLocale() {
        return locale;
    }
    
    /**
     * Evaluates period string for the specified date.
     * 
     * @param date Date for which period string should be evaluated.
     * @return Period string for the specified date.
     */
    public final String evaluate(Date date) {
        Calendar now = Calendar.getInstance(locale);
        Calendar src = Calendar.getInstance(locale);
        Calendar tst = Calendar.getInstance(locale);
        
        src.setTime(date);
        boolean future = src.compareTo(now) > 0;
        if (future) {
            Calendar cal = src;
            src = now;
            now = cal;
        }
        
        tst.setTime(date);
        tst.add(MINUTE, 1);
        if (now.compareTo(tst) < 0) {
            return getNowPeriod();
        }
        
        tst.setTime(date);
        tst.add(HOUR_OF_DAY, 1);
        if (now.compareTo(tst) < 0) {
            int count = now.get(HOUR_OF_DAY) == src.get(HOUR_OF_DAY)
                ? now.get(MINUTE) - src.get(MINUTE)
                : now.get(MINUTE) + src.getActualMaximum(MINUTE) - src.get(MINUTE);
            return getMinutePeriod(count, future);
        }
        
        tst.setTime(date);
        tst.add(DAY_OF_MONTH, 1);
        if (now.compareTo(tst) < 0) {
            int count = now.get(DAY_OF_MONTH) == src.get(DAY_OF_MONTH)
                ? now.get(HOUR_OF_DAY) - src.get(HOUR_OF_DAY)
                : now.get(HOUR_OF_DAY) + src.getActualMaximum(HOUR_OF_DAY) - src.get(HOUR_OF_DAY);
            return getHourPeriod(count, future);
        }
        
        tst.setTime(date);
        tst.add(WEEK_OF_MONTH, 1);
        if (now.compareTo(tst) < 0) {
            int count = now.get(MONTH) == src.get(MONTH)
                ? now.get(DAY_OF_MONTH) - src.get(DAY_OF_MONTH)
                : now.get(DAY_OF_MONTH) + src.getActualMaximum(DAY_OF_MONTH) - src.get(DAY_OF_MONTH);
            return getDayPeriod(count, future);
        }
        
        tst.setTime(date);
        tst.add(MONTH, 1);
        if (now.compareTo(tst) < 0) {
            int count = now.get(MONTH) == src.get(MONTH)
                ? now.get(WEEK_OF_MONTH) - src.get(WEEK_OF_MONTH)
                : now.get(WEEK_OF_MONTH) + src.getActualMaximum(WEEK_OF_MONTH) - src.get(WEEK_OF_MONTH);
            return getWeekPeriod(count, future);
        }
        
        tst.setTime(date);
        tst.add(YEAR, 1);
        if (now.compareTo(tst) < 0) {
            int count = now.get(YEAR) == src.get(YEAR)
                ? now.get(MONTH) - src.get(MONTH)
                : now.get(MONTH) + src.getActualMaximum(MONTH) - src.get(MONTH);
            return getMonthPeriod(count == 0 ? 1 : count, future);
        }
        
        return getYearPeriod(now.get(YEAR) - src.get(YEAR), future);
    }
    
    /**
     * Returns period string for now (less than 1 minute).
     * 
     * @return Period string for now.
     */
    protected String getNowPeriod() {
        return "just now";
    }
    
    /**
     * Returns period string in minutes (less than 1 hour).
     * 
     * @param count Number of minutes.
     * @param future Determines if the specified period is past or future.
     * @return Period string in minutes.
     */
    protected String getMinutePeriod(int count, boolean future) {
        return count == 1
            ? future ? "in a minute" : "a minute ago"
            : future ? count + " minutes" : count + " minutes ago";
    }
    
    /**
     * Returns period string in hours (less than 1 day).
     * 
     * @param count Number of hours.
     * @param future Determines if the specified period is past or future.
     * @return Period string in hours.
     */
    protected String getHourPeriod(int count, boolean future) {
        return count == 1
            ? future ? "in an hour" : "a hour ago"
            : future ? count + " hours" : count + " hours ago";
    }
    
    /**
     * Returns period string in days (less than 1 week).
     * 
     * @param count Number of days.
     * @param future Determines if the specified period is past or future.
     * @return Period string in days.
     */
    protected String getDayPeriod(int count, boolean future) {
        return count == 1
            ? future ? "a day about" : "a day ago"
            : future ? count + " days" : count + " days ago";
    }
    
    /**
     * Returns period string in weeks (less than 1 month).
     * 
     * @param count Number of weeks.
     * @param future Determines if the specified period is past or future.
     * @return Period string in weeks.
     */
    protected String getWeekPeriod(int count, boolean future) {
        return count == 1
            ? future ? "in a week" : "a week ago"
            : future ? count + " weeks" : count + " weeks ago";
    }
    
    /**
     * Returns period string in months (less than 1 year).
     * 
     * @param count Number of months.
     * @param future Determines if the specified period is past or future.
     * @return Period string in months.
     */
    protected String getMonthPeriod(int count, boolean future) {
        return count == 1
            ? future ? "a month" : "a month ago"
            : future ? count + " months" : count + " months ago";
    }
    
    /**
     * Returns period string in years.
     * 
     * @param count Number of years.
     * @param future Determines if the specified period is past or future.
     * @return Period string in years.
     */
    protected String getYearPeriod(int count, boolean future) {
        return count == 1
            ? future ? "within a year" : "an year ago"
            : future ? count + " years" : count + " years ago";
    }
    
    // English
    
    /**
     * <code>PeriodCounter</code> instance for english locale.
     */
    public static final PeriodCounter ENGLISH = new PeriodCounter(Locale.ENGLISH);
    
    // Russian
    
    /**
     * <code>PeriodCounter</code> instance for russian locale.
     */
    public static final PeriodCounter RUSSIAN = new PeriodCounter(new Locale("ru")) {
        
        /**
         * {@inheritDoc}
         */
        @Override
        protected String getNowPeriod() {
            return "\u0421\u0435\u0439\u0447\u0430\u0441";
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        protected String getMinutePeriod(int count, boolean future) {
            return count == 1
                ? future
                    ? "\u0427\u0435\u0440\u0435\u0437 \u043C\u0438\u043D\u0443\u0442\u0443"
                    : "\u041C\u0438\u043D\u0443\u0442\u0443 \u043D\u0430\u0437\u0430\u0434"
                : count > 20 && count % 10 == 1 
                    ? future
                        ? "\u0427\u0435\u0440\u0435\u0437 " + count + " \u043C\u0438\u043D\u0443\u0442\u0443"
                        : count + " \u043C\u0438\u043D\u0443\u0442\u0443 \u043D\u0430\u0437\u0430\u0434"
                    : (count < 10 || count > 20) && count % 10 > 0 && count % 10 < 5
                        ? future
                            ? "\u0427\u0435\u0440\u0435\u0437 " + count + " \u043C\u0438\u043D\u0443\u0442\u044B"
                            : count + " \u043C\u0438\u043D\u0443\u0442\u044B \u043D\u0430\u0437\u0430\u0434"
                        : future
                            ? "\u0427\u0435\u0440\u0435\u0437 " + count + " \u043C\u0438\u043D\u0443\u0442"
                            : count + " \u043C\u0438\u043D\u0443\u0442 \u043D\u0430\u0437\u0430\u0434";
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        protected String getHourPeriod(int count, boolean future) {
            return count == 1
                ? future
                    ? "\u0427\u0435\u0440\u0435\u0437 \u0447\u0430\u0441"
                    : "\u0427\u0430\u0441 \u043D\u0430\u0437\u0430\u0434"
                : count > 20 && count % 10 == 1 
                    ? future
                        ? "\u0427\u0435\u0440\u0435\u0437 " + count + " \u0447\u0430\u0441"
                        : count + " \u0447\u0430\u0441 \u043D\u0430\u0437\u0430\u0434"
                    : (count < 10 || count > 20) && count % 10 < 5
                        ? future
                            ? "\u0427\u0435\u0440\u0435\u0437 " + count + " \u0447\u0430\u0441\u0430"
                            : count + " \u0447\u0430\u0441\u0430 \u043D\u0430\u0437\u0430\u0434"
                        : future
                            ? "\u0427\u0435\u0440\u0435\u0437 " + count + " \u0447\u0430\u0441\u043E\u0432"
                            : count + " \u0447\u0430\u0441\u043E\u0432 \u043D\u0430\u0437\u0430\u0434";
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        protected String getDayPeriod(int count, boolean future) {
            return count == 1
                ? future
                    ? "\u0427\u0435\u0440\u0435\u0437 \u0434\u0435\u043D\u044C"
                    : "\u0414\u0435\u043D\u044C \u043D\u0430\u0437\u0430\u0434"
                : count > 20 && count % 10 == 1 
                    ? future
                        ? "\u0427\u0435\u0440\u0435\u0437 " + count + " \u0434\u0435\u043D\u044C"
                        : count + " \u0434\u0435\u043D\u044C \u043D\u0430\u0437\u0430\u0434"
                    : (count < 10 || count > 20) && count % 10 > 0 && count % 10 < 5
                        ? future
                            ? "\u0427\u0435\u0440\u0435\u0437 " + count + " \u0434\u043D\u044F"
                            : count + " \u0434\u043D\u044F \u043D\u0430\u0437\u0430\u0434"
                        : future
                            ? "\u0427\u0435\u0440\u0435\u0437 " + count + " \u0434\u043D\u0435\u0439"
                            : count + " \u0434\u043D\u0435\u0439 \u043D\u0430\u0437\u0430\u0434";
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        protected String getWeekPeriod(int count, boolean future) {
            return count == 1
                ? future
                    ? "\u0427\u0435\u0440\u0435\u0437 \u043D\u0435\u0434\u0435\u043B\u044E"
                    : "\u041D\u0435\u0434\u0435\u043B\u044E \u043D\u0430\u0437\u0430\u0434"
                : future
                    ? "\u0427\u0435\u0440\u0435\u0437 " + count + " \u043D\u0435\u0434\u0435\u043B\u0438"
                    : count + " \u043D\u0435\u0434\u0435\u043B\u0438 \u043D\u0430\u0437\u0430\u0434";
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        protected String getMonthPeriod(int count, boolean future) {
            return count == 1
                ? future
                    ? "\u0427\u0435\u0440\u0435\u0437 \u043C\u0435\u0441\u044F\u0446"
                    : "\u041C\u0435\u0441\u044F\u0446 \u043D\u0430\u0437\u0430\u0434"
                : count < 5
                    ? future
                        ? "\u0427\u0435\u0440\u0435\u0437 " + count + " \u043C\u0435\u0441\u044F\u0446\u0430"
                        : count + " \u043C\u0435\u0441\u044F\u0446\u0430 \u043D\u0430\u0437\u0430\u0434"
                    : future
                        ? "\u0427\u0435\u0440\u0435\u0437 " + count + " \u043C\u0435\u0441\u044F\u0446\u0435\u0432"
                        : count + " \u043C\u0435\u0441\u044F\u0446\u0435\u0432 \u043D\u0430\u0437\u0430\u0434";
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        protected String getYearPeriod(int count, boolean future) {
            return count == 1
                ? future
                    ? "\u0427\u0435\u0440\u0435\u0437 \u0433\u043E\u0434"
                    : "\u0413\u043E\u0434 \u043D\u0430\u0437\u0430\u0434"
                : count > 20 && count % 10 == 1 
                    ? future
                        ? "\u0427\u0435\u0440\u0435\u0437 " + count + " \u0433\u043E\u0434"
                        : count + " \u0433\u043E\u0434 \u043D\u0430\u0437\u0430\u0434"
                    : (count < 10 || count > 20) && count % 10 > 0 && count % 10 < 5
                        ? future
                            ? "\u0427\u0435\u0440\u0435\u0437 " + count + " \u0433\u043E\u0434\u0430"
                            : count + " \u0433\u043E\u0434\u0430 \u043D\u0430\u0437\u0430\u0434"
                        : future
                            ? "\u0427\u0435\u0440\u0435\u0437 " + count + " \u043B\u0435\u0442"
                            : count + " \u043B\u0435\u0442 \u043D\u0430\u0437\u0430\u0434";
        }
        
    };
    
    // Factory methods
    
    /**
     * Available <code>PeriodCounter</code>s.
     */
    static final Map<Locale, PeriodCounter> counters = new HashMap<Locale, PeriodCounter>();
    
    // Searches for new counters using SPI
    static {
        counters.put(ENGLISH.getLocale(), ENGLISH);
        counters.put(RUSSIAN.getLocale(), RUSSIAN);
        Iterator<PeriodCounter> itr = Service.lookup(PeriodCounter.class);
        while (itr.hasNext()) {
            PeriodCounter counter = itr.next();
            counters.put(counter.getLocale(), counter);
        }
    }
    
    /**
     * Returns period string for the specified date and default locale.
     * 
     * @param date Date for which period should be returned.
     * @return Period string for the specified date and default locale.
     * @see #period(Date, Locale)
     */
    public static String period(Date date) {
        return period(date, Locale.getDefault());
    }
    
    /**
     * Returns period string for the specified date and locale.
     * 
     * @param date Date for which period should be returned.
     * @param locale Desired locale.
     * @return Period string for the specified date and locale.
     */
    public static String period(Date date, Locale locale) {
        PeriodCounter counter = counters.get(locale);
        if (counter != null) {
            return counter.evaluate(date);
        }
        if (locale.getVariant().length() > 0) {
            locale = new Locale(locale.getLanguage(), locale.getCountry());
            counter = counters.get(locale);
            if (counter != null) {
                return counter.evaluate(date);
            }
        }
        if (locale.getCountry().length() > 0) {
            locale = new Locale(locale.getLanguage());
            counter = counters.get(locale);
            if (counter != null) {
                return counter.evaluate(date);
            }
        }
        return ENGLISH.evaluate(date);
    }
    
    /**
     * Parses the specified string representation of time interval and returns
     * number of milliseconds. The format is <code>*w *d *h *m *s</code>
     * (representing weeks, days, hours, minutes and seconds - where
     * <code>*</code> is positive number).
     * 
     * @param interval String representation of time interval.
     * @return Number of milliseconds.
     * @throws IllegalArgumentException if the specified string could not be
     *         parsed as time interval.
     */
    public static long decodeInterval(String interval) {
        int length = interval == null ? 0 : (interval = interval.trim()).length();
        if (length == 0) {
            return 0L;
        }
        
        int count = -1;
        long result = 0L;
        boolean[] flags = new boolean[INTERVAL_SIZES.length];
        for (int i = 0; i < length; i++) {
            char ch = interval.charAt(i);
            if (ch >= '0' && ch <= '9') {
                count = (count < 0 ? 0 : count * 10) + ch - '0';
            } else {
                int unit = INTERVAL_UNITS.indexOf(Character.toLowerCase(ch));
                if (unit < 0) {
                    if (!Character.isWhitespace(ch)) {
                        throw new IllegalArgumentException(interval);
                    }
                } else {
                    if (count < 0 || flags[unit]) {
                        throw new IllegalArgumentException(interval);
                    }
                    result += count * INTERVAL_SIZES[unit];
                    flags[unit] = true;
                    count = -1;
                }
            }
        }
        
        if (count < 0) {
            return result;
        } else {
            throw new IllegalArgumentException(interval);
        }
    }
    
    /**
     * Returns string representation of the specified time interval. The format
     * is <code>*w *d *h *m *s</code> (representing weeks, days, hours, minutes
     * and seconds - where <code>*</code> is positive number).
     * 
     * @param interval Time interval in milliseconds.
     * @return String representation of the specified time interval.
     * @throws IllegalArgumentException if the specified time interval is
     *         negative.
     */
    public static String encodeInterval(long interval) {
        if (interval < 0L) {
            throw new IllegalArgumentException(Long.toString(interval));
        }
        
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < INTERVAL_SIZES.length; i++) {
            long size = INTERVAL_SIZES[i];
            if (interval >= size) {
                result.append(' ').append(interval / size).append(INTERVAL_UNITS.charAt(i));
                interval %= size;
            }
        }
        
        if (result.length() > 0) {
            return result.substring(1);
        } else {
            return "0" + INTERVAL_UNITS.charAt(INTERVAL_UNITS.length() - 1);
        }
    }
    
    /**
     * Returns string representation of the specified time interval. This
     * method differs from the {@link #encodeInterval(long)} in that it renders
     * milliseconds.
     * 
     * @param duration Time interval in milliseconds.
     * @return String representation of the specified time interval.
     * @throws IllegalArgumentException if the specified time interval is
     *         negative.
     * @see #encodeInterval(long)
     */
    public static String encodeDuration(long duration) {
        if (duration < 0L) {
            throw new IllegalArgumentException(Long.toString(duration));
        }
        
        long millis = duration % 60000L;
        long interval = duration / 60000L * 60000L;
        String seconds = (millis / 1000L) + "." + (millis % 1000L) + "s";
        
        if (interval == 0L) {
            return seconds;
        } else {
            return encodeInterval(interval) + " " + seconds;
        }
    }
    
    // Time interval constants
    private static final String INTERVAL_UNITS = "wdhms";
    private static final long[] INTERVAL_SIZES = new long[]{604800000L, 86400000L, 3600000L, 60000L, 1000L};
    
}
