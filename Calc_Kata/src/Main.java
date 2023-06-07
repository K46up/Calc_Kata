import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите выражение: ");
        while (true){
        String input = scanner.nextLine();
        String result = Main.calc(input); // присваиваем строке возвращенную строку из метода калк в классе мейн
        System.out.println(result);
        }

    }
    public static String calc (String input) throws IOException {

        String[] words = input.split(" ");

        //проверка на кол-во элементов в массиве, позволяет отсеить (1), (1 + 3 - 5)
        if ((words.length != 3)) {
            throw new IOException("Формат математической операции не удовлетворяет заданию - " +
                    "два операнда и один оператор (+, -, /, *)");
        }

        String strA = words[0];
        String strB = words[2];
        String sign = words[1];

        int a = 0;
        int b = 0;
        int result = 0;

        //проверка на совпадение типа чисел, позволяет отсеить (1 + v), (x - 2)
        if(!strA.matches("^[1-9]$|^10$") && strB.matches("^[1-9]$|^10$")){
            throw new IOException("Калькулятор умеет работать только с арабскими или римскими цифрами одновременно!!!");
        } else if (strA.matches("^[1-9]$|^10$") && !strB.matches("^[1-9]$|^10$")) {
            throw new IOException("Калькулятор умеет работать только с арабскими или римскими цифрами одновременно!!!");
        }

        //проверка и конвертация строк в числа
        if(strA.matches("^[1-9]$|^10$") && strB.matches("^[1-9]$|^10$")){

                a = Integer.parseInt(words[0]);
                b = Integer.parseInt(words[2]);

        } else if (!strA.matches("^[1-9]$|^10$") && !strB.matches("^[1-9]$|^10$")) {

                a = Main.romanToArabic(strA);
                b = Main.romanToArabic(strB);

        }

        //проверка чисел на интервал 1-10
        if (a < 1 | a > 10 | b < 1 | b > 10) {
            throw new IOException("Введенные числа должны быть в диапозоне от 1 до 10 включительно!!!");
        }

        switch (sign){
            case "+":
                result = a + b;
                break;
            case "-":
                result = a - b;
                break;
            case "/":
                result = a / b;
                break;
            case "*":
                result = a * b;
                break;
            default:
                throw new IOException("Строка не является математической операцией!!!");
        }

        //проверка на наличие правильного оператора, позволяет избежать (5 % 10)
/*        if (!sign.equals("+") && !sign.equals("-") && !sign.equals("*") && !sign.equals("/")) {
            throw new IOException("Строка не является математической операцией!!!");
        }*/

        String str = input;

        if(!strA.matches("^[1-9]$|^10$") && !strB.matches("^[1-9]$|^10$")){
            str = Main.arabicToRoman(result);
            if (result <= 0) {
                throw new IOException("В римской системе нет отрицательных чисел!");
            }
        } else if (strA.matches("^[1-9]$|^10$") && strB.matches("^[1-9]$|^10$")) {
            str = Integer.toString(result);
        }

        return str;
    }

    enum RomanNumeral {
        I(1), IV(4), V(5), IX(9), X(10),
        XL(40), L(50), XC(90), C(100),
        CD(400), D(500), CM(900), M(1000);

        private int value;

        RomanNumeral(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static List<RomanNumeral> getReverseSortedValues() {
            return Arrays.stream(values())
                    .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
                    .collect(Collectors.toList());
        }
    }

    public static String arabicToRoman(int number) {

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }

    public static int romanToArabic(String input) {
        String romanNumeral = input.toUpperCase();
        int result = 0;

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;

        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        if (romanNumeral.length() > 0) {
            throw new IllegalArgumentException(input + "Невозможно конвертировать в римские цифры!!!");
        }

        return result;
    }
}


