/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Eskil Hesselroth
 */
public class checkForInteger {

    public static boolean isInteger(String str) {
        try {
            Float.parseFloat(str);
            return true;

        } catch (Exception e) {
            return false;
        }

    }
}
