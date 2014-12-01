/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DataInsight;

import Model.Item;
import java.util.Comparator;

/**
 *
 * @author Eskil Hesselroth
 */


class AprioriComperator implements Comparator<Item> {

    @Override
    public int compare(Item e1, Item e2) {
        int item1CreatedInt = e1.createdInt;
        int item2CreatedInt = e2.createdInt;
        if (item1CreatedInt < item2CreatedInt) {

            return -1;
        } else if (item1CreatedInt == item2CreatedInt) {

            return 0;

        } else {
            return 1;
        }
    }
}
