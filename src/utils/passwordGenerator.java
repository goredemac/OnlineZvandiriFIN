/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Random;

/**
 *
 * @author goredemac
 */
public class passwordGenerator {

    public String genPass, genPassNum, genPassChar, genPassSpec;

    public void randomGenerator() {
        genPassNum = generateRandomChars("1234567890", 3);
        genPassSpec = generateRandomChars("!@$%&*(){}+?", 3);
        genPassChar = generateRandomChars("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvzwxyz", 6);
        genPass = generateRandomChars(genPassNum + genPassSpec + genPassChar,8);
        System.out.println(genPass);
    }

    /**
     *
     * @param candidateChars the candidate chars
     * @param length the number of random chars to be generated
     *
     * @return
     */
    public static String generateRandomChars(String candidateChars, int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
        }

        return sb.toString();
    }
}
