
public class Pokemon implements Battleable {
    private Species species;
    private int level;
    private IVs ivs;
    private EVs evs = new EVs();
    private int hp;
    private int atk;
    private int def;
    private int spe;
    private int spcAtk;
    private int spcDef;
    private int totalExp;
    private Moveset moves;
    private boolean wild;
    private boolean atkBadge = false;
    private boolean defBadge = false;
    private boolean speBadge = false;
    private boolean spcBadge = false;
    private Nature nature;
    private Item holdItem = null;
    private String ability;
    
    //wild pokemon, with 0 IVs
    public Pokemon(Species s, int newLevel) {
        this(s, newLevel, Moveset.defaultMoveset(s, newLevel, Settings.game), new IVs(), Nature.getNature(0), true);
    }
    // default moveset based off of species and level, for trainers
    public Pokemon(Species s, int newLevel, IVs ivs, Nature nature) {
        this(s, newLevel, Moveset.defaultMoveset(s, newLevel, Settings.game), ivs, nature, false);
    }   
    // will work for leaders
    public Pokemon(Species s, int newLevel, Moveset moves, IVs ivs, Nature nature) {
        this(s, newLevel, moves, ivs, nature, false);
    }
    public Pokemon(Species s, int newLevel, Moveset moves, IVs ivs, Nature nature, boolean wild) {
        species = s;
        level = newLevel;
        this.moves = moves;
        this.ivs = ivs;
        this.nature = nature;
        this.wild = wild;
        calculateStats();
        setExpForLevel();
        ability = s.getAbility1();
    }
    
    // call this to update your stats
    // automatically called on level ups/rare candies, but not just from gaining
    // stat EV
    public void calculateStats() {
        hp = calcHPWithIV(ivs.getHPIV());
        atk = calcAtkWithIV(ivs.getAtkIV());
        def = calcDefWithIV(ivs.getDefIV());
        spe = calcSpeWithIV(ivs.getSpeIV());
        spcAtk = calcSpcAtkWithIV(ivs.getSpcAtkIV());
        spcDef = calcSpcDefWithIV(ivs.getSpcDefIV());
    }
    
    private int calcHPWithIV(int iv) {
        if(species.equals(Species.getSpeciesFromName("SHEDINJA"))) {
            return 1;
        }
        return calcStatNumerator(iv, species.getBaseHP(), evs.getHPEV()) * level / 100
                + level + 10;
    }
    private int calcAtkWithIV(int iv) {
        int beforeNature = calcStatNumerator(iv, species.getBaseAtk(), evs.getAtkEV())
                           * level / 100 + 5;
        return applyNature(beforeNature, NatureStat.Atk);
    }
    private int calcDefWithIV(int iv) {
        int beforeNature = calcStatNumerator(iv, species.getBaseDef(), evs.getDefEV())
                           * level / 100 + 5;
        return applyNature(beforeNature, NatureStat.Def);
    }
    private int calcSpeWithIV(int iv) {
        int beforeNature = calcStatNumerator(iv, species.getBaseSpe(), evs.getSpeEV())
                           * level / 100 + 5;
        return applyNature(beforeNature, NatureStat.Spe);
    }
    private int calcSpcAtkWithIV(int iv) {
        int beforeNature = calcStatNumerator(iv, species.getBaseSpcAtk(), evs.getSpcAtkEV())
                           * level / 100 + 5;
        return applyNature(beforeNature, NatureStat.SpA);
    }
    private int calcSpcDefWithIV(int iv) {
        int beforeNature = calcStatNumerator(iv, species.getBaseSpcDef(), evs.getSpcDefEV())
                           * level / 100 + 5;
        return applyNature(beforeNature, NatureStat.SpD);
    }
    private int calcStatNumerator(int iv, int base, int ev) {
        return iv + 2*base + ev/4;
    }
    private int applyNature(int beforeNature, NatureStat ns) {
        if(nature.getPlusStat() == ns) {
            return beforeNature * 11 / 10;
        } else if (nature.getMinusStat() == ns) {
            return beforeNature * 9 / 10;
        } else {
            return beforeNature;
        }
    }
    
