package me.despawningbone.module.Utils;

public class LoreBeauty {
	public static String percentToBar(double percent, int bars, char unfinishedColor, char finishedColor) {
		double finalPercent = percent;
		if(percent > 100) percent = 100;
		int n = (int) Math.round(percent / 100 * bars); 
		String init = n <= 0 ? "§"+unfinishedColor+" §l|" : "§"+finishedColor+" §l|";
		for(int i = 1; i < bars; i++) {
			String bar = i == n ? "§"+unfinishedColor+"§l|" : "|";
			init += bar;
		}
		return init + " §8(§7" + NumberBeauty.formatNumber(finalPercent) + "%§8)";
	}
	
	public static String toDisplayCase(String s) {

	    final String ACTIONABLE_DELIMITERS = " '-/"; // these cause the character following
	                                                 // to be capitalized
	    
	    StringBuilder sb = new StringBuilder();
	    boolean capNext = true;

	    for (char c : s.toCharArray()) {
	        c = (capNext)
	                ? Character.toUpperCase(c)
	                : Character.toLowerCase(c);
	        sb.append(c);
	        capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit cast not needed
	    }
	    return sb.toString();
	}
}
