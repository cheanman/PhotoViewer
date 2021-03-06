package main;

import app.ImageTextCell;
import app.MetaDataProperty;
import app.Photo;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Photo Viewer Controller.
 *
 * @author Janos Benyovszki
 */
public class PhotoViewerController {

    @FXML
    private VBox landingBackground;

    @FXML
    private ImageView photoImageView;

    @FXML
    private ListView<Photo> photoListView;

    @FXML
    private TextField shutterText;

    @FXML
    private TextField apertureText;

    @FXML
    private TextField isoText;

    @FXML
    private TextArea commentTextArea;

    @FXML
    private Button editButton;

    @FXML
    private Button saveButton;

    private final ObservableList<Photo> photos = FXCollections.observableArrayList();

    private ArrayList<TextInputControl> editables = new ArrayList<>();

    private Photo currentSelection;

    /**
     * Populates the observable list, collects editable controls into
     * one collection, adds listener to the selected list item.
     */
    public void initialize() {
        photos.add(new Photo("Valley", "res/image/cliffside.jpeg"));
        photos.add(new Photo("Forest", "res/image/forest.jpeg"));
        photos.add(new Photo("Meadow", "res/image/meadow.jpeg", "Lush green vegetation"));
        photos.add(new Photo("Mountains", "res/image/mountains.jpeg"));
        photos.add(new Photo(
                "Sunset", "res/image/sunset.jpeg",
                new MetaDataProperty(
                        "1/16",
                        "22",
                        "1600"
                ),
                "Peaceful surroundings")
        );
        photos.add(new Photo("Valley", "res/image/valley.jpeg"));

        editables.add(shutterText);
        editables.add(apertureText);
        editables.add(isoText);
        editables.add(commentTextArea);

        photoListView.setItems(photos);

        photoListView.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> {
                    currentSelection = newValue;
                    var currentMeta = currentSelection.getMetaData();

                    shutterText.setText(currentMeta.getShutterSpeed());
                    apertureText.setText(currentMeta.getAperture());
                    isoText.setText(currentMeta.getIso());
                    commentTextArea.setText(currentSelection.getComment());
                    photoImageView.setImage(new Image(currentSelection.getImagePath()));
                })
        );

        photoListView.setCellFactory(param -> new ImageTextCell());

        setDefault();
    }

    /**
     * For initial selection.
     */
    private void setDefault() {
        photoListView.getSelectionModel().select(0);
    }

    @FXML
    private void enterClicked() {
        var transition = new FadeTransition(new Duration(1000), landingBackground);
        transition.setFromValue(1.0);
        transition.setToValue(0.0);
        transition.play();
        transition.setOnFinished((ActionEvent event) -> {landingBackground.setVisible(false);});
    }

    @FXML
    private void editClicked() {

        for (TextInputControl textControl : editables) {
            textControl.setEditable(true);
        }

        saveButton.setVisible(true);
        editButton.setVisible(false);
    }

    @FXML
    private void saveClicked() {
        for (TextInputControl textControl : editables) {
            textControl.setEditable(false);
        }

        saveData();

        saveButton.setVisible(false);
        editButton.setVisible(true);

    }

    /**
     * Saves metadata and comment to the selected object.
     */
    private void saveData() {
        currentSelection.setMetaData(new MetaDataProperty(shutterText.getText(), apertureText.getText(), isoText.getText()));
        currentSelection.setComment(commentTextArea.getText());
    }

}
