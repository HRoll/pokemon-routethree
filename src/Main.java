import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;


public class Main {
    private static StringBuilder output = new StringBuilder();
    
    public static void append(String s) {
        output.append(s);
    }
    public static void appendln(String s) {
        output.append(s + Constants.endl);
    }
    
    public static void main2(String[] args) {
        //initialization
        
        
        
//        System.out.println(Move.getMoveByName("PSyCHOBoOST"));
//        System.out.println(Learnset.getLearnset(4, true));
//        System.out.println(Learnset.getLearnset(4, false));
//        System.out.println(Constants.numPokes);
//        for (int i = 1; i <= 9; i++) {
//            System.out.println(Species.getSpecies(i));
//        }
//        for (int i = 0; i < 25; i++) {
//            System.out.println(Nature.getNature(i));
//        }
        Species s = Species.getSpeciesFromName("ALAKAZAM");
        Pokemon p = new Pokemon(s, 100);
        p.setNature(Nature.getNature(1));
        p.setAtkBadge(true);
        p.setSpcBadge(true);
        p.setHoldItem(Item.getItemByName("MYSTIC  waTER"));
        System.out.println(p);
        System.out.println(p.statsPrintout(false));
        System.out.println(p.statRanges(false));

        Trainer t = Trainer.getTrainer(804);
        System.out.println(t);
        for (Pokemon pp : t) {
            System.out.println(pp);
        }
        
        System.out.println(output);
    }
    
    public static void main(String[] args) throws InvalidFileFormatException, IOException { 
        String fileName = (args.length > 0) ? args[0] : "config.ini";
        Wini ini = new Wini(new File(fileName));
        //set pokemon
        String species = ini.get("poke", "species");
        int level = ini.get("poke", "level", int.class);
        int hpIV = ini.get("poke", "hpIV", int.class);
        int atkIV = ini.get("poke", "atkIV", int.class);
        int defIV = ini.get("poke", "defIV", int.class);
        int spaIV = ini.get("poke", "spaIV", int.class);
        int spdIV = ini.get("poke", "spdIV", int.class);
        int speIV = ini.get("poke", "speIV", int.class);
        
        //set game
        String gameName = ini.get("game", "game");
        if(gameName.equalsIgnoreCase("firered"))
            Settings.game = Settings.FIRERED;
        else if(gameName.equalsIgnoreCase("leafgreen"))
            Settings.game = Settings.LEAFGREEN;
        else if(gameName.equalsIgnoreCase("ruby"))
            Settings.game = Settings.RUBY;
        else if(gameName.equalsIgnoreCase("SAPPHIRE"))
            Settings.game = Settings.SAPPHIRE;
        else 
            Settings.game = Settings.EMERALD;
        
        Trainer.initTrainers();
        
        Nature nature = Nature.getNature(ini.get("poke", "nature"));
        IVs ivs = new IVs(hpIV,atkIV,defIV,spaIV,spdIV,speIV);
        boolean atkBadge = ini.get("extras", "atkBadge", boolean.class);
        boolean defBadge = ini.get("extras", "defBadge", boolean.class);
        boolean speBadge = ini.get("extras", "speBadge", boolean.class);
        boolean spcBadge = ini.get("extras", "spcBadge", boolean.class);
        int hpEV = ini.get("extras", "hpEV", int.class);
        int atkEV = ini.get("extras", "atkEV", int.class);
        int defEV = ini.get("extras", "defEV", int.class);
        int spaEV = ini.get("extras", "spaEV", int.class);
        int spdEV = ini.get("extras", "spdEV", int.class);
        int speEV = ini.get("extras", "speEV", int.class);
        
        Pokemon p = null;
        try {
            p = new Pokemon(Species.getSpeciesFromName(species),level,ivs,nature);
            p.setAtkBadge(atkBadge);
            p.setDefBadge(defBadge);
            p.setSpeBadge(speBadge);
            p.setSpcBadge(spcBadge);
            EVs evs = new EVs(hpEV,atkEV,defEV,spaEV,spdEV,speEV);
            p.setEVs(evs);
        } catch(NullPointerException e) {
            appendln("Error in your config file. Perhaps you have an incorrect pokemon species name?");
            FileWriter fw = new FileWriter(ini.get("files", "outputFile"));
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(output.toString());
            bw.close();
            return;
        }
        List<GameAction> actions = RouteParser.parseFile(ini.get("files","routeFile"));
        
        int[] XItems = {0,0,0,0,0}; //atk,def,spa,spd,spe
        int numBattles = 0;
        int rareCandies = 0;
        int HPUp = 0;
        int iron = 0;
        int protein = 0;
        int calcium = 0;
        int zinc = 0;
        int carbos = 0;
        for(GameAction a : actions) {        
            a.performAction(p);
            if (a instanceof Battle) {
                StatModifier sm = ((Battle) a).getMod1();
                XItems[0] += Math.max(0, sm.getAtkStage());
                XItems[1] += Math.max(0, sm.getDefStage());
                XItems[2] += Math.max(0, sm.getSpcAtkStage());
                XItems[3] += Math.max(0, sm.getSpcDefStage());
                XItems[4] += Math.max(0, sm.getSpeStage());
                numBattles++;
            } else if (a == GameAction.eatRareCandy) {
                rareCandies++;
            } else if (a == GameAction.eatHPUp){
                HPUp++;
            } else if (a == GameAction.eatIron){
                iron++;
            } else if (a == GameAction.eatProtein){
                protein++;
            } else if (a == GameAction.eatCarbos){
                carbos++;
            } else if (a == GameAction.eatCalcium){
                calcium++;
            } else if (a == GameAction.eatZinc) {
                zinc++;
            }
        }        
        
        if(ini.get("util", "printxitems", boolean.class)) {
            if(XItems[0] != 0)
                appendln("X ATTACKS: " + XItems[0]);
            if(XItems[1] != 0)
                appendln("X DEFENDS: " + XItems[1]);
            if(XItems[2] != 0)
                appendln("X SPECIALS: " + XItems[2]);
            if(XItems[3] != 0)
                appendln("X SPECIAL DEFS: " + XItems[3]);
            if(XItems[4] != 0)
                appendln("X SPEEDS: " + XItems[4]);
            int cost = XItems[0] * 500 + XItems[1] * 550 + XItems[2] * 350 + XItems[3] * 350 + XItems[4] * 350;
            if(cost != 0)
                appendln("X item cost: " + cost);
        }
        
        if(ini.get("util", "printrarecandies", boolean.class)) {
            if(rareCandies != 0)
                appendln("Total Rare Candies: " + rareCandies);
        }
        if(ini.get("util", "printstatboosters", boolean.class)) {
            if(HPUp != 0) {
                appendln("HP UP: " + HPUp);
            }
            if(iron != 0) {
                appendln("IRON: " + iron);
            }
            if(protein != 0) {
                appendln("PROTEIN: " + protein);
            }
            if(calcium != 0) {
                appendln("CALCIUM: " + calcium);
            }
            if(zinc != 0) {
                appendln("ZINC: " + zinc);
            }
            if(carbos != 0) {
                appendln("CARBOS: " + carbos);
            }
        }
        //System.out.println("Total Battles: " + numBattles);
        
        
        FileWriter fw = new FileWriter(ini.get("files", "outputFile"));
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(output.toString());
        bw.close();
 
    }
}
