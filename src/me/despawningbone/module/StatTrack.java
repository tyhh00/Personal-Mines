package me.despawningbone.module;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

enum DURATION_INTERVAL {
	HOURLY,
	DAILY,
	WEEKLY,
	MONTHLY,
	YEARLY,
	CUSTOM,
}

public class StatTrack implements Serializable {

	private static final long serialVersionUID = 5996180937105692064L;
	long statCount;
	Long startDate;
	
	final DURATION_INTERVAL interval;
	int totalDuration;
	
	boolean autoResetAtInterval;
	boolean active;
	
	boolean trackHistory;
	int trackHistoryMaxCount;
	ArrayList<StatHistory> statHistory;
	
	public StatTrack(int startCount, DURATION_INTERVAL interval, boolean autoResetInterval)
	{
		this.statCount = startCount;
		this.startDate = getStartOfIntervalTime(interval);
		this.active = true;
		if(interval != DURATION_INTERVAL.CUSTOM)
			this.totalDuration = getDurationOfInterval(interval); // in seconds

		else
			this.totalDuration = 86400; // random non-zero duration interval
		
		this.interval = interval;
		this.autoResetAtInterval = autoResetInterval;
		
		this.trackHistory = true;
		this.trackHistoryMaxCount = 3;
	}
	
	public void addCount(int count)
	{
		if(active)
		{
			//Will only reset counter if intervals are enabled
			if(autoResetAtInterval && System.currentTimeMillis() > this.startDate + (this.totalDuration * 1000))
			{
				//Start new tracking
				
				//Add to history
				if(statHistory == null)
					statHistory = new ArrayList<StatHistory>();
				if(statHistory.size() >= this.trackHistoryMaxCount)
				{
					statHistory.remove(0); //Remove the oldest record
				}
				statHistory.add(new StatHistory(this.startDate, this.startDate + (this.totalDuration * 1000), this.statCount));
				
				this.statCount = 0;
				this.startDate = getStartOfIntervalTime(interval);
				
			}
			this.statCount += count;	
		}
	}
	
	public void resetAtInterval(boolean toReset)
	{
		this.autoResetAtInterval = toReset;
	}
	
	public long getCount()
	{
		return this.statCount;
	}
	
	public Long getTrackerStartDate(Long startDate)
	{
		return startDate;
	}
	
	public boolean setCustomIntervalTime(int durationInSeconds)
	{
		if(this.interval == DURATION_INTERVAL.CUSTOM)
		{
			this.totalDuration = durationInSeconds;
			return true;
		}
		return false;
	}
	
	public void setActive(boolean active)
	{
		this.active = active;
	}
	
	public boolean isActive()
	{
		return this.active;
	}
	
	public static int getDurationOfInterval(DURATION_INTERVAL interval)
	{
		switch(interval)
		{
		case HOURLY:
			return 3600;
		case DAILY:
			return 86400;
		case WEEKLY:
			return 604800;
		case MONTHLY:
			Calendar cal = Calendar.getInstance();
			return cal.getActualMaximum(Calendar.DAY_OF_MONTH) * 86400;
		case YEARLY:
			return 31536000;
		default:
			return 0;
		}
	}
	
	public static Long getStartOfIntervalTime(DURATION_INTERVAL interval)
	{
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("EST"));
		cal.setTime(new Date(System.currentTimeMillis()));
		
		
		switch(interval)
		{
		case HOURLY:
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			break;
			
		case DAILY:
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			break;
			
		case WEEKLY:
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			break;
			
		case MONTHLY:
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			break;
			
		case YEARLY:
			cal.set(Calendar.DAY_OF_YEAR, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			break;
		}
		
		return cal.getTimeInMillis();
	}
	
}
