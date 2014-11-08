package utility;

public class StringMatching {
    public int computeEditDistance(String s, String t) {
        int dp[][] = new int[s.length() + 1][t.length() + 1];
        for (int i = 0; i <= s.length(); ++i) {
            for (int j = 0; j <= t.length(); ++j) {
                dp[i][j] = Math.max(s.length(), t.length());
            }
        }
        dp[s.length()][t.length()] = 0;
        for (int i = s.length(); i >= 0; --i) {
            for (int j = t.length(); j >= 0; --j) {
                if (i == s.length() && j == t.length()) {
                    continue;
                }
                if (i < s.length()) {
                    dp[i][j] = Math.min(dp[i][j], dp[i+1][j] + 1);
                }
                if (j < t.length()) {
                    dp[i][j] = Math.min(dp[i][j], dp[i][j+1] + 1);
                }
                if (i < s.length() && j < t.length()) {
                    if (s.charAt(i) == t.charAt(j)) {
                        dp[i][j] = Math.min(dp[i][j], dp[i+1][j+1]);
                    } else {
                        dp[i][j] = Math.min(dp[i][j], dp[i+1][j+1] + 1);
                    }
                }
            }
        }
        return dp[0][0];
    }
    
    /*public static void main(String[] args) {
        StringMatching sm = new StringMatching();
        int k = sm.computeEditDistance("gajaaah","gajah");
        System.out.println(k);
    }*/
}
