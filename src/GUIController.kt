import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXComboBox
import com.jfoenix.controls.JFXTextArea
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Label

class GUIController {

    @FXML
    val build: JFXButton? = null

    @FXML
    val myComboBoxL1: JFXComboBox<Label> = JFXComboBox()
    @FXML
    val myComboBoxL2: JFXComboBox<Label> = JFXComboBox()

    @FXML
    val errorLog: JFXTextArea = JFXTextArea()

    @FXML
    val info: JFXTextArea = JFXTextArea()

    @FXML
    fun buildGR(event: ActionEvent) {
        info.setPromptText("Pressed build. This is errorlog.")
        info.setFocusTraversable(false); // set focus traversable false.
    System.out.println("Button Action");
        checkXeLaTeX()
//        var argArray: ArrayList<String> = ArrayList<String>()
//        Main.main(argArray)
    }

    @FXML
    fun sendL1(event: ActionEvent) {
    println("l1 pressed")
    }

    @FXML
    fun sendL2(event: ActionEvent) {
        println("l2 pressed")
    }

    fun setData() {
        myComboBoxL1.getItems().add(Label("Java 1.8"))
        myComboBoxL1.getItems().add(Label("Java 1.7"))
        myComboBoxL1.getItems().add(Label("Java 1.6"))
        myComboBoxL1.getItems().add(Label("Java 1.5"))

        myComboBoxL1.setPromptText("Select Java Version")
    }
//    myComboBoxL1.getItems().clear()
//
//    myComboBoxL1.getItems().addAll(
//    "jacob.smith@example.com",
//    "isabella.johnson@example.com",
//    "ethan.williams@example.com",
//    "emma.jones@example.com",
//    "michael.brown@example.com")
//     }

//    var options = FXCollections.observableArrayList(
//            "English",
//            "Chinese (Mandarin)",
//            "Option 3"
//    )
//    myComboBoxL1.setItems(options)

//    @FXML
//    myComboBoxL1 = JFXComboBox(options)
//
//    myComboBoxL2 = JFXComboBox(options)


    fun initialize() {
        println("Label is null? ")
        setData(); println("setData")
//        val myComboBoxL1 = ComboBox(options)
    }

    private fun checkXeLaTeX(){
        errorLog.setPromptText("Checking for xelatex support.")
        var scanner: java.util.Scanner = java.util.Scanner(Runtime.getRuntime().exec("xelatex").getInputStream()) // .useDelimiter("\\A"
        var line = scanner.next()
        var hasXeTeX = (line.contains("This")) // "This is XeTeX, version ....."
        if (hasXeTeX){errorLog.setPromptText("yay")}
        else {errorLog.setPromptText("XeTeX not detected. &#10;Download XeTeX here: http://www.texts.io/support/0002/")}
        errorLog.setFocusTraversable(false); // set focus traversable false.
    }
}