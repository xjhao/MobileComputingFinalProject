package edu.wpi.zqinxhao.playerpooling.model.googlePlaces;

import edu.wpi.zqinxhao.playerpooling.model.Game;

/**
 * Created by zishanqin on 4/26/16.
 */
public class GameWithDetailedAddress extends Game {
    private String address=new String();
    public void setAddress(String ads){
        this.address = ads;
    }
}
