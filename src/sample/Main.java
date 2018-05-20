package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        GameMap map = new GameMap(100,50);
        primaryStage.setScene(new Scene(map.root, Param.mapSize, Param.mapSize));
        primaryStage.show();
        synchronized (map.allUnits) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 100000000; j++) {
                        map.doATurn();
                        for (int i = 0; i < map.allUnits.size(); i++) {
                            if ( map.allUnits.get(i).isDead ){
                                map.allUnits.get(i).graphicCircle.setVisible(false);
                                map.allUnits.remove(i);
                                i--;
                            }else {
                                map.allUnits.get(i).graphicCircle.setCenterY(map.allUnits.get(i).Y);
                                map.allUnits.get(i).graphicCircle.setCenterX(map.allUnits.get(i).X);
                            }
                        }
                        try {
                            Thread.sleep(10);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}
