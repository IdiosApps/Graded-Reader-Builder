import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import java.io.FileInputStream
import javafx.fxml.FXMLLoader
import javafx.scene.layout.VBox

// Made with the help of @danielt998's H2A + http://code.makery.ch/library/javafx-8-tutorial/part1/

class MainGUI: Application() {

    override fun start(givenStage: Stage) {

        val loader = FXMLLoader()
        val fxmlDocPath = "src/UI.fxml"
        val fxmlStream = FileInputStream(fxmlDocPath)
        val rootLayout = loader.load(fxmlStream) as VBox
        var myController = loader.getController<GUIController>()
        // Show the scene containing the root layout.
        val scene = Scene(rootLayout)

        givenStage.setScene(scene)
        givenStage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(MainGUI::class.java)
}

