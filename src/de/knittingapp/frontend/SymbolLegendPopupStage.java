package de.knittingapp.frontend;

import de.knittingapp.dto.StitchTypes;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * <pre>
 *     template for legend with explanation of symbols in knitting pattern
 * </pre>
 */
public class SymbolLegendPopupStage extends Stage {

    public SymbolLegendPopupStage(Stage owner){
        initOwner(owner);
        GridPane legend = new GridPane();
        legend.setPadding(new Insets(10));
        legend.add(new Label(" Symbol "),0,0);
        legend.add(new Label(" Explanation "),1,0);
        int index = 1;
        for (StitchTypes stitchType : StitchTypes.values()){
            Canvas symbol = new Canvas(20,20);
            GraphicsContext gc = symbol.getGraphicsContext2D();
            stitchType.draw(gc, 0,0, symbol.getWidth(), symbol.getHeight(), 2);
            Label explanation = new Label(stitchType.getExplanation());
            legend.add(symbol,0,index);
            GridPane.setHalignment(symbol, HPos.CENTER);
            legend.add(explanation,1,index);
            index++;
        }
        legend.setGridLinesVisible(true);

        setScene(new Scene(legend));

    }
}
