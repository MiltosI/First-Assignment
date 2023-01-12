import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.stream.Collectors;

public class App {
    private static float totalBuy = 0.0f;
    private static float totalSell = 0.0f;
    private static HashMap<String, Float> entitiesBuy = new HashMap<>();
    private static HashMap<String, Float> entitiesSell = new HashMap<>();

    public static void main(String[] args) throws ParseException {
        // Create a new scanner
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // Prompt the user for input
            System.out.print("Enter a entities:\n ");
            String entities = scanner.nextLine();
            scanner.nextLine();

            System.out.print("Buy or Sell (true/false):\n ");
            boolean buySell = scanner.nextBoolean();
            scanner.nextLine();

            System.out.print("Enter AgreedFx:\n ");
            float agreedFx = scanner.nextFloat();
            scanner.nextLine();

            System.out.print("Enter Currency:\n ");
            String currency = scanner.nextLine();

            System.out.print("Enter InstructionDate in the format dd-MM-yyyy:\n ");
            String instructionDate = scanner.nextLine();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date dateOfSettlement = format.parse(instructionDate);

            System.out.print("Enter Unit:\n ");
            float units = scanner.nextFloat();

            System.out.print("Enter Price Per Unit:\n ");
            float pricePerUnits = scanner.nextFloat();

            if (currency.equals("AED") || currency.equals("SAR")) {
                Calendar c = Calendar.getInstance();
                c.setTime(dateOfSettlement);
                // check if date is a weekend
                while (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                    c.add(Calendar.DATE, 1);
                }
                // check if date is not a working day
                while (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                    c.add(Calendar.DATE, 1);
                }
                dateOfSettlement = c.getTime();
            } else {
                Calendar c = Calendar.getInstance();
                c.setTime(dateOfSettlement);
                // check if date is a weekend
                while (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    c.add(Calendar.DATE, 1);
                }
                dateOfSettlement = c.getTime();
            }
            System.out.println("New Settlement Date: " + dateOfSettlement);
            float total = calculateUSD(pricePerUnits, units, agreedFx);
            System.out.println("USD amount of a trade: " + total);

            if (buySell) {
                totalBuy += total;
                if(entitiesBuy.containsKey(entities)){
                    entitiesBuy.put(entities, entitiesBuy.get(entities) + total);
                }else{
                    entitiesBuy.put(entities, total);
                }

            } else {
                totalSell += total;
                if(entitiesSell.containsKey(entities)){
                    entitiesSell.put(entities, entitiesSell.get(entities) + total);
                }else{
                    entitiesSell.put(entities, total);
                }

            }
            System.out.println("Total amount of Buys: " + totalBuy);
            System.out.println("Total amount of Sells: " + totalSell);
            System.out.println("Entities ranking based on amount of buys:");
            entitiesBuy = sortByValue(entitiesBuy);
            int i = 1;
            for(Map.Entry<String, Float> entry : entitiesBuy.entrySet()){
                System.out.println(i + ": " + entry.getKey() + " - " + entry.getValue());
                i++;
            }
            System.out.println("Entities ranking based on amount of sells:");
            i = 1;
            entitiesSell = sortByValue(entitiesSell);
            for(Map.Entry<String, Float> entry : entitiesSell.entrySet()){
                System.out.println(i + ": " + entry.getKey() + " - " + entry.getValue());
                i++;
            }
        }
    }
    public static float calculateUSD(float pricePerUnits, float units, float agreedFx) {
        return (pricePerUnits * units * agreedFx);
    }
    public static HashMap<String, Float> sortByValue(HashMap<String, Float> hm) {
        return hm.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
