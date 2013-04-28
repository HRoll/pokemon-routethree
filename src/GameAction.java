
public abstract class GameAction {
    abstract void performAction(Pokemon p);
    
    public static final GameAction eatRareCandy = new GameAction() {
        void performAction(Pokemon p) { p.eatRareCandy(); }
    };
    public static final GameAction eatHPUp = new GameAction() {
        void performAction(Pokemon p) { p.eatHPUp(); }
    };
    public static final GameAction eatIron = new GameAction() {
        void performAction(Pokemon p) { p.eatIron(); }
    };
    public static final GameAction eatProtein = new GameAction() {
        void performAction(Pokemon p) { p.eatProtein(); }
    };
    public static final GameAction eatCalcium = new GameAction() {
        void performAction(Pokemon p) { p.eatCalcium(); }
    };
    public static final GameAction eatZinc = new GameAction() {
        void performAction(Pokemon p) { p.eatZinc(); }
    };
    public static final GameAction eatCarbos = new GameAction() {
        void performAction(Pokemon p) { p.eatCarbos(); }
    };
    
    //badges
    public static final GameAction getAtkBadge = new GameAction() {
        void performAction(Pokemon p) { p.setAtkBadge(true); }
    };
    public static final GameAction getSpeBadge = new GameAction() {
        void performAction(Pokemon p) { p.setSpeBadge(true); }
    };
    public static final GameAction getSpcBadge = new GameAction() {
        void performAction(Pokemon p) { p.setSpcBadge(true); }
    };
    public static final GameAction getDefBadge = new GameAction() {
        void performAction(Pokemon p) { p.setDefBadge(true); }
    };
    
    
    //not really a game action, but it's a nice hack?
    public static final GameAction printAllStats = new GameAction() {
        void performAction(Pokemon p) { Main.appendln(p.statsPrintout(true)); }
    };
    public static final GameAction printAllStatsNoBoost = new GameAction() {
        void performAction(Pokemon p) { Main.appendln(p.statsPrintout(false)); }
    };
    public static final GameAction printStatRanges = new GameAction() {
        void performAction(Pokemon p) { Main.appendln(p.statRanges(true)); }
    };
    public static final GameAction printStatRangesNoBoost = new GameAction() {
        void performAction(Pokemon p) { Main.appendln(p.statRanges(false)); }
    };

}

class LearnMove extends GameAction {
    private Move move;
    LearnMove(Move m) { move = m; }
    LearnMove(String s) { move = Move.getMoveByName(s); }
    public Move getMove() { return move; }
    @Override
    void performAction(Pokemon p) { p.getMoveset().addMove(move); }
}


class UnlearnMove extends GameAction {
    private Move move;
    UnlearnMove(Move m) { move = m; }
    UnlearnMove(String s) { move = Move.getMoveByName(s); }
    public Move getMove() { return move; }
    @Override
    void performAction(Pokemon p) { p.getMoveset().delMove(move); }
}

class Evolve extends GameAction {
    private Species target;
    Evolve(Species s) { target = s; }
    Evolve(String s) { target = Species.getSpeciesFromName(s); }
    @Override
    void performAction(Pokemon p) {
        p.evolve(target);
        p.calculateStats();}
}

class GiveItem extends GameAction {
    private Item item;
    GiveItem(Item i) { item = i; }
    GiveItem(String s) { item = Item.getItemByName(s); }
    @Override
    void performAction(Pokemon p) { p.setHoldItem(item); }
}

class TakeItem extends GameAction {
    @Override
    void performAction(Pokemon p) { p.takeHoldItem(); }
}

class SetAbility extends GameAction {
    private String ability;
    SetAbility(String a) { ability = a; }
    void performAction(Pokemon p) { p.setAbility(ability); }
}