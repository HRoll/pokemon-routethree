import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.ini4j.jdk14.edu.emory.mathcs.backport.java.util.Arrays;

public class RouteParser {
    public static int lineNum = 0;

    public static List<GameAction> parseFile(String fileName) {
        lineNum = 0;
        ArrayList<GameAction> actions = new ArrayList<GameAction>();

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(
                    fileName)));
            while (in.ready()) {
                lineNum++;
                String wholeLine = in.readLine();
                String[] lines = wholeLine.split("//"); // remove comments
                String line = lines[0];
                GameAction a = null;
                try {
                    a = parseLine(line);
                } catch (Exception e) {
                    Main.appendln("Error in line " + lineNum);
                }
                if (a != null)
                    actions.add(a);

            }
            in.close();
        } catch (FileNotFoundException e) {
            Main.appendln("Could not find Route file: `" + fileName + "`");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return actions;
    }

    // assumes no comments
    private static GameAction parseLine(String line) throws Exception {
        String[] tokens = line.split(" ");
        int n = tokens.length;
        if (n == 0)
            return null;

        String firstToken = tokens[0];
        // trainer T(num)
        if (firstToken.matches("[Tt][0-9]+")) {
            Battleable b = Trainer.getTrainer(Integer.parseInt(firstToken.substring(1)));
            if (b == null) {
                Main.appendln("ERROR ON LINE "
                        + lineNum
                        + ": that trainer number doesn't exist. Are you sure you're setting the right game?");
                return null;
            }
            String[] flagTokens = (String[]) Arrays.copyOfRange(tokens, 1, n);
            return addFlagsToBattleable(b, flagTokens);
        }
        // L(num), to indicate pokemon
        else if (firstToken.matches("[Ll][0-9]+")) {
            if (n < 2) {
                Main.appendln("ERROR ON LINE " + lineNum);
                return null;
            }
            int lvl = Integer.parseInt(firstToken.substring(1));
            String species = tokens[1];
            Pokemon b = new Pokemon(Species.getSpeciesFromName(species),
                    lvl); // default to wild pokemon
            if (b.getSpecies() == null) {
                Main.appendln("ERROR ON LINE " + lineNum + ": bad pokemon name");
                return null;
            }

            String[] flagTokens = (String[]) Arrays.copyOfRange(tokens, 2, n);
            return addFlagsToBattleable(b, flagTokens);
        }
        // evolve
        else if (firstToken.equalsIgnoreCase("e")
                || firstToken.equalsIgnoreCase("evolve")) {
            if (n < 2) {
                Main.appendln("ERROR ON LINE " + lineNum);
                return null;
            }
            String species = tokens[1];
            return new Evolve(species);
        }
        // learnmove
        else if (firstToken.equalsIgnoreCase("lm")
                || firstToken.equalsIgnoreCase("learnmove")) {
            if (n < 2) {
                Main.appendln("ERROR ON LINE " + lineNum);
                return null;
            }
            String move = tokens[1];
            LearnMove l = new LearnMove(move);
            if (l.getMove() == null) {
                Main.appendln("ERROR ON LINE " + lineNum + ": bad move name");
                return null;
            }
            return l;
        }
        // unlearnmove
        else if (firstToken.equalsIgnoreCase("um")
                || firstToken.equalsIgnoreCase("unlearnmove")) {
            if (n < 2) {
                Main.appendln("ERROR ON LINE " + lineNum);
                return null;
            }
            String move = tokens[1];
            UnlearnMove l = new UnlearnMove(move);
            if (l.getMove() == null) {
                Main.appendln("ERROR ON LINE " + lineNum + ": bad move name");
                return null;
            }
            return l;
        }
        // candies, etc
        else if (firstToken.equalsIgnoreCase("rc")
                || firstToken.equalsIgnoreCase("rarecandy")) {
            return GameAction.eatRareCandy;
        } else if (firstToken.equalsIgnoreCase("hpup")) {
            return GameAction.eatHPUp;
        } else if (firstToken.equalsIgnoreCase("iron")) {
            return GameAction.eatIron;
        } else if (firstToken.equalsIgnoreCase("protein")) {
            return GameAction.eatProtein;
        } else if (firstToken.equalsIgnoreCase("calcium")) {
            return GameAction.eatCalcium;
        } else if (firstToken.equalsIgnoreCase("zinc")) {
            return GameAction.eatZinc;
        } else if (firstToken.equalsIgnoreCase("carbos")) {
            return GameAction.eatCarbos;
        }
        //items
        else if (firstToken.equalsIgnoreCase("giveitem")) {
            if (n < 2) {
                return new TakeItem();
            } else {
                return new GiveItem(tokens[1]);
            }
        } else if (firstToken.equalsIgnoreCase("takeitem")) {
            return new TakeItem();
        }
        //ability
        else if (firstToken.equalsIgnoreCase("ability") || firstToken.equalsIgnoreCase("setability")) {
            return new SetAbility(tokens[1]);
        }
        // printing commands
        else if (firstToken.equalsIgnoreCase("stats")) {
            if (n == 1) {
                return GameAction.printAllStatsNoBoost;
            } else if (tokens[1].equalsIgnoreCase("-b")) {
                return GameAction.printAllStats;
            } else {
                return GameAction.printAllStatsNoBoost;
            }
        } else if (firstToken.equalsIgnoreCase("ranges")) {
            if (n == 1) {
                return GameAction.printStatRangesNoBoost;
            } else if (tokens[1].equalsIgnoreCase("-b")) {
                return GameAction.printStatRanges;
            } else {
                return GameAction.printStatRangesNoBoost;
            }
        } else if (!firstToken.trim().isEmpty()) {
            // attempt to parse as trainer name
            //Battleable b = Trainer.getTrainer(firstToken.toUpperCase());
            Battleable b = null;
            if (b == null) {
//                Main.appendln("ERROR ON LINE "
//                        + lineNum
//                        + ": that trainer doesn't exist. Check for typos, "
//                        + "and make sure you use offsets for rockets (their names repeat)");
                Main.appendln("ERROR ON LINE " + lineNum + ": first token not recognized");
                return null;
            }
            String[] flagTokens = (String[]) Arrays.copyOfRange(tokens, 1, n);
            return addFlagsToBattleable(b, flagTokens);
        }
        return null;
    }

    private static GameAction addFlagsToBattleable(Battleable b,
            String[] flagTokens) throws Exception {
        BattleOptions options = new BattleOptions();
        
        for (int i = 0; i < flagTokens.length; i++) {
            String s = flagTokens[i];
            // set this pokemon to wild
            if (s.equalsIgnoreCase("-w") || s.equalsIgnoreCase("-wild")) {
                if (b instanceof Trainer) {
                    Main.appendln("ERROR ON LINE " + lineNum);
                    return null;
                    // can't use -wild or -trainer flag on trainers
                }
                ((Pokemon) b).setWild(true);
                continue;
            }
            // set this pokemon to trainer
            else if (s.equalsIgnoreCase("-t")
                    || s.equalsIgnoreCase("-trainer")) {
                if (b instanceof Trainer) {
                    Main.appendln("ERROR ON LINE " + lineNum);
                    return null;
                    // can't use -wild or -trainer flag on trainers
                }
                ((Pokemon) b).setWild(false);
                continue;
            }
            // xitems (sm1)
            else if (s.equalsIgnoreCase("-x")
                    || s.equalsIgnoreCase("-xitems")) {
                i++; s = flagTokens[i];//next flag
                String[] nums = s.split("/");
                if (nums.length != 5) {
                    Main.appendln("ERROR ON LINE " + lineNum);
                    return null;
                }
                options.getMod1().incrementAtkStage(Integer.parseInt(nums[0]));
                options.getMod1().incrementDefStage(Integer.parseInt(nums[1]));
                options.getMod1().incrementSpcAtkStage(
                        Integer.parseInt(nums[2]));
                options.getMod1().incrementSpcDefStage(
                        Integer.parseInt(nums[3]));
                options.getMod1().incrementSpeStage(Integer.parseInt(nums[4]));
                continue;
            }
            // yitems (sm2)
            else if (s.equalsIgnoreCase("-y")
                    || s.equalsIgnoreCase("-yitems")) {
                i++; s = flagTokens[i];//next flag
                String[] nums = s.split("/");
                if (nums.length != 5) {
                    Main.appendln("ERROR ON LINE " + lineNum);
                    return null;
                }
                options.getMod2().incrementAtkStage(Integer.parseInt(nums[0]));
                options.getMod2().incrementDefStage(Integer.parseInt(nums[1]));
                options.getMod2().incrementSpcAtkStage(
                        Integer.parseInt(nums[2]));
                options.getMod2().incrementSpcDefStage(
                        Integer.parseInt(nums[3]));
                options.getMod2().incrementSpeStage(Integer.parseInt(nums[4]));
                continue;
            }
            // all the x items (and y items)
            else if (s.equalsIgnoreCase("-xatk")) {
                i++; s = flagTokens[i];//next flag
                options.getMod1().incrementAtkStage(Integer.parseInt(s));
                continue;
            } else if (s.equalsIgnoreCase("-yatk")) {
                i++; s = flagTokens[i];//next flag
                options.getMod2().incrementAtkStage(Integer.parseInt(s));
                continue;
            } else if (s.equalsIgnoreCase("-xdef")) {
                i++; s = flagTokens[i];//next flag
                options.getMod1().incrementDefStage(Integer.parseInt(s));
                continue;
            } else if (s.equalsIgnoreCase("-ydef")) {
                i++; s = flagTokens[i];//next flag
                options.getMod2().incrementDefStage(Integer.parseInt(s));
                continue;
            } else if (s.equalsIgnoreCase("-xspc") || s.equalsIgnoreCase("-xspa")) {
                i++; s = flagTokens[i];//next flag
                options.getMod1().incrementSpcAtkStage(Integer.parseInt(s));
                continue;
            } else if (s.equalsIgnoreCase("-yspc") || s.equalsIgnoreCase("-yspa")) {
                i++; s = flagTokens[i];//next flag
                options.getMod2().incrementSpcAtkStage(Integer.parseInt(s));
                continue;
            } else if (s.equalsIgnoreCase("-xspd")) {
                i++; s = flagTokens[i];//next flag
                options.getMod1().incrementSpcDefStage(Integer.parseInt(s));
                continue;
            } else if (s.equalsIgnoreCase("-yspd")) {
                i++; s = flagTokens[i];//next flag
                options.getMod2().incrementSpcDefStage(Integer.parseInt(s));
                continue;
            } else if (s.equalsIgnoreCase("-xspe")) {
                i++; s = flagTokens[i];//next flag
                options.getMod1().incrementSpeStage(Integer.parseInt(s));
                continue;
            } else if (s.equalsIgnoreCase("-yspe")) {
                i++; s = flagTokens[i];//next flag
                options.getMod2().incrementSpeStage(Integer.parseInt(s));
                continue;
            }
            // verbose
            else if (s.equalsIgnoreCase("-v")
                    || s.equalsIgnoreCase("-verbose")) {
                i++; s = flagTokens[i];//next flag
                if (s.matches("[0-9]+")) {
                    options.setVerbose(Integer.parseInt(s));
                } else if (s.equalsIgnoreCase("NONE")) {
                    options.setVerbose(BattleOptions.NONE);
                } else if (s.equalsIgnoreCase("SOME")) {
                    options.setVerbose(BattleOptions.SOME);
                } else if (s.equalsIgnoreCase("ALL")) {
                    options.setVerbose(BattleOptions.ALL);
                }
                continue;
            }
            // split exp
            else if (s.equalsIgnoreCase("-sxp")) {
                i++; s = flagTokens[i];//next flag
                options.setParticipants(Integer.parseInt(s));
                continue;
            }
            // print stat ranges if level
            else if (s.equalsIgnoreCase("-lvranges")) {
                options.setPrintSRsOnLvl(true);
                continue;
            } else if (s.equalsIgnoreCase("-lvrangesb")) {
                options.setPrintSRsBoostOnLvl(true);
                continue;
            }
        }
        return new Battle(b, options);
    }
}
