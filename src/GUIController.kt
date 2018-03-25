import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXComboBox
import com.jfoenix.controls.JFXTextArea
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Label

class GUIController {

    @FXML
    val build: JFXButton? = null

//    @FXML
//    private val myComboBoxL2: JFXComboBox<*>? = null
//
//    @FXML
//    private val myComboBoxL1: JFXComboBox<*>? = null

    @FXML
    val myComboBoxL1: JFXComboBox<Label> = JFXComboBox()
    @FXML
    val myComboBoxL2: JFXComboBox<Label> = JFXComboBox()

    @FXML
    val errorLog: JFXTextArea? = null

    @FXML
    fun buildGR(event: ActionEvent) {
    System.out.println("Button Action");
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

    // Init ComboBox items




}
