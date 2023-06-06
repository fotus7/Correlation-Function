package org.bsu.CorrelationFunction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.effect.Light.Point;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Stage stage;
    private double imageOneHeight, imageOneWidth, imageTwoHeight, imageTwoWidth;
    private boolean selectAreaButtonClicked = false;
    final private Rectangle selectionOne = new Rectangle();
    final private Rectangle selectionTwo = new Rectangle();
    final private Point anchorOne = new Point();
    final private Point anchorTwo = new Point();
    private double[][][] colorsOne, colorsTwo;
    private final Rectangle selectedArea = new Rectangle();
    @FXML
    private ImageView imageOne, imageTwo;
    @FXML
    private TextField scaleField, maxValueField, shiftValueField;
    @FXML
    private ScrollPane scrollPaneOne, scrollPaneTwo;
    @FXML
    private Button openImgButtonOne, openImgButtonTwo;
    @FXML
    private Button selectAreaButton, dropSelection;
    @FXML
    private Button plusScale, minusScale;
    @FXML
    private Button similaritiesButton, differencesButton;
    @FXML
    private Pane imagePaneOne, imagePaneTwo;
    @FXML
    private ScrollPane lineChartPane;
    @FXML
    private NumberAxis xAxis = new NumberAxis();
    @FXML
    private NumberAxis yAxis = new NumberAxis();
    @FXML
    private LineChart<Number, Number> lineChart;
    @FXML
    private ComboBox<String> colorBox;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void openImgButtonClicked(ActionEvent event) {
        dropSelectionButtonClicked();
        Button button = (Button) event.getSource();
        FileChooser fileChooser = getFileChooser();
        String path = String.valueOf(fileChooser.showOpenDialog(stage));
        imagePaneOne.getChildren().remove(selectionOne);
        imagePaneTwo.getChildren().remove(selectionTwo);
        imagePaneTwo.getChildren().remove(selectedArea);
        Image image = new Image(path);
        if (button.equals(openImgButtonOne)) {
            imageOne.setImage(image);
            imageOne.setFitWidth(image.getWidth());
            imageOne.setFitHeight(image.getHeight());
            imageOneHeight = imageOne.getFitHeight();
            imageOneWidth = imageOne.getFitWidth();
            scrollPaneOne.setStyle("-fx-background-color:transparent;");
        } else {
            imageTwo.setImage(image);
            imageTwo.setFitWidth(image.getWidth());
            imageTwo.setFitHeight(image.getHeight());
            imageTwoHeight = imageTwo.getFitHeight();
            imageTwoWidth = imageTwo.getFitWidth();
            scrollPaneTwo.setStyle("-fx-background-color:transparent;");
        }
        if (imageOne.getImage() != null && imageTwo.getImage() != null) setScale(0);
    }

    public void scaleIncreased() {
        if (!selectAreaButtonClicked && (imageOne.getImage() != null && imageTwo.getImage() != null)) {
            double scale = getScale();
            if (scale < 100) {
                scale += 10;
                setScale(scale);
            }
        }
    }

    public void scaleDecreased() {
        if (!selectAreaButtonClicked && (imageOne.getImage() != null && imageTwo.getImage() != null)) {
            double scale = getScale();
            if (scale > 0) {
                scale -= 10;
                setScale(scale);
            }
        }
    }

    public void selectAreaButtonClicked() {
        selectAreaButtonClicked = true;
        scrollPaneOne.setPannable(false);
        scrollPaneOne.setCursor(Cursor.CROSSHAIR);
    }

    public void dropSelectionButtonClicked() {
        selectAreaButtonClicked = false;
        scrollPaneOne.setPannable(true);
        scrollPaneOne.setCursor(Cursor.DEFAULT);
        imageOne.setOnMousePressed(null);
        imageOne.setOnMouseReleased(null);
        selectionOne.setWidth(0);
        selectionOne.setHeight(0);
        selectionTwo.setWidth(0);
        selectionTwo.setHeight(0);
        imagePaneOne.getChildren().remove(selectionOne);
        imagePaneTwo.getChildren().remove(selectionTwo);
        imagePaneTwo.getChildren().remove(selectedArea);
        lineChart.getData().clear();
        maxValueField.clear();
        shiftValueField.clear();
    }

    public void mouseEnteredPane(MouseEvent event) {
        if (selectAreaButtonClicked) selectArea((Pane) event.getSource());
    }

    public void similaritiesButtonClicked() throws Exception {
        imagePaneTwo.getChildren().remove(selectedArea);
        PixelReader pixelReader = imageOne.getImage().getPixelReader();
        double ratio = ((imageOne.getImage().getWidth() / imageOne.getFitWidth()) + (imageOne.getImage().getHeight() / imageOne.getFitHeight())) / 2;
        Rectangle areaOne = new Rectangle(anchorOne.getX() * ratio, anchorOne.getY() * ratio, selectionOne.getWidth() * ratio, selectionOne.getHeight() * ratio);
        colorsOne = getColor(pixelReader, areaOne);
        pixelReader = imageTwo.getImage().getPixelReader();
        ratio = ((imageTwo.getImage().getWidth() / imageTwo.getFitWidth()) + (imageTwo.getImage().getHeight() / imageTwo.getFitHeight())) / 2;
        Rectangle areaTwo = new Rectangle(anchorTwo.getX() * ratio, anchorTwo.getY() * ratio, selectionTwo.getWidth() * ratio, selectionTwo.getHeight() * ratio);
        colorsTwo = getColor(pixelReader, areaTwo);
        double[][] R = Function.count(colorsOne, colorsTwo);
        double[] maxR = Function.maxR(R);
        setMaxValueField(maxR, "Максимум функции: ");
        selectedArea.setX(selectionTwo.getX() + maxR[1] / ratio);
        selectedArea.setY(selectionTwo.getY() + maxR[2] / ratio);
        selectedArea.setWidth(selectionOne.getWidth());
        selectedArea.setHeight(selectionOne.getHeight());
        setShiftValueField();
        imagePaneTwo.getChildren().add(selectedArea);
        lineChart.getData().add(getSeries(R, maxR));
        draw3DGraph(R);
    }

    public void differencesButtonClicked() throws Exception {
        PixelReader pixelReader = imageTwo.getImage().getPixelReader();
        double ratio = ((imageTwo.getImage().getWidth() / imageTwo.getFitWidth()) + (imageTwo.getImage().getHeight() / imageTwo.getFitHeight())) / 2;
        Rectangle areaTwo = new Rectangle(anchorTwo.getX() * ratio, anchorTwo.getY() * ratio, selectionTwo.getWidth() * ratio, selectionTwo.getHeight() * ratio);
        colorsTwo = getColor(pixelReader, areaTwo);
        double[][] R = Function.count((int) (selectionTwo.getWidth() / 5), (int) (selectionTwo.getHeight() / 5), colorsTwo);
        double[] minR = Function.minR(R);
        setMaxValueField(minR, "Минимум функции: ");
        selectedArea.setX(selectionTwo.getX() + minR[1] / ratio);
        selectedArea.setY(selectionTwo.getY() + minR[2] / ratio);
        //selectedArea.setWidth(selectionTwo.getWidth() / 5);
        //selectedArea.setHeight(selectionTwo.getHeight() / 5);
        //imagePaneTwo.getChildren().add(selectedArea);
        lineChart.getData().add(getSeries(R, minR));
        draw3DGraph(R);
    }

    private void setMaxValueField(double[] R, String text) {
        maxValueField.setText(text + R[0]);

    }

    private void setShiftValueField() {
        int xShift = (int) (Math.abs(((selectionOne.getX() - selectedArea.getX()))) / (1 + getScale() / 100));
        int yShift = (int) (Math.abs(((selectionOne.getY() - selectedArea.getY()))) / (1 + getScale() / 100));
        shiftValueField.setText("Смещение: [X: " + xShift + ", Y: " + yShift + "]");
    }

    private void draw3DGraph(double[][] initR) throws Exception {
        double[][][] R = new double[(int) initR[initR.length - 1][1] + 1][(int) initR[initR.length - 1][2] + 1][3];
        int k = 0;
        for (int i = 0; i < R.length; i++) {
            for (int j = 0; j < R[0].length; j++) {
                R[i][j] = initR[k];
                k++;
            }
        }
        //System.out.println(Arrays.deepToString(R));
        SurfaceDemoSwing graph = new SurfaceDemoSwing();
        graph.graph(R);
    }

    private XYChart.Series<Number, Number> getSeries(double[][] R, double[] peakR) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        System.out.println(R.length);
        System.out.println(peakR[2]);
        for (int i = 0; i <= R[R.length - 1][1]; i++) {
            series.getData().add(new XYChart.Data<>(i, R[(int) (i * (R[R.length - 1][2] + 1) + peakR[2])][0]));
        }
        //series.getData().add(new XYChart.Data<>(peakR[1], peakR[0]));
        //System.out.println(series.getData());
        lineChart.setPrefWidth(series.getData().size());
        xAxis.setUpperBound(series.getData().size());
        return series;
    }

    private double[][][] getColor(PixelReader pixelReader, Rectangle rectangle) {
        int width = (int) rectangle.getWidth();
        int height = (int) rectangle.getHeight();
        Color[][] colors = new Color[width][height];
        double[][][] RGB = new double[width][height][3];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                colors[i][j] = pixelReader.getColor((int) (rectangle.getX() + i), (int) rectangle.getY() + j);
                switch (colorBox.getValue()) {
                    case "Все" -> {
                        RGB[i][j][0] = colors[i][j].getRed();
                        RGB[i][j][1] = colors[i][j].getGreen();
                        RGB[i][j][2] = colors[i][j].getBlue();
                    }
                    case "Красный" -> {
                        RGB[i][j][0] = colors[i][j].getRed();
                        RGB[i][j][1] = 0;
                        RGB[i][j][2] = 0;
                    }
                    case "Зеленый" -> {
                        RGB[i][j][0] = 0;
                        RGB[i][j][1] = colors[i][j].getGreen();
                        RGB[i][j][2] = 0;
                    }
                    case "Синий" -> {
                        RGB[i][j][0] = 0;
                        RGB[i][j][1] = 0;
                        RGB[i][j][2] = colors[i][j].getBlue();
                    }
                }
            }
        }
        return RGB;
    }

    private double getScale() {
        String scaleText = scaleField.getText();
        StringBuilder sb = new StringBuilder(scaleText);
        sb.deleteCharAt(sb.length() - 1);
        return Double.parseDouble(sb.toString());
    }

    private void setScale(double scale) {
        scaleField.setText((int) scale + "%");
        imageOne.setFitHeight(imageOneHeight * (1 + scale / 100));
        imageOne.setFitWidth(imageOneWidth * (1 + scale / 100));
        imageTwo.setFitHeight(imageTwoHeight * (1 + scale / 100));
        imageTwo.setFitWidth(imageTwoWidth * (1 + scale / 100));
    }

    private void selectArea(Pane pane) {
        Point anchor;
        ScrollPane scrollPane;
        ImageView image;
        Rectangle selection;
        if (pane.equals(imagePaneOne)) {
            image = (ImageView) pane.lookup("#imageOne");
            scrollPane = (ScrollPane) pane.lookup("#scrollPaneOne");
            selection = selectionOne;
            anchor = anchorOne;
        } else {
            image = (ImageView) pane.lookup("#imageTwo");
            scrollPane = (ScrollPane) pane.lookup("#scrollPaneTwo");
            selection = selectionTwo;
            anchor = anchorTwo;
        }
        image.setOnMousePressed(mouseEvent -> {
            selection.setHeight(0);
            selection.setWidth(0);
            pane.getChildren().remove(selection);
            pane.getChildren().remove(selectedArea);
            anchor.setX(mouseEvent.getX());
            anchor.setY(mouseEvent.getY());
            selection.setX(mouseEvent.getX() - (image.getFitWidth() - scrollPane.getWidth()) * scrollPane.getHvalue());
            selection.setY(mouseEvent.getY() - (image.getFitHeight() - scrollPane.getHeight()) * scrollPane.getVvalue());
            pane.getChildren().add(selection);
        });
        image.setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.getX() - (image.getFitWidth() - scrollPane.getWidth()) * scrollPane.getHvalue() > scrollPane.getWidth())
                selection.setWidth(scrollPane.getWidth() - selection.getX() - 2);
            else selection.setWidth(Math.abs(mouseEvent.getX() - anchor.getX()));
            if (mouseEvent.getY() - (image.getFitHeight() - scrollPane.getHeight()) * scrollPane.getVvalue() > scrollPane.getHeight())
                selection.setHeight(scrollPane.getHeight() - selection.getY() - 2);
            else selection.setHeight(Math.abs(mouseEvent.getY() - anchor.getY()));
        });
        image.setOnMouseReleased(mouseEvent -> {

        });
    }

    private FileChooser getFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите фотографию");
        fileChooser.setInitialDirectory(new File("src/main/resources/static"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));
        return fileChooser;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scrollPaneOne.hvalueProperty().bindBidirectional(scrollPaneTwo.hvalueProperty());
        scrollPaneOne.vvalueProperty().bindBidirectional(scrollPaneTwo.vvalueProperty());
        scrollPaneOne.cursorProperty().bindBidirectional(scrollPaneTwo.cursorProperty());
        scrollPaneOne.pannableProperty().bindBidirectional(scrollPaneTwo.pannableProperty());
        scrollPaneOne.setPannable(true);
        //imageOne.fitHeightProperty().bindBidirectional(imageTwo.fitHeightProperty());
        //imageOne.fitWidthProperty().bindBidirectional(imageTwo.fitWidthProperty());
        imageOne.cursorProperty().bindBidirectional(imageTwo.cursorProperty());
        imageOne.onMousePressedProperty().bindBidirectional(imageTwo.onMousePressedProperty());
        imageOne.onMouseDraggedProperty().bindBidirectional(imageTwo.onMouseDraggedProperty());
        imageOne.onMouseReleasedProperty().bindBidirectional(imageTwo.onMouseReleasedProperty());
        selectionOne.setFill(null);
        selectionOne.setStroke(Color.RED);
        selectionOne.setStrokeWidth(2);
        selectionTwo.setFill(null);
        selectionTwo.setStroke(Color.RED);
        selectionTwo.setStrokeWidth(2);
        selectedArea.setFill(null);
        selectedArea.setStroke(Color.GREEN);
        selectedArea.setStrokeWidth(2);
        ObservableList<String> colors = FXCollections.observableArrayList("Все", "Красный", "Зеленый", "Синий");
        colorBox.setItems(colors);
        colorBox.setValue("Все");
        //imageOne.setPreserveRatio(true);
        //imageTwo.setPreserveRatio(true);
        //xAxis.setAutoRanging(false);
        //xAxis.setLowerBound(0);
        //xAxis.setUpperBound(100);
        //xAxis.setTickUnit(10);
        //yAxis.setAutoRanging(false);
        //yAxis.setLowerBound(-0.7);
        //yAxis.setUpperBound(1.1);
        //yAxis.setTickUnit(0.1);
    }
}
