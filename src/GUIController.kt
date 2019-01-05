import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXComboBox
import com.jfoenix.controls.JFXTextArea
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Label

class GUIController {

//    @FXML
//    fun buildGR(event: ActionEvent) {
//        info.setPromptText("Pressed build. This is errorlog.")
//        info.setFocusTraversable(false); // set focus traversable false.
//    System.out.println("Button Action");
//        checkXeLaTeX()
//        var argArray: ArrayList<String> = ArrayList<String>()
//        Main.main(argArray)
//    }

    @FXML
    fun clickedBuildButton(event: ActionEvent) {
        println("buildButton pressed")
    }

    fun initialize() {}
}

//    private fun checkXeLaTeX(){
//        errorLog.setPromptText("Checking for xelatex support.")
//        var scanner: java.util.Scanner = java.util.Scanner(Runtime.getRuntime().exec("xelatex").getInputStream()) // .useDelimiter("\\A"
//        var line = scanner.next()
//        var hasXeTeX = (line.contains("This")) // "This is XeTeX, version ....."
//        if (hasXeTeX){errorLog.setPromptText("yay")}
//        else {errorLog.setPromptText("XeTeX not detected. &#10;Download XeTeX here: http://www.texts.io/support/0002/")}
//        errorLog.setFocusTraversable(false); // set focus traversable false.
//    }
//}