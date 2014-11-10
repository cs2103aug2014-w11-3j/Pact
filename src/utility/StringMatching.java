package utility;

public class StringMatching {
	
	//@author A0113012J
	private void initializeDpTable(int dp[][],String s,String t) {
		for (int i = 0; i <= s.length(); ++i) {
            for (int j = 0; j <= t.length(); ++j) {
            	//set dp[][] to sufficiently large number
            	//edit distance of two strings must not be greater than
            	//the maximum length
            	dp[i][j] = Math.max(s.length(), t.length());
            }
        }
	}
	
	/**
	 * Compute the Edit Distance of two strings
	 * @param s first string to be compared
	 * @param t second string to be compared
	 * @return the edit distance between two strings
	 */
    public int computeEditDistance(String s, String t) {
    	//compute edit distance using standard Dynamic Programming
        int dp[][] = new int[s.length() + 1][t.length() + 1];
        
        initializeDpTable(dp,s,t);
        
        dp[s.length()][t.length()] = 0;
        for (int i = s.length(); i >= 0; --i) {
            for (int j = t.length(); j >= 0; --j) {
                if (i == s.length() && j == t.length()) {
                    continue;
                }
                if (i < s.length()) {
                    dp[i][j] = Math.min(dp[i][j], dp[i + 1][j] + 1);
                }
                if (j < t.length()) {
                    dp[i][j] = Math.min(dp[i][j], dp[i][j + 1] + 1);
                }
                if (i < s.length() && j < t.length()) {
                    if (s.charAt(i) == t.charAt(j)) {
                        dp[i][j] = Math.min(dp[i][j], dp[i + 1][j + 1]);
                    } else {
                        dp[i][j] = Math.min(dp[i][j], dp[i + 1][j + 1] + 1);
                    }
                }
            }
        }
        return dp[0][0];
    }
}