    private void setExpForLevel() {
        totalExp = ExpCurve.lowestExpForLevel(species.getCurve(), level);
    }
    
    public int getHP() {
        return hp;
    }
    // badge boosts
    public int getAtk() {
        return (int) (atkBadge ? 11 * atk / 10 : atk);
    }
    public int getDef() {
        return (int) (defBadge ? 11 * def / 10 : def);
    }
    public int getSpcAtk() {
        return (int) (spcBadge ? 11 * spcAtk / 10 : spcAtk);
    }
    public int getSpcDef() {
        return (int) (spcBadge ? 11 * spcDef / 10 : spcDef);
    }
    public int getSpe() {
        return (int) (speBadge ? 11 * spe / 10 : spe);
    }
    // not affected by badge boosts
    public int getTrueAtk() {
        return atk;
    }
    public int getTrueDef() {
        return def;
    }
    public int getTrueSpcAtk() {
        return spcAtk;
    }
    public int getTrueSpcDef() {
        return spcDef;
    }
    public int getTrueSpe() {
        return spe;
    }
    public int getLevel() {
        return level;
    }
    public Species getSpecies() {
        return species;
    }
    public void setMoveset(Moveset m) {
        moves = m;
    }
    public Moveset getMoveset() {
        return moves;
    }
    public boolean isWild() {
        return wild;
    }
    public void setWild(boolean isWild) {
        this.wild = isWild;
    }
    public int getTotalExp() {
        return totalExp;
    }
    public Item getHoldItem() {
        return holdItem;
    }
    public void setHoldItem(Item i) {
        holdItem = i;
    }
    public void takeHoldItem() {
        holdItem = null;
    }
    public void setEVs(EVs evs) {
        this.evs = evs;
    }
    public String getAbility() {
        return ability;
    }
    public void setAbility(String s) {
        ability = s;
    }
    public Nature getNature() {
        return nature;
    }
    public void setNature(Nature n) {
        nature = n;
    }

    public int expGiven(int participants) {
        return (species.getKillExp() / participants) * level / 7 * 3
                / (isWild() ? 3 : 2);
    }
    
    public String toString() {
        return shortDescription();
    }
    public String statsPrintout() {
        return statsPrintout(true);
    }
    
