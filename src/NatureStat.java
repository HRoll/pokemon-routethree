public enum NatureStat {
    Atk(0),
    Def(1),
    Spe(2),
    SpA(3),
    SpD(4),
    None(-1);
    
    private final int val;
    private NatureStat(int v) {val = v;}
    public int getVal() {return val;}
    public static NatureStat getNatureStat(int v) {
        switch(v) {
        case 0: return Atk;
        case 1: return Def;
        case 2: return Spe;
        case 3: return SpA;
        case 4: return SpD;
        default: return None;
        }
    }
}