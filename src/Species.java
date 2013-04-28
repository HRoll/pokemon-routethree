import java.util.HashMap;


public class Species {
    private String name;
    private ExpCurve curve;
    private Type type1;
    private Type type2;
    private int baseHP;
    private int baseAtk;
    private int baseDef;
    private int baseSpcAtk;
    private int baseSpcDef;
    private int baseSpe;
    private int killExp;
    private int pokedexNum;
    private String ability1;
    private String ability2;
    private int numAbilities;
    private int evHP;
    private int evAtk;
    private int evDef;
    private int evSpcAtk;
    private int evSpcDef;
    private int evSpe;
    private int nameValue;
    
    private static Species[] allSpecies;
    private static final HashMap<String, Species> nameMap;
    
    public Species(String s_name, ExpCurve s_curve, Type s_type1, Type s_type2,
            int s_baseHP, int s_baseAtk, int s_baseDef, int s_baseSpcAtk, int s_baseSpcDef, int s_baseSpe,
            int s_killExp, int s_pokedexNum, String s_ability1, String s_ability2, int s_numAbilities,
            int s_evHP, int s_evAtk, int s_evDef, int s_evSpcAtk, int s_evSpcDef, int s_evSpe, int s_nameValue) {
        name = s_name;
        curve = s_curve;
        type1 = s_type1;
        type2 = s_type2;
        baseHP = s_baseHP;
        baseAtk = s_baseAtk;
        baseDef = s_baseDef;
        baseSpcAtk = s_baseSpcAtk;
        baseSpcDef = s_baseSpcDef;
        baseSpe = s_baseSpe;
        killExp = s_killExp;
        pokedexNum = s_pokedexNum;
        ability1 = s_ability1;
        ability2 = s_ability2;
        numAbilities = s_numAbilities;
        evHP = s_evHP;
        evAtk = s_evAtk;
        evDef = s_evDef;
        evSpcAtk = s_evSpcAtk;
        evSpcDef = s_evSpcDef;
        evSpe = s_evSpe;
        nameValue = s_nameValue;
    }
    
    //initialize allSpecies to be a list of all species
    static {
        allSpecies = new Species[Constants.numPokes + 1];
        nameMap = new HashMap<String, Species>();
        Species s; Type[] ts;
        for(int i = 0; i < allSpecies.length; i++) {
            //s = new Species();
            String s_name = Constants.poke_names[i];
            ExpCurve s_curve = Constants.exp_curves[i];
            ts = Constants.poke_types[i];
            Type s_type1, s_type2;
            if(ts.length == 0) {
                s_type1 = Type.NONE;
                s_type2 = Type.NONE;
            } else if(ts.length == 1) {
                s_type1 = ts[0];
                s_type2 = Type.NONE;
            } else {
                s_type1 = ts[0];
                s_type2 = ts[1];
            }
            int s_baseHP = Constants.basestats[i][0];
            int s_baseAtk = Constants.basestats[i][1];
            int s_baseDef = Constants.basestats[i][2];
            int s_baseSpcAtk = Constants.basestats[i][3];
            int s_baseSpcDef = Constants.basestats[i][4];
            int s_baseSpd = Constants.basestats[i][5];
            int s_killExp = Constants.killexps[i];
            int s_pokedexNum = i;
            String[] abilities = Constants.poke_abilities[i];
            String s_ability1 = abilities[0];
            String s_ability2 = abilities.length > 1 ? abilities[1] : "";
            int s_numAbilities = abilities.length > 1 ? 2 : 1;
            int s_evHP = Constants.evs[i][0];
            int s_evAtk = Constants.evs[i][1];
            int s_evDef = Constants.evs[i][2];
            int s_evSpd = Constants.evs[i][3];
            int s_evSpcAtk = Constants.evs[i][4];
            int s_evSpcDef = Constants.evs[i][5];
            int s_nameValue = Constants.pokeNameVals[i];
            
            s = new Species(s_name, s_curve, s_type1, s_type2, s_baseHP, s_baseAtk, s_baseDef,
                    s_baseSpcAtk, s_baseSpcDef, s_baseSpd, s_killExp, s_pokedexNum, s_ability1, s_ability2, s_numAbilities,
                    s_evHP, s_evAtk, s_evDef, s_evSpcAtk, s_evSpcDef, s_evSpd, s_nameValue);
            allSpecies[i] = s;
            nameMap.put(Constants.hashName(s.getName()),s);
        }
    }
    
    //returns the species object corresponding to the pokemon with pokedex number i
    public static Species getSpecies(int i) {
        if(i < 0 || i >= allSpecies.length)
            return null;
        return allSpecies[i];
    }
    
    //returns the species with this name, or null if it does not exist
    public static Species getSpeciesFromName(String name) {
        name = Constants.hashName(name);
        if(!nameMap.containsKey(name))
            return null;
        return nameMap.get(name);
    }
    
    public String toString() {
        return String.format("%d %s %s %s%s Stats: %d %d %d %d %d %d Exp: %d EVs: %d %d %d %d %d %d Abilities: %s %s", pokedexNum, name, curve, type1,
                ((type2 == Type.NONE) ? "" : " " + type2), baseHP, baseAtk, baseDef, baseSpe,
                baseSpcAtk, baseSpcDef, killExp, evHP, evAtk, evDef, evSpe, evSpcAtk, evSpcDef, ability1, ability2);
        //return name + " " + curve + " " + type1 + ((type2 == Type.NONE) ? "" : " " + type2) + " ";
    }
    
    public String getName() {
        return name;
    }
    public ExpCurve getCurve() {
        return curve;
    }
    public Type getType1() {
        return type1;
    }
    public Type getType2() {
        return type2;
    }
    public int getBaseHP() {
        return baseHP;
    }
    public int getBaseAtk() {
        return baseAtk;
    }
    public int getBaseDef() {
        return baseDef;
    }
    public int getBaseSpcAtk() {
        return baseSpcAtk;
    }
    public int getBaseSpcDef() {
        return baseSpcDef;
    }
    public int getBaseSpe() {
        return baseSpe;
    }
    public int getKillExp() {
        return killExp;
    }
    public int getPokedexNum() {
        return pokedexNum;
    }
    public String getAbility1() {
        return ability1;
    }
    public String getAbility2() {
        return ability2;
    }
    public int getNumAbilities() {
        return numAbilities;
    }
    public int getEvHP() {
        return evHP;
    }
    public int getEvAtk() {
        return evAtk;
    }
    public int getEvDef() {
        return evDef;
    }
    public int getEvSpcAtk() {
        return evSpcAtk;
    }
    public int getEvSpcDef() {
        return evSpcDef;
    }
    public int getEvSpe() {
        return evSpe;
    }
    public int getNameValue() {
        return nameValue;
    }
}