    public String statsPrintout(boolean boost) {
        String endl = Constants.endl;
        StringBuilder sb = new StringBuilder();
        sb.append(shortDescription());
        sb.append(String.format("Stats %s badge boosts:", boost ? "WITH" : "WITHOUT") + endl);
        sb.append(String.format("  %1$6s%2$6s%3$6s%4$6s%5$6s%6$6s", "HP",
                natureStatBoost(NatureStat.Atk, boost),
                natureStatBoost(NatureStat.Def, boost),
                natureStatBoost(NatureStat.SpA, boost),
                natureStatBoost(NatureStat.SpD, boost),
                natureStatBoost(NatureStat.Spe, boost))
                + endl);
        if(boost){
            sb.append(String.format("  %1$6s%2$6s%3$6s%4$6s%5$6s%6$6s", getHP(),
                    getAtk(), getDef(), getSpcAtk(), getSpcDef(), getSpe()) + endl);

        } else {
            sb.append(String.format("  %1$6s%2$6s%3$6s%4$6s%5$6s%6$6s", getHP(),
                    getTrueAtk(), getTrueDef(), getTrueSpcAtk(), getTrueSpcDef(),
                    getTrueSpe()) + endl);
        }
        
        sb.append(String.format("IV%1$6s%2$6s%3$6s%4$6s%5$6s%6$6s",
                ivs.getHPIV(), ivs.getAtkIV(), ivs.getDefIV(), ivs.getSpcAtkIV(),
                ivs.getSpcDefIV(), ivs.getSpeIV()) + endl);
        sb.append(String.format("EV%1$6s%2$6s%3$6s%4$6s%5$6s%6$6s", evs.getHPEV(),
                evs.getAtkEV(), evs.getDefEV(), evs.getSpcAtkEV(), evs.getSpcDefEV(),
                evs.getSpeEV()) + endl);      
        sb.append(moves.toString() + endl);
        return sb.toString();
    }
    //returns e.g., "*+SpA"
    private String natureStatBoost(NatureStat ns, boolean boost) {
        String boostPart = "";
        if(ns == NatureStat.Atk) {
            boostPart = (boost && atkBadge) ? "*" : "";
        } else if(ns == NatureStat.Def) {
            boostPart = (boost && defBadge) ? "*" : "";
        } else if(ns == NatureStat.SpA) {
            boostPart = (boost && spcBadge) ? "*" : "";
        } else if(ns == NatureStat.SpD) {
            boostPart = (boost && spcBadge) ? "*" : "";
        } else if(ns == NatureStat.Spe) {
            boostPart = (boost && speBadge) ? "*" : "";
        } 
        
        String naturePart = "";
        if(nature.getPlusStat() == ns) {
            naturePart = "+";
        } else if(nature.getMinusStat() == ns) {
            naturePart = "-";
        }
        
        return boostPart + naturePart + ns.toString();
    }
    private String natureStatBoostReversed(NatureStat ns, boolean boost) {
        String boostPart = "";
        if(ns == NatureStat.Atk) {
            boostPart = (boost && atkBadge) ? "*" : "";
        } else if(ns == NatureStat.Def) {
            boostPart = (boost && defBadge) ? "*" : "";
        } else if(ns == NatureStat.SpA) {
            boostPart = (boost && spcBadge) ? "*" : "";
        } else if(ns == NatureStat.SpD) {
            boostPart = (boost && spcBadge) ? "*" : "";
        } else if(ns == NatureStat.Spe) {
            boostPart = (boost && speBadge) ? "*" : "";
        } 
        
        String naturePart = "";
        if(nature.getPlusStat() == ns) {
            naturePart = "+";
        } else if(nature.getMinusStat() == ns) {
            naturePart = "-";
        }
        
        return ns.toString() + naturePart + boostPart;
    }
    
    // utility getters
    public String levelName() {
        return "L" + level + " " + getSpecies().getName();
    }
    public String pokeName() {
        return getSpecies().getName();
    }
    public String statsStr() {
        return String.format("%s/%s/%s/%s/%s/%s", getHP(), getAtk(), getDef(),
                getSpcAtk(), getSpcDef(), getSpe());
    }
    public String shortDescription() {
        String endl = Constants.endl;
        StringBuilder sb = new StringBuilder();
        sb.append(levelName() + " ");
        sb.append("EXP Needed: " + expToNextLevel() + "/" + expForLevel() + endl);
        sb.append("Nature: " + nature.toString() + " ");
        sb.append("Ability: " + ability + " ");
        if(holdItem == null || holdItem.getName().isEmpty()) {
            sb.append(endl);
        } else {
            sb.append("Item: " + holdItem.toString() + endl);
        }
        return sb.toString();
    }
    
    // experience methods
    // exp needed to get to next level
    public int expToNextLevel() {
        return ExpCurve.expToNextLevel(species.getCurve(), level, totalExp);
    }
    // total exp needed to get from this level to next level (no partial exp)
    public int expForLevel() {
        return ExpCurve.expForLevel(species.getCurve(), level);
    }
    
    // in game actions
    // gain num exp
    private void gainExp(int num) {
        totalExp += num;
        // update lvl if necessary
        while (expToNextLevel() <= 0 && level < 100) {
            level++;
            calculateStats();
        }
    }

    // gain stat exp from a pokemon of species s
    private void gainStatExp(Species s, int participants) {
        //TODO: does participants matter?
        evs.gainEVs(s);
    }
    
    @Override
    public void battle(Pokemon p, BattleOptions options) {
        // p is the one that gets leveled up
        // this is the one that dies like noob
        // be sure to gain EVs before the exp
        p.gainStatExp(this.getSpecies(), options.getParticipants());
        p.gainExp(this.expGiven(options.getParticipants()));
    }

