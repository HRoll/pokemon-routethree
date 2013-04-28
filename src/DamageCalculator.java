//calculates damage (durr)
public class DamageCalculator {
    private static int MIN_RANGE = 85;
    private static int MAX_RANGE = 100;

    // rangeNum should range from 85 to 100
    // crit indicates if there is a crit or not
    private static int damage(Move attack, Pokemon attacker, Pokemon defender,
            StatModifier atkMod, StatModifier defMod, int rangeNum,
            boolean crit, double basePowerMultiplier) {
        if (rangeNum < MIN_RANGE) {
            rangeNum = MIN_RANGE;
        }
        if (rangeNum > MAX_RANGE) {
            rangeNum = MAX_RANGE;
        }

        int power = attack.getPower();
        if (power <= 0) {
            // TODO: special cases
            return 0;
        }
        power *= basePowerMultiplier;
        
        // stat modifiers
        // TODO: is this how it works in gen 3+?
        //int aa_orig = attacker.getTrueAtk();
        int atk_phy = attacker.getAtk();
        //int dd_orig = defender.getTrueDef();
        int def_phy = defender.getDef();
        //int as_orig = attacker.getTrueSpcAtk();
        int atk_spc = attacker.getSpcAtk();
        //int ds_orig = defender.getTrueSpcDef();
        int def_spc = defender.getSpcDef();

        //type-based hold items
        String itemName = (attacker.getHoldItem() == null ? "" : Constants.hashName(attacker.getHoldItem().getName()));
        if (itemName.equals("SILKSCARF") && attack.getType() == Type.NORMAL) {
            atk_phy = atk_phy * 11 / 10;
        } else if (itemName.equals("BLACKBELT") && attack.getType() == Type.FIGHTING) {
            atk_phy = atk_phy * 11 / 10;
        } else if (itemName.equals("SHARPBEAK") && attack.getType() == Type.FLYING) {
            atk_phy = atk_phy * 11 / 10;
        } else if (itemName.equals("POISONBARB") && attack.getType() == Type.POISON) {
            atk_phy = atk_phy * 11 / 10;
        } else if (itemName.equals("SOFTSAND") && attack.getType() == Type.GROUND) {
            atk_phy = atk_phy * 11 / 10;
        } else if (itemName.equals("HARDSTONE") && attack.getType() == Type.ROCK) {
            atk_phy = atk_phy * 11 / 10;
        } else if (itemName.equals("SILVERPOWDER") && attack.getType() == Type.BUG) {
            atk_phy = atk_phy * 11 / 10;
        } else if (itemName.equals("SPELLTAG") && attack.getType() == Type.GHOST) {
            atk_phy = atk_phy * 11 / 10;
        } else if (itemName.equals("METALCOAT") && attack.getType() == Type.STEEL) {
            atk_phy = atk_phy * 11 / 10;
        } else if (itemName.equals("CHARCOAL") && attack.getType() == Type.FIRE) {
            atk_spc = atk_spc * 11 / 10;
        } else if (itemName.equals("MYSTICWATER") && attack.getType() == Type.WATER) {
            atk_spc = atk_spc * 11 / 10;
        } else if (itemName.equals("MIRACLESEED") && attack.getType() == Type.GRASS) {
            atk_spc = atk_spc * 11 / 10;
        } else if (itemName.equals("MAGNET") && attack.getType() == Type.ELECTRIC) {
            atk_spc = atk_spc * 11 / 10;
        } else if (itemName.equals("TWISTEDSPOON") && attack.getType() == Type.PSYCHIC) {
            atk_spc = atk_spc * 11 / 10;
        } else if (itemName.equals("NEVERMELTICE") && attack.getType() == Type.ICE) {
            atk_spc = atk_spc * 11 / 10;
        } else if (itemName.equals("DRAGONFANG") && attack.getType() == Type.DRAGON) {
            atk_spc = atk_spc * 11 / 10;
        } else if (itemName.equals("BLACKGLASSES") && attack.getType() == Type.DARK) {
            atk_spc = atk_spc * 11 / 10;
        } 
        //TODO: other hold items    
        //TODO: Thick Fat, Hustle, Guts, Marvel Scale
        //TODO: Mud Sport, Water Sport? (Affect base power)
        
        //selfdestruct/explosion halves defense
        if (attack.getName().equalsIgnoreCase("EXPLOSION") || attack.getName().equalsIgnoreCase("SELFDESTRUCT")) {
            def_phy = Math.max(def_phy/2, 1);
        }
        
        //apply stages, choosing physical/special side
        int effective_atk = 0, effective_def = 0;
        if (attack.isPhysicalMove()) {
            effective_atk = (!crit || atkMod.getAtkStage() >= 0) ? atkMod.modAtk(atk_phy) : atk_phy;
            effective_def = (!crit || defMod.getDefStage() <= 0) ? defMod.modDef(def_phy) : def_phy;

        } else {
            effective_atk = (!crit || atkMod.getSpcAtkStage() >= 0) ? atkMod.modSpcAtk(atk_spc) : atk_spc;
            effective_def = (!crit || defMod.getSpcDefStage() <= 0) ? defMod.modSpcDef(def_spc) : def_spc;
        }
        int damage = (int) ((attacker.getLevel() * 0.4) + 2)
                * (effective_atk) * power / 50 / (effective_def);
        
        if(attack.isPhysicalMove()) {
            damage = Math.max(damage, 1);
        }
        damage += 2;
        damage *= crit ? 2 : 1;
        
        boolean STAB = attack.getType() == attacker.getSpecies().getType1()
                || attack.getType() == attacker.getSpecies().getType2();
        double effectiveMult = Type.effectiveness(attack.getType(), defender
                .getSpecies().getType1(), defender.getSpecies().getType2());
        if (effectiveMult == 0) {
            return 0;
        }
        
        if (STAB) {
            damage = damage * 3 / 2;
        }
        damage *= effectiveMult;
        damage = damage * rangeNum / 100;
        return Math.max(damage, 1);
    }

