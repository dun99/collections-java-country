package baii8;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IOFile {
    public IOFile() {
    }

    String countriesFile = "C:\\Users\\admin\\IdeaProjects\\vao_ra_file\\src\\baii8\\countries.dat";
    String citiesFile = "C:\\Users\\admin\\IdeaProjects\\vao_ra_file\\src\\baii8\\cities.dat";

    public List<Country> toCountryList() throws IOException {
        List<Country> countryList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(countriesFile));
        String line = br.readLine();
        Pattern pattern = Pattern.compile("^Country\\{code=(\\w+), name=(.*), continent=(.*), surfaceArea=(.*), population=(\\d+), gnp=(\\d+\\.?\\d+), capital=(.*)}");
        while (line != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String countriesCode = matcher.group(1);
                String name = matcher.group(2);
                String continent = matcher.group(3);
                double surfaceArea = Double.parseDouble(matcher.group(4));
                int population = Integer.parseInt(matcher.group(5));
                double gnp = Double.parseDouble(matcher.group(6));
                int capital = Integer.parseInt(matcher.group(7));
                countryList.add(new Country(countriesCode, name, continent, surfaceArea, population, gnp, capital));
            }
            line = br.readLine();
        }
        br.close();
        return countryList;
    }

    public Map<String, Country> toCountryMap() throws IOException {
        Map<String, Country> countryMap = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(countriesFile));
        String line = br.readLine();
        Pattern pattern = Pattern.compile("^Country\\{code=(\\w+), name=(.*), continent=(.*), surfaceArea=(.*), population=(\\d+), gnp=(\\d+\\.?\\d+), capital=(.*)}");

        while (line != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String co_code = matcher.group(1);
                String name = matcher.group(2);
                String continent = matcher.group(3);
                double surfaceArea = Double.parseDouble(matcher.group(4));
                int population = Integer.parseInt(matcher.group(5));
                double gnp = Double.parseDouble(matcher.group(6));
                int capital = Integer.parseInt(matcher.group(7));
                countryMap.put(co_code, new Country(co_code, name, continent, surfaceArea, population, gnp, capital));
            }
            line = br.readLine();
        }
        br.close();
        return countryMap;
    }

    public Map<Integer, City> toCityMap() throws IOException {
        Map<Integer, City> citiesMap = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(citiesFile));
        String line = br.readLine();
        Pattern pattern = Pattern.compile("^City \\[id=(\\d+), name=(.*), population=(\\d+), countryCode=(\\w+)\\]");
        while (line != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                int cityCode = Integer.parseInt(matcher.group(1));
                String name = matcher.group(2);
                int population = Integer.parseInt(matcher.group(3));
                String countryCode = matcher.group(4);
                citiesMap.put(cityCode, new City(cityCode, name, population, countryCode));
            }
            line = br.readLine();
        }
        br.close();
        return citiesMap;
    }

    public List<City> toCityList() throws IOException {
        List<City> cityList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(citiesFile));
        String line = br.readLine();
        while (line != null) {
            Pattern pattern = Pattern.compile("^City \\[id=(\\d+), name=(.*), population=(\\d+), countryCode=(\\w+)\\]");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                int id = Integer.parseInt(matcher.group(1));
                String name = matcher.group(2);
                int population = Integer.parseInt(matcher.group(3));
                String countryCode = matcher.group(4);
                cityList.add(new City(id, name, population, countryCode));
            }
            line = br.readLine();
        }
        br.close();
        return cityList;
    }


    public static void main(String[] args) throws IOException {
        IOFile io = new IOFile();
        List<City> cityList = io.toCityList();
        List<Country> countryList = io.toCountryList();
        Map<Integer, City> cityMap = io.toCityMap();
        Map<String, Country> countryMap = io.toCountryMap();
        System.out.println("Thanh pho dong danh nhat moi quoc gia ");
        cityList.stream()
                .collect(Collectors.groupingBy(City::getCountryCode))
                .values()
                .stream()
                .map(cities -> Collections.max(cities, Comparator.comparing(City::getPopulation)))
                .forEach(System.out::println);

        System.out.println("Thanh pho dong dan nhat moi luc dia");
        cityMap.values()
                .stream()
                .collect(Collectors.groupingBy(city -> countryMap.get(city.getCountryCode()).getContinent()))
                .values()
                .stream()
                .map(cities -> Collections.max(cities, Comparator.comparing(City::getPopulation)))
                .forEach(city -> System.out.println("Continent " + countryMap.get(city.getCountryCode()).getContinent() + " " + city));

        System.out.println("Thanh pho la thu do dong dan nhat ");
        Optional<City> op = countryMap.values()
                .stream()
                .filter(country -> country.getCapital() != -1)
                .map(country -> cityMap.get(country.getCapital()))
                .max(Comparator.comparing(City::getPopulation));
        System.out.println(op.get());

        System.out.println("Thanh pho la thu do dong dan nhat moi luc dia");
        countryMap.values()
                .stream()
                .filter(country -> country.getCapital() != -1)
                .map(country -> cityMap.get(country.getCapital()))
                .collect(Collectors.groupingBy(city -> countryMap.get(city.getCountryCode()).getContinent()))
                .values()
                .stream()
                .map(cities -> Collections.max(cities, Comparator.comparing(City::getPopulation)))
                .forEach(city -> System.out.println("Continent " + countryMap.get(city.getCountryCode()).getContinent() + "City " + city));

        System.out.println("Xep quoc gia theo so luong thanh pho giam dan:");
        Map<String, Integer> mapCountry = new HashMap<>();
        for (City i : cityList) {
            if (mapCountry.containsKey(i.getCountryCode())) {
                int count = mapCountry.get(i.getCountryCode()) + 1;
                mapCountry.put(i.getCountryCode(), count);
            } else {
                mapCountry.put(i.getCountryCode(), 1);
            }
        }
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(mapCountry.entrySet());
        Collections.sort(entryList, (o1, o2) -> o2.getValue() - o1.getValue());
        for (Map.Entry<String, Integer> i : entryList) {
            for (Country j : countryList) {
                if (i.getKey().equals(j.getCode())) {
                    System.out.println(j + " " + i.getValue());
                }
            }
        }
        System.out.println("Xep quoc gia theo mat do dan so");
        Collections.sort(countryList, (o1, o2) -> (int) (o2.getGnp()*10-o1.getGnp()*10));
        System.out.println(countryList);
    }

}
