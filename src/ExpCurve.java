
public enum ExpCurve {
    SLOW, MEDIUM_SLOW, MEDIUM, FAST, ERRATIC, FLUCTUATING, NONE;
    
    public static int expToNextLevel(ExpCurve curve, int currLevel, int totalExp) {
        if (curve == NONE)
            return 0;
        
        int n = currLevel + 1; //next level
        int nextExp = lowestExpForLevel(curve, n);
        
        return nextExp - totalExp;
    }
    
    public static int lowestExpForLevel(ExpCurve curve, int level) {
        int n = level;
        int exp = 0;
        switch(curve) {
        case SLOW:
            exp = 5*n*n*n/4;
            break;
        case MEDIUM_SLOW:
            exp = 6*n*n*n/5 - 15*n*n + 100*n - 140;
            break;
        case MEDIUM:
            exp = n*n*n;
            break;
        case FAST:
            exp = 4*n*n*n/5;
            break;
        case ERRATIC:
            if(n <= 50) {
                exp = n*n*n*(100-n)/50;
            } else if (n <= 68) {
                exp = n*n*n*(150-n)/100;
            } else if (n <= 98) {
                exp = n*n*n*((1911-10*n)/3)/500;
            } else {
                exp = n*n*n*(160-n)/100;
            }
            break;
        case FLUCTUATING:
            if(n <= 15) {
                exp = n*n*n*(((n+1)/3+24)/50);
            } else if (n <= 36) {
                exp = n*n*n*((n+14)/50);
            } else {
                exp = n*n*n*((n/2 + 32)/50);
            }
            break;
        default:
            break;
        }
        return exp;
    }
    
    //
    public static int expForLevel(ExpCurve curve, int level) {
        return lowestExpForLevel(curve, level + 1) - lowestExpForLevel(curve, level);
    }
}
