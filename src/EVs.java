public class EVs {
    private int hp;
    private int atk;
    private int def;
    private int spcAtk;
    private int spcDef;
    private int spe;
    
    public EVs() {
        this(0,0,0,0,0,0);
    }
    
    public EVs(int newHP, int newAtk, int newDef, int newSpA, int newSpD, int newSpe) {
        hp = newHP;
        atk = newAtk;
        def = newDef;
        spcAtk = newSpA;
        spcDef = newSpD;
        spe = newSpe;
    }
    
    
    public int getHPEV() {
        return hp;
    }
    public int getAtkEV() {
        return atk;
    }
    public int getDefEV() {
        return def;
    }
    public int getSpcAtkEV() {
        return spcAtk;
    }
    public int getSpcDefEV() {
        return spcDef;
    }
    public int getSpeEV() {
        return spe;
    }
    
    public void gainEVs(Species s) {
        //TODO: account for cases where pokemon has 510 total
        hp += s.getEvHP();
        hp = capEV(hp);
        atk += s.getEvAtk();
        atk = capEV(atk);
        def += s.getEvDef();
        def = capEV(def);
        spe += s.getEvSpe();
        spe = capEV(spe);
        spcAtk += s.getEvSpcAtk();
        spcAtk = capEV(spcAtk);
        spcDef += s.getEvSpcDef();
        spcDef = capEV(spcDef);       
    }
    private int capEV(int ev) {
        return Math.min(255,ev);
    }
    
    //TODO: account for cases where pokemon has 510 total
    public void eatHPUp() {
        hp = Math.min(100, hp + 10);
    }
    public void eatProtein() {
        atk = Math.min(100, atk + 10);
    }
    public void eatIron() {
        def = Math.min(100, def + 10);
    }
    public void eatCalcium() {
        spcAtk = Math.min(100, spcAtk + 10);
    }
    public void eatZinc() {
        spcDef = Math.min(100, spcDef + 10);
    }
    public void eatCarbos() {
        spe = Math.min(100, spe + 10);
    }
}
