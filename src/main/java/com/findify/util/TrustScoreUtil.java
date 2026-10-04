package com.findify.util;

import java.util.HashSet;
import java.util.Set;

public class TrustScoreUtil {

    public static int calculateTrustScore(String description, String proof) {

        if (description == null || proof == null) {
            return 0;
        }

        description = description.toLowerCase();
        proof = proof.toLowerCase();

        String[] ignore = {
        	    "the","is","a","an","and","or","of","to",
        	    "in","on","my","this","that","it","has","have",
        	    "with","for","was","were","at","by","from",
        	    "i","me","there","there's"
        	};

        Set<String> stopWords = new HashSet<>();

        for(String word : ignore){
            stopWords.add(word);
        }

        Set<String> keywords = new HashSet<>();
        Set<String> proofWords=new HashSet<>(); 
        for(String word : description.split("\\s+")){

            word = word.replaceAll("'s$", "");
            word = word.replaceAll("[^a-z0-9]", "");

            if(word.length() > 2 && !stopWords.contains(word)){
                keywords.add(word);
            }
        }
        
    for(String word : proof.split("\\s+")){

        word = word.replaceAll("'s$", "");
        word = word.replaceAll("[^a-z0-9]", "");

        if(word.length() > 2 && !stopWords.contains(word)){
            proofWords.add(word);
        }
    }

        
        
        
        int matched = 0;

        for(String word : keywords){

            if(proofWords.contains(word)){
                matched++;
            }

        }

        if(keywords.isEmpty()){
            return 0;
        }

        return (matched * 100) / keywords.size();
    }

 

}