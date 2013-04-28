//represents a battle, with planned statmods
public class Battle extends GameAction {
    private Battleable opponent;
    private BattleOptions options;

    public Battle(Battleable b) {
        opponent = b;
        options = new BattleOptions();
    }

    public Battle(Battleable b, BattleOptions options) {
        opponent = b;
        this.options = options;
    }

    public Battleable getOpponent() {
        return opponent;
    }
    public BattleOptions getOptions() {
        return options;
    }

    public StatModifier getMod1() {
        return options.getMod1();
    }

    public StatModifier getMod2() {
        return options.getMod2();
    }

    public int getVerbose() {
        return options.getVerbose();
    }

    public static Battle makeBattle(int offset) {
        return new Battle(Trainer.getTrainer(offset));
    }

    public static Battle makeBattle(int offset, BattleOptions options) {
        return new Battle(Trainer.getTrainer(offset), options);
    }

    public static Battle makeBattle(Pokemon p) {
        return new Battle(p);
    }

    public static Battle makeBattle(Pokemon p, BattleOptions options) {
        return new Battle(p, options);
    }

    @Override
    public void performAction(Pokemon p) {
        doBattle(p);
        int game = Settings.game;
        // check for special gym leader badges
        if(game == Settings.FIRERED) {
            if(opponent.equals(Trainer.getTrainer(414))) {
                p.setAtkBadge(true); //brock
            } else if (opponent.equals(Trainer.getTrainer(416))) {
                p.setSpeBadge(true); //surge
            } else if (opponent.equals(Trainer.getTrainer(418))) {
                p.setDefBadge(true); //koga
            } else if (opponent.equals(Trainer.getTrainer(419))) {
                p.setSpcBadge(true); //blaine
            }
        } else if (game == Settings.RUBY || game == Settings.SAPPHIRE || game == Settings.EMERALD) {
            if(opponent.equals(Trainer.getTrainer(265))) {
                p.setAtkBadge(true); //roxanne
            } else if (opponent.equals(Trainer.getTrainer(267))) {
                p.setSpeBadge(true); //wattson
            } else if (opponent.equals(Trainer.getTrainer(269))) {
                p.setDefBadge(true); //norman
            } else if (opponent.equals(Trainer.getTrainer(271))) {
                p.setSpcBadge(true); //tate&liza
            }
        }
//        if (Trainer.getTrainer("FALKNER").equals(opponent)) {
//            p.setAtkBadge(true);
//        } else if (Trainer.getTrainer("JASMINE").equals(opponent)) {
//            p.setDefBadge(true);
//        } else if (Trainer.getTrainer("WHITNEY").equals(opponent)) {
//            p.setSpdBadge(true);
//        } else if (Trainer.getTrainer("PRYCE").equals(opponent)) {
//            p.setSpcBadge(true);
//        }
    }

    private void doBattle(Pokemon p) {
        // TODO: automatically determine whether or not to print
        if (opponent instanceof Pokemon) {
            if (getVerbose() == BattleOptions.ALL)
                printBattle(p, (Pokemon) opponent);
            else if (getVerbose() == BattleOptions.SOME)
                printShortBattle(p, (Pokemon) opponent);

            opponent.battle(p, options);
        } else { // is a Trainer
            Trainer t = (Trainer) opponent;
            if (getVerbose() == BattleOptions.ALL
                    || getVerbose() == BattleOptions.SOME)
                Main.appendln(t.toString());
            int lastLvl = p.getLevel();
            for (Pokemon opps : t) {
                if (getVerbose() == BattleOptions.ALL)
                    printBattle(p, (Pokemon) opps);
                else if (getVerbose() == BattleOptions.SOME)
                    printShortBattle(p, (Pokemon) opps);
                opps.battle(p, options);
                // test if you leveled up on this pokemon
                if (p.getLevel() > lastLvl) {
                    lastLvl = p.getLevel();
                    if (options.isPrintSRsOnLvl()) {
                        Main.appendln(p.statRanges(false));
                    }
                    if (options.isPrintSRsBoostOnLvl()) {
                        Main.appendln(p.statRanges(true));
                    }
                }
            }
        }
        if (getVerbose() == BattleOptions.ALL
                || getVerbose() == BattleOptions.SOME) {
            Main.appendln(String.format("LVL: %d EXP NEEDED: %d/%d",
                    p.getLevel(), p.expToNextLevel(), p.expForLevel()));
        }
    }

    // does not actually do the battle, just prints summary
    public void printBattle(Pokemon us, Pokemon them) {
        Main.appendln(DamageCalculator.summary(us, them, options));
    }

    // does not actually do the battle, just prints short summary
    public void printShortBattle(Pokemon us, Pokemon them) {
        Main.appendln(DamageCalculator.shortSummary(us, them, options));
    }
}

class Encounter extends Battle {
    Encounter(Species s, int lvl, BattleOptions options) {
        super(new Pokemon(s, lvl), options);
    }

    Encounter(String s, int lvl) {
        this(Species.getSpeciesFromName(s), lvl, new BattleOptions());
    }

    Encounter(String s, int lvl, BattleOptions options) {
        this(Species.getSpeciesFromName(s), lvl, options);
    }
}

class TrainerPoke extends Battle {
    TrainerPoke(Species s, int lvl, BattleOptions options) {
        super(new Pokemon(s, lvl), options);
        ((Pokemon) getOpponent()).setWild(true);
    }

    TrainerPoke(String s, int lvl) {
        this(Species.getSpeciesFromName(s), lvl, new BattleOptions());
    }

    TrainerPoke(String s, int lvl, BattleOptions options) {
        this(Species.getSpeciesFromName(s), lvl, options);
    }
}
