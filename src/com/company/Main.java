package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
public class Main {
    static ArrayList<String> namesOfMen = new ArrayList<>(), namesOfWomen = new ArrayList<>();
    static TreeMap<String, TreeMap<String, Integer>> pairsOfNames = new TreeMap<>();

    public static void main(String[] args) {
        Date start =new Date();

        StringBuilder builder = new StringBuilder();
        Scanner sc = null;
        try {
            sc = new Scanner(new File("voyna-i-mir-tom-1-unicode.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (sc.hasNextLine())builder.append(sc.nextLine());
        try {
            sc = new Scanner(new File("men.txt"));
        } catch (FileNotFoundException e) { e.printStackTrace(); }
        while (sc.hasNextLine()) namesOfMen.add(sc.nextLine());
        try {
            sc = new Scanner(new File("women.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (sc.hasNextLine()) {
            String name = sc.nextLine();
            name =Character.toUpperCase(name.charAt(0)) + name.substring(1);
            if (!namesOfMen.contains(name))
                namesOfWomen.add(name);
        }
        System.out.println("men "+namesOfMen.size()+" women "+ namesOfWomen.size());
        int index = builder.indexOf("...");
        while (index != -1) {
            builder.delete(index, index + 4);
            int index1 = index;
            while (Character.isLetter(builder.charAt(index1))) index1++;
            if (Character.isUpperCase(builder.charAt(index1))) builder.insert(index, ".");
            index = builder.indexOf("...");
        }
        ArrayList<String> sentences = new ArrayList<>(Arrays.asList(builder.toString().split("\\.")));
        String chars = "?!;";
        for (int i = 0; i < chars.length(); i++) {
            String c = Character.toString(chars.charAt(i));
            ArrayList<String> temp = new ArrayList<>();
            Iterator<String> iterator = sentences.iterator();
            while (iterator.hasNext()) {
                String sentence = iterator.next();
                if (sentence.contains(c)) {
                    iterator.remove();
                    temp.addAll(Arrays.asList(sentence.split("\\" + chars.charAt(i))));
                }
            }
            sentences.addAll(temp);
            temp.clear();
        }
        TreeMap<String,Integer> topOfMen =new TreeMap<>();
        TreeMap<String,Integer> topOfWomen =new TreeMap<>();
        for (String sentence : sentences) {
            checkContains(sentence);
            for (String word: sentence.split(" ")) {
                for (String m : namesOfMen) if (word.startsWith(m)) writeInMap(topOfMen, m);
                for (String w : namesOfWomen) if (word.startsWith(w)) writeInMap(topOfWomen, w);
            }
        }
        ArrayList<String> keys =new ArrayList<>();
        ArrayList<Integer> val = new ArrayList<>();
        for (Map.Entry<String,TreeMap<String,Integer>> x:pairsOfNames.entrySet()) {
            for (Map.Entry<String, Integer> y : x.getValue().entrySet()) {
                keys.add(x.getKey()+" "+y.getKey());
                val.add(y.getValue());
            }
        }
        String[] top = topTen(keys.toArray(new String[0]),val.toArray(new Integer[0]));
        System.out.println("Топ 10 пар:");
        for (int i = 0; i < top.length; i++) {
            String[] names = top[i].split(" ");
            System.out.println(i+1+". "+names[0]+" и "+names[1]+" - "+pairsOfNames.get(names[0]).get(names[1]));
        }

        System.out.println("\nТоп мужских имен:");
        printTopOf(topOfMen);

        System.out.println("\nТоп женских имен:");
        printTopOf(topOfWomen);

        topOfMen.putAll(topOfWomen);
        System.out.println("\nТоп всех имен:");
        printTopOf(topOfMen);

        System.out.println("\nТоп редких имен:");
        printTopOf(topOfMen,true);

        System.out.println("\n"+(new Date().getTime()-start.getTime())+" ms");

    }
    static void printTopOf(TreeMap<String,Integer> map){printTopOf(map,false);}
    static void printTopOf(TreeMap<String,Integer> map, boolean decrease){
        ArrayList<String> keys =new ArrayList<>(map.keySet());
        ArrayList<Integer> val = new ArrayList<>(map.values());
        String[] top = topTen(keys.toArray(new String[0]),val.toArray(new Integer[0]),decrease);
        for (int i = 0; i < top.length; i++) {
            System.out.println(i+1+". "+top[i]+" - "+map.get(top[i]));
        }
    }
    static void checkContains(String sentence) {
        String[] words = sentence.split(" ");
        ArrayList<String> startWithUpperCase = new ArrayList<>();
        for (String s : words) if (s.length() > 0 && Character.isUpperCase(s.charAt(0))) startWithUpperCase.add(s);
        for (String s : startWithUpperCase) {
            for (String m : namesOfMen) {
                if (!s.startsWith(m)) continue;
                for (String s1 : startWithUpperCase)
                    for (String w : namesOfWomen) {
                        if (s1.startsWith(w)) {
                            writeInMap(pairsOfNames,m,w);
                        }
                    }
            }
        }
    }
    static String[] topTen(String[] keys, Integer[] values){return topTen(keys,values,false);}
    static String[] topTen(String[] keys, Integer[] values, boolean decrease){
        for (int i = 0; i <values.length ; i++) {
            for (int j = i; j<values.length ; j++) {
                int num = decrease?values[j].compareTo(values[i]):values[i].compareTo(values[j]);
                if (num<0){
                    int t=values[j];
                    values[j]=values[i];
                    values[i]=t;
                    String temp=keys[j];
                    keys[j]=keys[i];
                    keys[i]=temp;
                }
            }
        }
        return Arrays.copyOf(keys,10);
    }
    static  void writeInMap(TreeMap<String,TreeMap<String,Integer>> map, String key1, String key2){
        if (map.containsKey(key1)) {
            writeInMap(map.get(key1),key2);
        } else {
            map.put(key1,writeInMap(new TreeMap<>(),key2));
        }
    }
    static  TreeMap<String,Integer> writeInMap(TreeMap<String,Integer> map, String key){
        if (map.containsKey(key)){
            map.put(key,map.get(key)+1);
            return map;
        }else {
            map.put(key,1);
            return map;
        }
    }
}
