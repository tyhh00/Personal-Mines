package me.despawningbone.module;

import java.io.Serializable;

public class StatHistory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5579504029116585880L;
	public final Long value;
	public final Long start,end;
	public StatHistory(Long start, Long end, Long value)
	{
		this.value = value;
		this.start = start;
		this.end = end;
	}
}
