import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.visualnuts.domain.Data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Type REVIEW_TYPE = new TypeToken<List<Data>>() {
        }.getType();
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader("data.json"));
        List<Data> data = gson.fromJson(reader, REVIEW_TYPE);
        System.out.println("Returns the number of countries in the world: " + data.size());
        System.out.println("Finds the country with the most official languages, where they officially speak German (de): " + findByMostLanguage(data));
        System.out.println("That counts all the official languages spoken in the listed countries: " + countsAllOfficialLanguages(data));
        System.out.println("To find the country with the highest number of official languages: " + findByMostLanguage(data));
        System.out.println("To find the most common official language(s), of all countries: " + findTheMostLanguageUsedByCountries(data));
    }

    private static String findByMostLanguage(List<Data> data) {
        String countryWithMoreLanguages = "";
        int count = 0;
        for (Data d : data) {
            if (d.getLanguages().size() > count) {
                count = d.getLanguages().size();
                countryWithMoreLanguages = d.getCountry();
            }
        }
        return countryWithMoreLanguages;
    }

    private static long countsAllOfficialLanguages(List<Data> data) {
        return data.stream()
                .map(Data::getLanguages)
                .flatMap(Collection::stream)
                .distinct()
                .count();
    }

    private static List<String> findTheMostLanguageUsedByCountries(List<Data> data) {
        Map<String, Integer> languageCounts = new HashMap<>();
        for (Data country : data) {
            for (String language : country.getLanguages()) {
                languageCounts.put(language, languageCounts.getOrDefault(language, 0) + 1);
            }
        }
        int maxCount = 0;
        List<String> mostCommonLanguages = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : languageCounts.entrySet()) {
            int count = entry.getValue();
            if (count > maxCount) {
                maxCount = count;
                mostCommonLanguages.clear();
                mostCommonLanguages.add(entry.getKey());
            } else if (count == maxCount) {
                mostCommonLanguages.add(entry.getKey());
            }
        }
        return mostCommonLanguages;
    }
}