    // gains from eating stat/level boosters
    public void eatRareCandy() {
        if (level < 100) {
            level++;
            setExpForLevel();
            calculateStats();
        }
    }

    //TODO: don't recalculate stats if the food doesn't do anything (evs.eatX should return bool?)
    public void eatHPUp() {
        evs.eatHPUp();
        calculateStats();
    }
    public void eatProtein() {
        evs.eatProtein();
        calculateStats();
    }
    public void eatIron() {
        evs.eatIron();
        calculateStats();
    }
    public void eatCalcium() {
        evs.eatCalcium();
        calculateStats();
    }
    public void eatZinc() {
        evs.eatZinc();
        calculateStats();
    }
    public void eatCarbos() {
        evs.eatCarbos();
        calculateStats();
    }

    // TODO: proper evolution
    public void evolve(Species s) {
        species = s;
        ability = s.getAbility1();
    }
    
    // badge get/set
    public boolean isAtkBadge() {
        return atkBadge;
    }
    public void setAtkBadge(boolean atkBadge) {
        this.atkBadge = atkBadge;
    }
    public boolean isDefBadge() {
        return defBadge;
    }
    public void setDefBadge(boolean defBadge) {
        this.defBadge = defBadge;
    }
    public boolean isSpeBadge() {
        return speBadge;
    }
    public void setSpeBadge(boolean speBadge) {
        this.speBadge = speBadge;
    }
    public boolean isSpcBadge() {
        return spcBadge;
    }
    public void setSpcBadge(boolean spcBadge) {
        this.spcBadge = spcBadge;
    }
    public void setAllBadges() {
        atkBadge = true;
        defBadge = true;
        speBadge = true;
        spcBadge = true;
    }
    public void loseAllBadges() {
        atkBadge = false;
        defBadge = false;
        speBadge = false;
        spcBadge = false;
    }
    // a printout of stat ranges given this pokemon's EVs (not IVs)
    public String statRanges(boolean isBoosted) {
        int[] possibleHPs = new int[32];
        int[] possibleAtks = new int[32];
        int[] possibleDefs = new int[32];
        int[] possibleSpcAtks = new int[32];
        int[] possibleSpcDefs = new int[32];
        int[] possibleSpes = new int[32];
        if (isBoosted) {
            for (int i = 0; i < 32; i++) {
                possibleHPs[i] = calcHPWithIV(i);
                possibleAtks[i] = atkBadge ? 11 * calcAtkWithIV(i) / 10 : calcAtkWithIV(i);
                possibleDefs[i] = defBadge ? 11 * calcDefWithIV(i) / 10 : calcDefWithIV(i);
                possibleSpcAtks[i] = spcBadge ? 11 * calcSpcAtkWithIV(i) / 10 : calcSpcAtkWithIV(i);
                possibleSpcAtks[i] = spcBadge ? 11 * calcSpcDefWithIV(i) / 10 : calcSpcDefWithIV(i);
                possibleSpes[i] = speBadge ? 11 * calcSpeWithIV(i) / 10 : calcSpeWithIV(i);
            }
        } else {
            for (int i = 0; i < 32; i++) {
                possibleHPs[i] = calcHPWithIV(i);
                possibleAtks[i] = calcAtkWithIV(i);
                possibleDefs[i] = calcDefWithIV(i);
                possibleSpcAtks[i] = calcSpcAtkWithIV(i);
                possibleSpcDefs[i] = calcSpcDefWithIV(i);
                possibleSpes[i] = calcSpeWithIV(i);
            }
        }
        StringBuilder sb = new StringBuilder(levelName() + Constants.endl);
        sb.append("Stat ranges " + (isBoosted ? "WITH" : "WITHOUT")
                + " badge boosts:" + Constants.endl);
        sb.append("IV    |0   |1   |2   |3   |4   |5   |6   |7   |8   |9   |10  |11  |12  |13  |14  |15  "
                + Constants.endl
                + "--------------------------------------------------------------------------------------"
                + Constants.endl);
        sb.append("HP    ");
        for (int i = 0; i < 16; i++) {
            sb.append(String.format("|%1$4s", possibleHPs[i])); // pad left,
                                                                // length 4
        }
        sb.append(Constants.endl);
        sb.append(String.format("%1$-6s", natureStatBoostReversed(NatureStat.Atk, isBoosted)));
        for (int i = 0; i < 16; i++) {
            sb.append(String.format("|%1$4s", possibleAtks[i])); // pad left,
                                                                    // length 4
        }
        sb.append(Constants.endl);
        sb.append(String.format("%1$-6s", natureStatBoostReversed(NatureStat.Def, isBoosted)));
        for (int i = 0; i < 16; i++) {
            sb.append(String.format("|%1$4s", possibleDefs[i])); // pad left,
                                                                    // length 4
        }
        sb.append(Constants.endl);
        sb.append(String.format("%1$-6s", natureStatBoostReversed(NatureStat.SpA, isBoosted)));
        for (int i = 0; i < 16; i++) {
            sb.append(String.format("|%1$4s", possibleSpcAtks[i])); // pad left,
                                                                    // length 4
        }
        sb.append(Constants.endl);
        sb.append(String.format("%1$-6s", natureStatBoostReversed(NatureStat.SpD, isBoosted)));
        for (int i = 0; i < 16; i++) {
            sb.append(String.format("|%1$4s", possibleSpcDefs[i])); // pad left,
                                                                    // length 4
        }
        sb.append(Constants.endl);
        sb.append(String.format("%1$-6s", natureStatBoostReversed(NatureStat.Spe, isBoosted)));
        for (int i = 0; i < 16; i++) {
            sb.append(String.format("|%1$4s", possibleSpes[i])); // pad left,
                                                                    // length 4
        }
        sb.append(Constants.endl);
        sb.append(Constants.endl);
        sb.append("IV    |16  |17  |18  |19  |20  |21  |22  |23  |24  |25  |26  |27  |28  |29  |30  |31  "
                + Constants.endl
                + "--------------------------------------------------------------------------------------"
                + Constants.endl);
        sb.append("HP    ");
        for (int i = 16; i < 32; i++) {
            sb.append(String.format("|%1$4s", possibleHPs[i])); // pad left,
                                                                // length 4
        }
        sb.append(Constants.endl);
        sb.append(String.format("%1$-6s", natureStatBoostReversed(NatureStat.Atk, isBoosted)));
        for (int i = 16; i < 32; i++) {
            sb.append(String.format("|%1$4s", possibleAtks[i])); // pad left,
                                                                    // length 4
        }
        sb.append(Constants.endl);
        sb.append(String.format("%1$-6s", natureStatBoostReversed(NatureStat.Def, isBoosted)));
        for (int i = 16; i < 32; i++) {
            sb.append(String.format("|%1$4s", possibleDefs[i])); // pad left,
                                                                    // length 4
        }
        sb.append(Constants.endl);
        sb.append(String.format("%1$-6s", natureStatBoostReversed(NatureStat.SpA, isBoosted)));
        for (int i = 16; i < 32; i++) {
            sb.append(String.format("|%1$4s", possibleSpcAtks[i])); // pad left,
                                                                    // length 4
        }
        sb.append(Constants.endl);
        sb.append(String.format("%1$-6s", natureStatBoostReversed(NatureStat.SpD, isBoosted)));
        for (int i = 16; i < 32; i++) {
            sb.append(String.format("|%1$4s", possibleSpcDefs[i])); // pad left,
                                                                    // length 4
        }
        sb.append(Constants.endl);
        sb.append(String.format("%1$-6s", natureStatBoostReversed(NatureStat.Spe, isBoosted)));
        for (int i = 16; i < 32; i++) {
            sb.append(String.format("|%1$4s", possibleSpes[i])); // pad left,
                                                                    // length 4
        }
        sb.append(Constants.endl);
        
        return sb.toString();
    }
}
