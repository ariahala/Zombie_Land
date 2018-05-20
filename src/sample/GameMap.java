package sample;

import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mac on 2/6/2017 AD.
 */
public class GameMap {
    public ArrayList<Unit> allUnits = new ArrayList<>();
    public Group root;

    public GameMap(int numberOfHumans , int numberOfZombies){
        root = new Group();
        Rectangle tempRect = new Rectangle(0,0, Param.mapSize,Param.mapSize);
        tempRect.setFill(Paint.valueOf("Transparent"));
        tempRect.setStroke(Paint.valueOf("red"));
        root.getChildren().add(tempRect);
        for ( int i = 0 ; i < numberOfHumans ; i++ ){
            allUnits.add(new Unit(i,true,new Random().nextDouble()*Param.mapSize ,new Random().nextDouble()*Param.mapSize,root));
        }
        for ( int i = numberOfHumans ; i < numberOfHumans+numberOfZombies ; i++){
            allUnits.add(new Unit(i,false,new Random().nextDouble()*Param.mapSize ,new Random().nextDouble()*Param.mapSize,root));
        }
    }
    public void doATurn(){
        for ( int i = 0 ; i < allUnits.size() ; i++ ){
            allUnits.get(i).doYourShit(this.allUnits);
        }
        for ( int i = 0 ; i < allUnits.size() ; i++ ){
            if ( allUnits.get(i).HP < 0 ){
                if ( allUnits.get(i).isOk){
                    allUnits.get(i).turnIntoZombie();
                }else{
                    allUnits.get(i).isDead = true;
                }
            }
        }
    }
}