    public static int minDamage(Move attack, Pokemon attacker,
            Pokemon defender, StatModifier atkMod, StatModifier defMod,
            int basePowerMultiplier) {
        return damage(attack, attacker, defender, atkMod, defMod, MIN_RANGE,
                false, basePowerMultiplier);
    }

    public static int maxDamage(Move attack, Pokemon attacker,
            Pokemon defender, StatModifier atkMod, StatModifier defMod,
            int basePowerMultiplier) {
        return damage(attack, attacker, defender, atkMod, defMod, MAX_RANGE,
                false, basePowerMultiplier);
    }

    public static int minCritDamage(Move attack, Pokemon attacker,
            Pokemon defender, StatModifier atkMod, StatModifier defMod,
            int basePowerMultiplier) {
        return damage(attack, attacker, defender, atkMod, defMod, MIN_RANGE,
                true, basePowerMultiplier);
    }

    public static int maxCritDamage(Move attack, Pokemon attacker,
            Pokemon defender, StatModifier atkMod, StatModifier defMod,
            int basePowerMultiplier) {
        return damage(attack, attacker, defender, atkMod, defMod, MAX_RANGE,
                true, basePowerMultiplier);
    }

    // printout of move damages between the two pokemon
    // assumes you are p1
    public static String summary(Pokemon p1, Pokemon p2, BattleOptions options) {
        StringBuilder sb = new StringBuilder();
        String endl = Constants.endl;
        StatModifier mod1 = options.getMod1();
        StatModifier mod2 = options.getMod2();

        sb.append(p1.levelName() + " vs " + p2.levelName() + endl);
        // sb.append(String.format("EXP to next level: %d EXP gained: %d",
        // p1.expToNextLevel(), p2.expGiven()) + endl);
        sb.append(pokeStatMods(p1, mod1));

        sb.append(summary_help(p1, p2, mod1, mod2));

        sb.append(endl);

        sb.append(pokeStatMods(p2, mod2));
        
        sb.append(summary_help(p2, p1, mod2, mod1));

        return sb.toString();
    }
    
