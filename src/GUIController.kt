import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXComboBox
import com.jfoenix.controls.JFXTextArea
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.ComboBox

class GUIController {

    @FXML
    val build: JFXButton? = null

    @FXML
    val l2Selector: JFXComboBox<*>? = null

    @FXML
    val l1Selector: JFXComboBox<*>? = null

    @FXML
    val errorLog: JFXTextArea? = null

    @FXML
    fun buildGR(event: ActionEvent) {
    System.out.println("Button Action");
    }

    @FXML
    fun sendL1(event: ActionEvent) {
    }

    @FXML
    fun sendL2(event: ActionEvent) {
    }

    @FXML
    val myComboBoxL1 = ComboBox<String>()
    @FXML
    val myComboBoxL2 = ComboBox<String>()

    fun setData(){
    myComboBoxL1.getItems().clear()

    myComboBoxL1.getItems().addAll(
    "jacob.smith@example.com",
    "isabella.johnson@example.com",
    "ethan.williams@example.com",
    "emma.jones@example.com",
    "michael.brown@example.com")
     }

//    var options = FXCollections.observableArrayList(
//            "English",
//            "Chinese (Mandarin)",
//            "Option 3"
//    )
//
//    @FXML
//    val myComboBoxL1 = ComboBox(options)
//
//    val myComboBoxL2 = ComboBox(options)


    fun initialize() {
        println("Label is null? ")
        setData(); println("setData")
//        val myComboBoxL1 = ComboBox(options)
    }

    // Init ComboBox items
//    myComboBoxL1.setItems(myComboBoxData)



}
