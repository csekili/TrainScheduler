package hu.bme.mit.inf.scheduler.gui;

import javafx.scene.image.Image;

public class EndSectionHolder extends SectionHolder {

    public EndSectionHolder(String name) {
        super(name);
        sectionImageURI = "hu/bme/mit/inf/scheduler/gui/EndSectionNormal.png";
        sectionImage.setImage(new Image(sectionImageURI));
    }
}