    // used for the less verbose option
    public static String shortSummary(Pokemon p1, Pokemon p2,
            BattleOptions options) {
        StringBuilder sb = new StringBuilder();
        String endl = Constants.endl;

        StatModifier mod1 = options.getMod1();
        StatModifier mod2 = options.getMod2();

        sb.append(p1.levelName() + " vs " + p2.levelName() + endl);
        // sb.append(String.format("EXP to next level: %d EXP gained: %d",
        // p1.expToNextLevel(), p2.expGiven()) + endl);
        sb.append(pokeStatMods(p1, mod1));
        
        sb.append(summary_help(p1, p2, mod1, mod2) + endl);

        sb.append(pokeStatMods(p2, mod2));

        sb.append(" " + p2.getMoveset().toString() + endl);
        return sb.toString();
    }
    
    private static String pokeStatMods(Pokemon p, StatModifier sm) {
        StringBuilder sb = new StringBuilder();
        String endl = Constants.endl;
        sb.append(p.pokeName()+ " ");
        
        if (sm.hasMods()) {
            sb.append(String.format("(%s) %s -> (%s) ",
                    p.statsStr(), sm.summary(), sm.modStatsStr(p)));
        } else {
            sb.append(String.format("(%s) ", p.statsStr()));
        }
        sb.append("{" + p.getNature().toString() + "} ");
        sb.append("[" + p.getAbility() + "]");
        if(p.getHoldItem() == null || p.getHoldItem().getName().isEmpty()) {
            sb.append(": " + endl);
        } else {
            sb.append(" <" + p.getHoldItem().toString() + ">: " + endl);
        }
        return sb.toString();
    }

    // String summary of all of p1's moves used on p2
    // (would be faster if i didn't return intermediate strings)
    private static String summary_help(Pokemon p1, Pokemon p2,
            StatModifier mod1, StatModifier mod2) {
        StringBuilder sb = new StringBuilder();
        String endl = Constants.endl;

        int enemyHP = p2.getHP();

        for (Move m : p1.getMoveset()) {
            //rollout, fury cutter
            if (m.getIndexNum() == 205 || m.getIndexNum() == 210) {
                for (int i = 1; i <= 5; i++) {
                    Move m2 = new Move(m, i);
                    printMoveDamage(sb, m2, p1, p2, mod1, mod2, endl, enemyHP,
                            1 << (i - 1));
                }
            } else if (m.getIndexNum() == 99) { //rage
                for (int i = 1; i <= 8; i++) {
                    Move m2 = new Move(m, i);
                    printMoveDamage(sb, m2, p1, p2, mod1, mod2, endl, enemyHP,
                            i);
                }
            } else if (m.getType() == Type.WATER && m.getPower() > 0 && p1.getAbility().equalsIgnoreCase("TORRENT")) {
                printMoveDamage(sb, m, p1, p2, mod1, mod2, endl, enemyHP, 1);
                Move m2 = new Move(m, "(w/ Torrent)", m.getPower() * 3 /2);
                printMoveDamage(sb, m2, p1, p2, mod1, mod2, endl, enemyHP, 1);
            } else if (m.getType() == Type.FIRE && m.getPower() > 0 && p1.getAbility().equalsIgnoreCase("BLAZE")) {
                printMoveDamage(sb, m, p1, p2, mod1, mod2, endl, enemyHP, 1);
                Move m2 = new Move(m, "(w/ Blaze)", m.getPower() * 3 /2);
                printMoveDamage(sb, m2, p1, p2, mod1, mod2, endl, enemyHP, 1);
            } else if (m.getType() == Type.GRASS && m.getPower() > 0 && p1.getAbility().equalsIgnoreCase("OVERGROW")) {
                printMoveDamage(sb, m, p1, p2, mod1, mod2, endl, enemyHP, 1);
                Move m2 = new Move(m, "(w/ Overgrow)", m.getPower() * 3 /2);
                printMoveDamage(sb, m2, p1, p2, mod1, mod2, endl, enemyHP, 1);
            } else if (m.getType() == Type.BUG && m.getPower() > 0 && p1.getAbility().equalsIgnoreCase("SWARM")) {
                printMoveDamage(sb, m, p1, p2, mod1, mod2, endl, enemyHP, 1);
                Move m2 = new Move(m, "(w/ Swarm)", m.getPower() * 3 /2);
                printMoveDamage(sb, m2, p1, p2, mod1, mod2, endl, enemyHP, 1);
            }
            else {
                printMoveDamage(sb, m, p1, p2, mod1, mod2, endl, enemyHP, 1);
            }

        }

        return sb.toString();
    }

