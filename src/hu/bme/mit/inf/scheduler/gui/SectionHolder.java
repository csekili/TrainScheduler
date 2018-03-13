package hu.bme.mit.inf.scheduler.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;


public class SectionHolder {
    protected GridPane panel = new GridPane();
    protected String sectionName;
    protected String sectionImageURI = "hu/bme/mit/inf/scheduler/gui/SectionNormal.png";
    protected boolean isTrainHere = false;
    ImageView sectionImage = new ImageView();
    ImageView trainImage;

    public SectionHolder(String name) {
        sectionName=name;

        trainImage = new ImageView();
        trainImage.setImage(new Image("hu/bme/mit/inf/scheduler/gui/Train.png"));
        trainImage.setVisible(false);
        panel.add(trainImage, 0, 0);

        sectionImage.setImage(new Image(sectionImageURI));
        panel.add(sectionImage, 0, 1);

        Text nameText = new Text(sectionName);
        nameText.setStyle("-fx-font-family: Roboto, \"Segoe UI\",  sans-serif; -fx-font-size: 10px;");
        panel.add(nameText, 0, 2);

    }

    public GridPane getPanel() {
        return panel;
    }

    void setTrainHere(boolean isHere) {
        isTrainHere = isHere;
        trainImage.setVisible(isHere);
    }
}
