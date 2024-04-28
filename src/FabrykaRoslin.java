public class FabrykaRoslin {
    public static Roslina utworzRosline(char symbol, int x, int y, Swiat swiat) {
        switch (symbol) {
            case 'b':
                return new BarszczSosnowskiego(x, y, swiat);
            case 'g':
                return new Guarana(x, y, swiat);
            case 'm':
                return new Mlecz(x, y, swiat);
            case 't':
                return new Trawa(x, y, swiat);
            case 'w':
                return new WilczeJagody(x, y, swiat);
            default:
                return null;
        }
    }
};