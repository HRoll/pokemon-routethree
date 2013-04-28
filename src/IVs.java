public class IVs {
    private int hp;
    private int atk;
    private int def;
    private int spcAtk;
    private int spcDef;
    private int spe;
     
    public IVs() {
        hp = 0;
        atk = 0;
        def = 0;
        spcAtk = 0;
        spcDef = 0;
        spe = 0;
    }
    
    public IVs(int newHP, int newAtk, int newDef, int newSpA, int newSpD, int newSpe) {
        hp = newHP;
        atk = newAtk;
        def = newDef;
        spcAtk = newSpA;
        spcDef = newSpD;
        spe = newSpe;
    }
    public IVs(int iv) {
        this(iv,iv,iv,iv,iv,iv);
    }
    
    public int getHPIV() {
        return hp;
    }
    public int getAtkIV() {
        return atk;
    }
    public int getDefIV() {
        return def;
    }
    public int getSpcAtkIV() {
        return spcAtk;
    }
    public int getSpcDefIV() {
        return spcDef;
    }
    public int getSpeIV() {
        return spe;
    }
}
