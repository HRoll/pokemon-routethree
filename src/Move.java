import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;


public class Move {
    private String name;
    private Type type;
    private int pp;
    private int power;
    private int accuracy;
    private int indexNum;
    //TODO: more fields for special cases (enum on special case?)
    
    private static Move[] allMoves;
    private static HashMap<String,Move> allMovesHashMap;
    
    public Move(String m_name, Type m_type, int m_pp, int m_power, int m_accuracy, int m_indexNum) {
        name = m_name;
        type = m_type;
        pp = m_pp;
        power = m_power;
        accuracy = m_accuracy;
        indexNum = m_indexNum;
    }
    
    public Move(Move baseMove, int increaseStage) {
        name = baseMove.name+" "+increaseStage;
        type = baseMove.type;
        pp = baseMove.pp;
        power = baseMove.power;
        accuracy = baseMove.accuracy;
        indexNum = baseMove.indexNum;
    }
    
    public Move(Move baseMove, String newName, int newPower) {
        name = newName;
        type = baseMove.type;
        pp = baseMove.pp;
        power = newPower;
        accuracy = baseMove.accuracy;
        indexNum = baseMove.indexNum;
    }
    
    static {
        allMoves = new Move[Constants.numMoves + 1];
        Move m;
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(
                    System.class.getResource("/resources/movedata.txt").openStream()));
            String text; 
            for (int i = 1; i <= Constants.numMoves; i++) {
                text = in.readLine();
                String[] tokens = text.split("\t");
                String m_name = tokens[0];
                int m_accuracy = Integer.parseInt(tokens[1]);
                int m_power = Integer.parseInt(tokens[2]);
                int m_pp = Integer.parseInt(tokens[3]);
                Type m_type = toType(tokens[4]);

                m = new Move(m_name, m_type, m_pp, m_power, m_accuracy,i);
                allMoves[i] = m;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        allMovesHashMap = new HashMap<String,Move>();
        for (Move m1 : allMoves) {
            if(m1 != null) {
                allMovesHashMap.put(Constants.hashName(m1.getName()), m1);
            }
        }
        //TODO: put in special cases
    }
    
    private static Type toType(String t) {
        //NORMAL, FIGHTING, FLYING, GRASS, WATER, FIRE, ROCK, GROUND, PSYCHIC, BUG, DRAGON, ELECTRIC, GHOST, POISON, ICE, STEEL, DARK
        if(t.equalsIgnoreCase("NORMAL")) {
            return Type.NORMAL;
        }
        else if(t.equalsIgnoreCase("FIGHTING")) {
            return Type.FIGHTING;
        }
        else if(t.equalsIgnoreCase("FLYING")) {
            return Type.FLYING;
        }
        else if(t.equalsIgnoreCase("GRASS")) {
            return Type.GRASS;
        }
        else if(t.equalsIgnoreCase("WATER")) {
            return Type.WATER;
        }
        else if(t.equalsIgnoreCase("FIRE")) {
            return Type.FIRE;
        }
        else if(t.equalsIgnoreCase("ROCK")) {
            return Type.ROCK;
        }
        else if(t.equalsIgnoreCase("GROUND")) {
            return Type.GROUND;
        }
        else if(t.equalsIgnoreCase("PSYCHIC")) {
            return Type.PSYCHIC;
        }
        else if(t.equalsIgnoreCase("BUG")) {
            return Type.BUG;
        }
        else if(t.equalsIgnoreCase("DRAGON")) {
            return Type.DRAGON;
        }
        else if(t.equalsIgnoreCase("ELECTRIC")) {
            return Type.ELECTRIC;
        }
        else if(t.equalsIgnoreCase("GHOST")) {
            return Type.GHOST;
        }
        else if(t.equalsIgnoreCase("POISON")) {
            return Type.POISON;
        }
        else if(t.equalsIgnoreCase("ICE")) {
            return Type.ICE;
        }
        else if(t.equalsIgnoreCase("STEEL")) {
            return Type.STEEL;
        }
        else if(t.equalsIgnoreCase("DARK")) {
            return Type.DARK;
        }
        else {
            return Type.NONE;
        }
    }
    
    //returns the move object corresponding to the move with index i
    public static Move getMove(int i) {
        if(i < 0 || i >= allMoves.length)
            return null;
        return allMoves[i];
    }
    
    public static Move getMoveByName(String name) {
        name = Constants.hashName(name);
        if(!allMovesHashMap.containsKey(name))
            return null;
        return allMovesHashMap.get(name);
    }
    
    public String toString() {
        return String.format("%d %s %s PP: %d Power: %d Acc: %d", indexNum, 
                name, type, pp, power, accuracy);
    }
    
    public String getName() {
        return name;
    }
    public Type getType() {
        return type;
    }
    public int getPp() {
        return pp;
    }
    public int getPower() {
        return power;
    }
    public int getAccuracy() {
        return accuracy;
    }
    public int getIndexNum() {
        return indexNum;
    }
    //this will be different in gen 4+
    public boolean isPhysicalMove() {
        return Type.isPhysicalType(type);
    }
    
    //TODO: consider checking more
    public boolean isEqual(Object o) {
        return (o instanceof Move) && ((Move) o).indexNum == this.indexNum;        
    }
    
    public int hashCode() {
        return indexNum;
    }
}
