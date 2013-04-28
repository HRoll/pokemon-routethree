import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

//a trainer has a class name and some pokemon, corresponding to some location in memory
public class Trainer implements Battleable, Iterable<Pokemon> {
    private String name;
    private ArrayList<Pokemon> pokes;
    private int trainerNumber;
    private int nameValue;
    private boolean isFemale;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Trainer)) {
            return false;
        } else {
            Trainer t = (Trainer) o;
            return t.name.equals(name) && t.trainerNumber == trainerNumber;
        }
    }

    @Override
    public void battle(Pokemon p, BattleOptions options) {
        for (Pokemon tp : pokes) {
            tp.battle(p, options);
        }
    }

    @Override
    public Iterator<Pokemon> iterator() {
        return pokes.iterator();
    }

    public String toString() {
        return String.format("%s (%d: %s)",  name, trainerNumber,
                allPokes());
    }

    public String allPokes() {
        StringBuilder sb = new StringBuilder();
        for (Pokemon p : pokes) {
            sb.append(p.levelName() + ", ");
        }
        return sb.toString();
    }

    private static HashMap<Integer, Trainer> allTrainers;

    public static Trainer getTrainer(int trainerNum) {
        if (!allTrainers.containsKey(trainerNum))
            return null;
        else
            return allTrainers.get(trainerNum);
    }

//    private static HashMap<String, Trainer> trainersByName;
//
//    public static Trainer getTrainer(String name) {
//        return trainersByName.get(name);
//    }

    // must be called before any other calls are made
    public static void initTrainers() {
        allTrainers = new HashMap<Integer, Trainer>();
        //trainersByName = new HashMap<String, Trainer>();

        List<Trainer> trainerList = null;
        if (Settings.game == Settings.FIRERED || Settings.game == Settings.LEAFGREEN)
            trainerList = getData("trainer_data_frlg.txt");
        else if (Settings.game == Settings.RUBY || Settings.game == Settings.SAPPHIRE)
            trainerList = getData("trainer_data_rs.txt");
        else
            trainerList = getData("trainer_data_e.txt");

        for (Trainer t : trainerList) {
            allTrainers.put(new Integer(t.trainerNumber), t);
//            if (t.name.equals("GRUNT") == false
//                    && t.name.equals("EXECUTIVE") == false
//                    && t.name.equals("?") == false
//                    && trainersByName.containsKey(t.name) == false) {
//                trainersByName.put(t.name, t);
//            }
        }
    }

    // reads trainer_data_(frlg|rs|e).txt to get trainer data
    private static List<Trainer> getData(String filename) {
        ArrayList<Trainer> trainers = new ArrayList<Trainer>();
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(System.class
                    .getResource("/resources/" + filename).openStream()));

            Trainer t;
            while (in.ready()) {
                String text = in.readLine();
                if(text.trim().isEmpty()) {
                    continue;
                }
                //Trainer line: num|name|isFemale|pokeDataType|#pokes|nameVal
                t = new Trainer();
                String[] parts = text.split("\\|");
                t.trainerNumber = Integer.parseInt(parts[0]);
                t.name = parts[1];
                t.isFemale = Integer.parseInt(parts[2]) == 1;
                int pokeDataType = Integer.parseInt(parts[3]);
                int numPokes = Integer.parseInt(parts[4]);
                t.nameValue = Integer.parseInt(parts[5]);
                t.pokes = new ArrayList<Pokemon>();
                
                //the next few lines are pokemon lines
                long incrementedNameValue = 0; //the sum of name values of trainer + pokes
                for(int i = 0; i < numPokes; i++) {
                    String pokeText = in.readLine();
                    String[] pokeParts = pokeText.split("\\|");
                    int level = Integer.parseInt(pokeParts[0].substring(1));
                    Species s = Species.getSpeciesFromName(pokeParts[1]);
                    int AI = Integer.parseInt(pokeParts[2]);
                    int ivVal = 31 * AI / 255;
                    IVs ivs = new IVs(ivVal);
                    
                    incrementedNameValue += t.nameValue;
                    incrementedNameValue += s.getNameValue();
                    long PID = (incrementedNameValue << 8) + (t.isFemale ? 0x77 : 0x88);
                    //moveset not specified
                    if(pokeDataType == 0) {
                        Pokemon pk = new Pokemon(s, level, ivs, Nature.getNature(PID));
                        t.pokes.add(pk);
                    }
                    //moveset not specified with hold item
                    else if(pokeDataType == 2) {
                        Pokemon pk = new Pokemon(s, level, ivs, Nature.getNature(PID));
                        Item item = Item.getItem(Integer.parseInt(pokeParts[3]));
                        pk.setHoldItem(item);
                        t.pokes.add(pk);
                    }
                    //moveset specified
                    else if(pokeDataType == 1) {
                        int move1 = Integer.parseInt(pokeParts[3]);
                        int move2 = Integer.parseInt(pokeParts[4]);
                        int move3 = Integer.parseInt(pokeParts[5]);
                        int move4 = Integer.parseInt(pokeParts[6]);
                        Moveset m = new Moveset();
                        if (move1 != 0)
                            m.addMove(move1);
                        if (move2 != 0)
                            m.addMove(move2);
                        if (move3 != 0)
                            m.addMove(move3);
                        if (move4 != 0)
                            m.addMove(move4);
                        Pokemon pk = new Pokemon(s, level, m, ivs, Nature.getNature(PID));
                        t.pokes.add(pk);
                    }
                    //hold item, and moveset specified
                    else if(pokeDataType == 3) {
                        Item item = Item.getItem(Integer.parseInt(pokeParts[3]));
                        int move1 = Integer.parseInt(pokeParts[4]);
                        int move2 = Integer.parseInt(pokeParts[5]);
                        int move3 = Integer.parseInt(pokeParts[6]);
                        int move4 = Integer.parseInt(pokeParts[7]);
                        Moveset m = new Moveset();
                        if (move1 != 0)
                            m.addMove(move1);
                        if (move2 != 0)
                            m.addMove(move2);
                        if (move3 != 0)
                            m.addMove(move3);
                        if (move4 != 0)
                            m.addMove(move4);
                        Pokemon pk = new Pokemon(s, level, m, ivs, Nature.getNature(PID));
                        pk.setHoldItem(item);
                        t.pokes.add(pk);
                    }
                }
                trainers.add(t);
            }
            in.close();
            return trainers;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
    
    public int getNameValue() {
        return nameValue;
    }
    public String getName() {
        return name;
    }
}
