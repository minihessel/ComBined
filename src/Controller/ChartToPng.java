/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import static Controller.MainFXMLController.stage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 * 
 * @author Eskil Hesselroth
 */
public class ChartToPng {
     final FileChooser fileChooser = new FileChooser();
     
    
    public void saveChartToPNG(Image image) throws IOException{
         File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            }

    }
    
}
