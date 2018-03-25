//import GUIController.GUIController
import javafx.application.Application
import javafx.collections.FXCollections
import javafx.scene.Scene
import javafx.stage.Stage
import java.io.FileInputStream
import javafx.fxml.FXMLLoader
import javafx.scene.control.ComboBox
import javafx.scene.layout.VBox

// Made with the help of @danielt998's H2A + http://code.makery.ch/library/javafx-8-tutorial/part1/

class MainGUI: Application() {

    override fun start(givenStage: Stage) {

        val loader = FXMLLoader()
        val fxmlDocPath = "src/GRB_SB_JFX.fxml"
        val fxmlStream = FileInputStream(fxmlDocPath)
        val rootLayout = loader.load(fxmlStream) as VBox
        var myController = loader.getController<GUIController>()
        // Show the scene containing the root layout.
        val scene = Scene(rootLayout)


        myController.setData()

//        var myComboBoxL1 = ComboBox<String>()
//        myComboBoxL1.getItems().addAll(
//                "jacob.smith@example.com",
//                "isabella.johnson@example.com",
//                "ethan.williams@example.com",
//                "emma.jones@example.com",
//                "michael.brown@example.com")


        givenStage.setScene(scene)
        givenStage.show()



//        GUIController.myComboBox.setItems(myComboBoxData);
//        showPersonOverview();
    }



}

fun main(args: Array<String>) {
    Application.launch(MainGUI::class.java)
}

