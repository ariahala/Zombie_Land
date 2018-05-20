package sample;

import javafx.scene.Group;
import javafx.scene.paint.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Amineh.ahm on 2/6/2017 AD. All rights reserved.
 */
public class Unit {
    public boolean isDead = false;
    public double X;
    public double Y;
    public int index;
    public boolean isOk;
    public Circle graphicCircle;
    public double speed;
    public double HP;
    public double zombieParam = -1;
    public double humanParam = 0.01;
    public double fightProbability = Param.fightProbability;

    public Unit(int index , boolean isOk , double X , double Y , Group root) {
        this.index = index;
        this.isOk = isOk;
        if ( isOk ){
            speed = Param.initSpeedHuman;
        }else{
            speed = Param.initSpeedZombie;
        }
        HP = 100;
        this.X = X;
        this.Y = Y;
        if ( isOk ) {
            this.graphicCircle = new Circle(X, Y, 2, javafx.scene.paint.Paint.valueOf("Black"));
        }else{
            this.graphicCircle = new Circle(X, Y, 2, javafx.scene.paint.Paint.valueOf("Red"));
        }
        root.getChildren().add(this.graphicCircle);
    }
    public void move(double xDis , double yDis ){
        if ( (this.X + xDis) > 0 && (this.X + xDis) < Param.mapSize && (this.Y + yDis) < Param.mapSize && (this.Y + yDis) > 0 ) {
            this.X = this.X + xDis;
            this.Y = this.Y + yDis;
        }
    }
    public void turnIntoZombie(){
        HP = 100;
        speed = Param.initSpeedZombie;
        graphicCircle.setFill(Paint.valueOf("Red"));
        isOk = false;
    }
    public void doYourShit (ArrayList<Unit> allUnits){
        if ( !isOk ){
            double distance = Param.mapSize*100;
            double xDirection = 0;
            double yDirection = 0;
            int tmpindex = -1;
            for ( int i = 0 ; i < allUnits.size() ; i++ ){
                if (!allUnits.get(i).isOk)
                    continue;
                if (allUnits.get(i).index == this.index )
                    continue;
                double tempDistance = Math.pow(allUnits.get(i).X-this.X,2.) +  Math.pow(allUnits.get(i).Y-this.Y,2.);
                tempDistance = Math.sqrt(tempDistance);
                if ( distance > tempDistance ){
                    xDirection = allUnits.get(i).X-this.X;
                    yDirection = allUnits.get(i).Y-this.Y;
                    double r = Math.pow(xDirection,2.) + Math.pow(yDirection,2.);
                    r = Math.sqrt(r);
                    if ( r != 0 ) {
                        xDirection /= r;
                        yDirection /= r;
                    }
                    tmpindex = i;
                    distance = tempDistance;
                }
            }
            if ( tmpindex == -1 ){
                System.out.println("!!!!!!!!!!!!!WTF!!!!!!!!!!!!!!");
                xDirection = new Random().nextDouble();
                yDirection = new Random().nextDouble();
                xDirection = xDirection*2 - 1;
                yDirection = yDirection*2 - 1;
                this.move(xDirection*Param.epsilon*speed,yDirection*Param.epsilon*speed);
            }else{

                this.move(xDirection*Param.epsilon*speed+(new Random().nextDouble()*2.)-1,yDirection*Param.epsilon*speed+(new Random().nextDouble()*2.)-1);
                if ( distance < Param.fightDistance){
                    double fightProb = new Random().nextDouble();
                    if ( fightProb < allUnits.get(tmpindex).fightProbability ){
                        this.HP -= Param.zobimbeDamage;
                    }else{
                        allUnits.get(tmpindex).HP -= Param.damage;
                    }
                }
            }
        }else{
            double superTemp = new Random().nextDouble();
            if ( superTemp < 0 ){
                this.graphicCircle.setFill(Paint.valueOf("Brown"));
                this.HP = 300;
                this.fightProbability = Param.superHumanFightProbability;
                this.zombieParam = 1;
                this.humanParam = 0.0001;
            }
            ArrayList<Double> dir = this.getGradiantDirectoin(allUnits);
            double xDirection = dir.get(0);
            double yDirection = dir.get(1);
            this.move(Param.epsilon*xDirection*speed,Param.epsilon*yDirection*speed);
        }
    }
    public ArrayList<Double> getGradiantDirectoin (ArrayList<Unit> allUnits){
        double x = 0;
        double y = 0;
        for ( int i = 0 ; i < allUnits.size() ; i++ ){
            if ( allUnits.get(i).index == this.index )
                continue;
            double temp;
            if ( !allUnits.get(i).isOk )
                temp = zombieParam;
            else
                temp = humanParam;
            temp /= Math.sqrt(2*Math.PI*Math.pow(Param.humanVisionVariance,2.));
            double distance = Math.pow(this.X-allUnits.get(i).X,2.)+Math.pow(this.Y - allUnits.get(i).Y,2.);
            distance = Math.sqrt(distance);
            temp *= Math.exp((-1)*Math.pow(distance,2.)/Math.pow(Param.humanVisionVariance,2.));
            temp *= (1/distance) - 2*distance;

            x += temp*(this.X - allUnits.get(i).X);
            y += temp*(this.Y - allUnits.get(i).Y);
        }
        ArrayList<Double> answer = new ArrayList<>();
        double r = Math.pow(x,2.)+Math.pow(y,2.);
        r = Math.sqrt(r);
        if ( r != 0) {
            x /= r;
            y /= r;
        }
        answer.add(new Double(x));
        answer.add(new Double(y));
        return answer;
    }
}
