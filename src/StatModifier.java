//the in-battle stat modifiers to a pokemon (caused by e.g. x attack)
public class StatModifier {
    // int from -6 to +6 that represents the stage
    private int atk = 0;
    private int def = 0;
    private int spe = 0;
    private int spcAtk = 0;
    private int spcDef = 0;

    public StatModifier() {
    }

    public StatModifier(int atk, int def, int spcAtk, int spcDef, int spe) {
        this.atk = atk;
        this.def = def;
        this.spcAtk = spcAtk;
        this.spcDef = spcDef;
        this.spe = spe;
    }


    // used to keep the stage between -6 and +6
    private static int bound(int stage) {
        if (stage < -6)
            return -6;
        else if (stage > 6)
            return 6;
        else
            return stage;
    }

    // multiplier for atk,def,spa,spd,spe
    private static int applyStage(int stat, int stage) {
        stage = bound(stage);
        if (stage >= 0)
            return stat * (2 + stage) / 2;
        else
            return stat * 2 / (2 - stage);
    }

    public int getAtkStage() {
        return atk;
    }
    public void setAtkStage(int atk) {
        this.atk = bound(atk);
    }
    public int getDefStage() {
        return def;
    }
    public void setDefStage(int def) {
        this.def = bound(def);
    }
    public int getSpcAtkStage() {
        return spcAtk;
    }
    public void setSpcAtkStage(int spa) {
        this.spcAtk = bound(spa);
    }
    public int getSpcDefStage() {
        return spcDef;
    }
    public void setSpcDefStage(int spd) {
        this.spcDef = bound(spd);
    }
    public int getSpeStage() {
        return spe;
    }
    public void setSpeStage(int spe) {
        this.spe = bound(spe);
    }
    public void incrementAtkStage() {
        incrementAtkStage(1);
    }
    public void incrementDefStage() {
        incrementDefStage(1);
    }
    public void incrementSpcAtkStage() {
        incrementSpcAtkStage(1);
    }
    public void incrementSpcDefStage() {
        incrementSpcDefStage(1);
    }
    public void incrementSpeStage() {
        incrementSpeStage(1);
    }
    public void incrementAtkStage(int i) {
        setAtkStage(getAtkStage() + i);
    }
    public void incrementDefStage(int i) {
        setDefStage(getDefStage() + i);
    }
    public void incrementSpcAtkStage(int i) {
        setSpcAtkStage(getSpcAtkStage() + i);
    }
    public void incrementSpcDefStage(int i) {
        setSpcDefStage(getSpcDefStage() + i);
    }
    public void incrementSpeStage(int i) {
        setSpeStage(getSpeStage() + i);
    }

    public String summary() {
        if (hasMods()) {
            return String.format("+[%s/%s/%s/%s/%s]", atk, def, spcAtk, spcDef, spe);
        } else {
            return "";
        }
    }
    public boolean hasMods() {
        return atk != 0 || def != 0 || spcAtk != 0 || spcDef != 0 || spe != 0;
    }

    public int modAtk(int stat) {
        return Math.max(applyStage(stat,atk), 1);       
    }
    public int modDef(int stat) {
        return Math.max(applyStage(stat,def), 1);  
    }
    public int modSpcAtk(int stat) {
        return Math.max(applyStage(stat,spcAtk), 1);  
    }
    public int modSpcDef(int stat) {
        return Math.max(applyStage(stat,spcDef), 1);  
    }
    public int modSpe(int stat) {
        return Math.max(applyStage(stat,spe), 1);  
    }
    
    public String modStatsStr(Pokemon p) {
        return String.format("%s/%s/%s/%s/%s/%s", p.getHP(),
               modAtk(p.getAtk()), modDef(p.getDef()),
               modSpcAtk(p.getSpcAtk()), modSpcDef(p.getSpcDef()),
               modSpe(p.getSpe()));
    }

}
