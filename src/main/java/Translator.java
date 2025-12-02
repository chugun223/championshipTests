public class Translator {
    public static String translatePosition(Position position){
        return switch(position){
            case DEFENDER -> "ЗАЩИТНИК";
            case MIDFIELD -> "ПОЛУЗАЩИТНИК";
            case FORWARD -> "НАПАДАЮЩИЙ";
            case GOALKEEPER -> "ВРАТАРЬ";
        };
    }
}
