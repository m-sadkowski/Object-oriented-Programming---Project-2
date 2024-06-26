import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import static java.lang.Math.max;

public class Swiat {

    public static final int ARROW_UP = 38;

    public static final int ARROW_DOWN = 40;

    public static final int ARROW_LEFT = 37;

    public static final int ARROW_RIGHT = 39;

    public static final int KEY_CONTROL = 17;

    private int m;
    private int n;
    private int x;
    private int y;
    private char[][] plansza;
    public Vector<String> komunikaty = new Vector<>();
    public Vector<Organizm> organizmy = new Vector<>();

    public Swiat(int m, int n) {
        this.m = m;
        this.n = n;
        plansza = new char[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                    plansza[i][j] = '.';
            }
        }
    }

    public void generujSwiat() {
        int maxOrganizmow = max(1, (m + n) / 11);
        Random rand = new Random();

        Class[] organizmyClasses = new Class[] {
                Owca.class, Wilk.class, Zolw.class, Lis.class, Antylopa.class,
                Trawa.class, Mlecz.class, Guarana.class, WilczeJagody.class,
                BarszczSosnowskiego.class
        };

        for (Class organizmClass : organizmyClasses) {
            int ilosc = rand.nextInt(maxOrganizmow) + 1;
            for (int j = 0; j < ilosc; j++) {
                Para para = generujOrganizm();
                try {
                    Constructor constructor = organizmClass.getConstructor(int.class, int.class, Swiat.class);
                    Organizm organizm = (Organizm) constructor.newInstance(para.x, para.y, this);
                    this.dodajOrganizm(organizm);
                } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        Para para = generujOrganizm();
        this.dodajOrganizm(new Czlowiek(para.x, para.y, this));
    }

    public Para generujOrganizm() {
        Random rand = new Random();
        while (true) {
            int X = rand.nextInt(m - 1);
            int Y = rand.nextInt(n - 1);
            if (getOrganizm(X, Y) == null)
            {
                return new Para(X, Y);
            }
        }
    }

    public void dodajOrganizm(Organizm organizm) {
        organizmy.add(organizm);
    }

    public void usunOrganizm(Organizm organizm) {
        for (int i = 0; i < organizmy.size(); i++) {
            if (organizmy.get(i) == organizm) {
                organizmy.remove(i);
                break;
            }
        }
    }

    public void przeniesOrganizm(Organizm organizm, int x, int y) {
        organizm.setX(x);
        organizm.setY(y);
    }

    public void posortujOrganizmy() {
        for (int i = 0; i < organizmy.size(); i++) {
            for (int j = 0; j < organizmy.size() - 1; j++) {
                if (organizmy.get(j).getInicjatywa() < organizmy.get(j + 1).getInicjatywa()) {
                    Organizm temp = organizmy.get(j);
                    organizmy.set(j, organizmy.get(j + 1));
                    organizmy.set(j + 1, temp);
                }
            }
        }
    }

    public void dodajKomunikat(String komunikat) { komunikaty.add(komunikat); }

    public void zapiszSwiat(String nazwa) {
        System.out.println("zapisano do pliku: " + nazwa);
        try {
            FileWriter writer = new FileWriter(nazwa + ".txt");

            boolean czyCzlowiekZyje = false;
            Czlowiek czlowiek = null;

            writer.write(Integer.toString(m));
            writer.write(" ");
            writer.write(Integer.toString(n));
            writer.write("\n");
            writer.write(Integer.toString(organizmy.size()));
            writer.write("\n");

            for (int i = 0; i < organizmy.size(); i++) {
                if (organizmy.get(i).getSymbol() == 'C') {
                    czyCzlowiekZyje = true;
                    czlowiek = (Czlowiek) organizmy.get(i);
                }
                writer.write(organizmy.get(i).getSymbol() + " ");
                writer.write(organizmy.get(i).getX() + " ");
                writer.write(organizmy.get(i).getY() + " " );
                writer.write(organizmy.get(i).getWiek() + " ");
                writer.write(organizmy.get(i).getSila() + " ");
                writer.write(organizmy.get(i).getInicjatywa() + " ");
                writer.write(organizmy.get(i).getCooldown() + "\n");
            }

            if (czyCzlowiekZyje)
            {
                writer.write(czlowiek.getCzasMocy() + " ");
                writer.write(czlowiek.getMocUzyta() + "\n");
            }

            writer.close();
            System.out.println("Dane zostały zapisane do pliku.");

        } catch (IOException e) {
            System.out.println("Wystąpił błąd podczas zapisu do pliku.");
            e.printStackTrace();
        }
    }

    public void wczytajSwiat(String nazwa) {
        try {
            File file = new File(nazwa + ".txt");
            Scanner scanner = new Scanner(file);
            int m = scanner.nextInt();
            int n = scanner.nextInt();
            int iloscOrganizmow = scanner.nextInt();
            for(int i = 0; i < iloscOrganizmow; i++) {
                String symbolLine = scanner.next();
                char symbol = symbolLine.charAt(0);

                int x = scanner.nextInt();
                int y = scanner.nextInt();
                int wiek = scanner.nextInt();
                int sila = scanner.nextInt();
                int inicjatywa = scanner.nextInt();
                int cooldown = scanner.nextInt();

                switch (symbol)
                {
                    case 'A':
                        dodajOrganizm(new Antylopa(x, y, this, wiek, sila, inicjatywa, cooldown));
                        break;
                    case 'b':
                        dodajOrganizm(new BarszczSosnowskiego(x, y, this, wiek, sila, inicjatywa, cooldown));
                        break;
                    case 'C':
                        dodajOrganizm(new Czlowiek(x, y, this, wiek, sila, inicjatywa, cooldown));
                        break;
                    case 'g':
                        dodajOrganizm(new Guarana(x, y, this, wiek, sila, inicjatywa, cooldown));
                        break;
                    case 'L':
                        dodajOrganizm(new Lis(x, y, this, wiek, sila, inicjatywa, cooldown));
                        break;
                    case 'm':
                        dodajOrganizm(new Mlecz(x, y, this, wiek, sila, inicjatywa, cooldown));
                        break;
                    case 'O':
                        dodajOrganizm(new Owca(x, y, this, wiek, sila, inicjatywa, cooldown));
                        break;
                    case 't':
                        dodajOrganizm(new Trawa(x, y, this, wiek, sila, inicjatywa, cooldown));
                        break;
                    case 'w':
                        dodajOrganizm(new WilczeJagody(x, y, this, wiek, sila, inicjatywa, cooldown));
                        break;
                    case 'W':
                        dodajOrganizm(new Wilk(x, y, this, wiek, sila, inicjatywa, cooldown));
                        break;
                    case 'Z':
                        dodajOrganizm(new Zolw(x, y, this, wiek, sila, inicjatywa, cooldown));
                        break;
                    default:
                        break;
                }
            }
            int czasMocy = scanner.nextInt();
            int mocUzyta = scanner.nextInt();

            for (int i = 0; i < organizmy.size(); i++)
            {
                if (organizmy.get(i).getSymbol() == 'C')
                {
                    Czlowiek czlowiek = (Czlowiek) organizmy.get(i);
                    czlowiek.setCzasMocy(czasMocy);
                    if(mocUzyta == 1) {
                        czlowiek.setMocUzyta(true);
                    } else {
                        czlowiek.setMocUzyta(false);
                    }
                    break;
                }
            }
            scanner.close();
        } catch (FileNotFoundException f) {
            System.err.println("Nie znaleziono pliku: " + f.getMessage());
        }
    }

    public int getWysokosc() {
        return m;
    }

    public int getSzerokosc() { return n; }

    public int getIloscOrganizmow() { return organizmy.size(); }

    public Organizm getOrganizm(int x, int y) {
        for (int i = 0; i < organizmy.size(); i++) {
            Organizm organizm = organizmy.get(i);
            if (organizm.getX() == x && organizm.getY() == y) {
                return organizm;
            }
        }
        return null;
    }

    public char[][] getPlansza() { return plansza; }

    public void wykonajTure(int strzalka) {
        if (!(strzalka == ARROW_UP || strzalka == ARROW_DOWN || strzalka == ARROW_LEFT || strzalka == ARROW_RIGHT || strzalka == KEY_CONTROL)) {
            return;
        }
        posortujOrganizmy();
        for (int i = 0; i < organizmy.size(); i++) {
            organizmy.get(i).zwiekszWiek();
            if (organizmy.get(i).getCooldown() > 0) {
                organizmy.get(i).zmniejszCooldown();
            }
            if (organizmy.get(i).getSymbol() == 'C') {
                Czlowiek czlowiek = (Czlowiek) organizmy.get(i);
                czlowiek.akcja(strzalka);
            } else {
                organizmy.get(i).akcja();
            }
        }
    }

    public boolean czyCzlowiekZyje() {
        for (int i = 0; i < organizmy.size(); i++) {
            if (organizmy.get(i).getSymbol() == 'C') {
                return true;
            }
        }
        return false;
    }


    public void dodajOrganizm(String nazwa, int clickedRow, int clickedColumn) {
        switch (nazwa)
        {
            case "Antylopa":
                dodajOrganizm(new Antylopa(clickedRow, clickedColumn, this));
                break;
            case "Barszcz sosnowskiego":
                dodajOrganizm(new BarszczSosnowskiego(clickedRow, clickedColumn, this));
                break;
            case "Guarana":
                dodajOrganizm(new Guarana(clickedRow, clickedColumn, this));
                break;
            case "Lis":
                dodajOrganizm(new Lis(clickedRow, clickedColumn, this));
                break;
            case "Mlecz":
                dodajOrganizm(new Mlecz(clickedRow, clickedColumn, this));
                break;
            case "Owca":
                dodajOrganizm(new Owca(clickedRow, clickedColumn, this));
                break;
            case "Trawa":
                dodajOrganizm(new Trawa(clickedRow, clickedColumn, this));
                break;
            case "Wilcze jagody":
                dodajOrganizm(new WilczeJagody(clickedRow, clickedColumn, this));
                break;
            case "Wilk":
                dodajOrganizm(new Wilk(clickedRow, clickedColumn, this));
                break;
            case "Zolw":
                dodajOrganizm(new Zolw(clickedRow, clickedColumn, this));
                break;
            case "Czlowiek":
                dodajOrganizm(new Czlowiek(clickedRow, clickedColumn, this));
                break;
            default:
                break;
        }
        System.out.println(nazwa + clickedRow + clickedColumn);
    }
}