    public static void printMoveDamage(StringBuilder sb, Move m, Pokemon p1,
            Pokemon p2, StatModifier mod1, StatModifier mod2, String endl,
            int enemyHP, int basePowerMultiplier) {
        sb.append(m.getName() + "\t");
        // calculate damage of this move, and its percentages on opposing
        // pokemon
        int minDmg = minDamage(m, p1, p2, mod1, mod2, basePowerMultiplier);
        int maxDmg = maxDamage(m, p1, p2, mod1, mod2, basePowerMultiplier);

        // don't spam if the move doesn't do damage
        // TODO: better test of damaging move, to be done when fixes are made
        if (maxDmg == 0) {
            sb.append(endl);
            return;
        }
        double minPct = 100.0 * minDmg / enemyHP;
        double maxPct = 100.0 * maxDmg / enemyHP;
        sb.append(String.format("%d-%d %.02f-%.02f", minDmg, maxDmg, minPct,
                maxPct));
        sb.append("%\t(crit: ");
        // do it again, for crits
        int critMinDmg = minCritDamage(m, p1, p2, mod1, mod2, basePowerMultiplier);
        int critMaxDmg = maxCritDamage(m, p1, p2, mod1, mod2, basePowerMultiplier);

        double critMinPct = 100.0 * critMinDmg / enemyHP;
        double critMaxPct = 100.0 * critMaxDmg / enemyHP;
        sb.append(String.format("%d-%d %.02f-%.02f", critMinDmg, critMaxDmg,
                critMinPct, critMaxPct));
        sb.append("%)" + endl);

        int oppHP = p2.getHP();
        // test if noncrits can kill in 1shot
        if (maxDmg >= oppHP && minDmg < oppHP) {
            double oneShotPct = oneShotPercentage(m, p1, p2, mod1, mod2, false,
                    basePowerMultiplier);
            sb.append(String.format("\t(One shot prob.: %.02f%%)", oneShotPct)
                    + endl);
        }
        // test if crits can kill in 1shot
        if (critMaxDmg >= oppHP && critMinDmg < oppHP) {
            double oneShotPct = oneShotPercentage(m, p1, p2, mod1, mod2, true,
                    basePowerMultiplier);
            sb.append(String.format("\t(Crit one shot prob.: %.02f%%)",
                    oneShotPct) + endl);
        }
    }

    private static double oneShotPercentage(Move attack, Pokemon attacker,
            Pokemon defender, StatModifier atkMod, StatModifier defMod,
            boolean crit, int basePowerMultiplier) {
        // iterate until damage is big enough
        int rangeNum = MIN_RANGE;
        while (damage(attack, attacker, defender, atkMod, defMod, rangeNum,
                crit, basePowerMultiplier) < defender.getHP()) {
            rangeNum++;
        }
        return 100.0 * (MAX_RANGE - rangeNum + 1) / (MAX_RANGE - MIN_RANGE + 1);
    }
}
