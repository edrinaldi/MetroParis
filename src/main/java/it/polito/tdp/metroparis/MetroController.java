/**
 * Sample Skeleton for 'Metro.fxml' Controller Class
 */

package it.polito.tdp.metroparis;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.metroparis.model.Fermata;
import it.polito.tdp.metroparis.model.Model;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

public class MetroController {
	
	Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxArrivo"
    private ComboBox<Fermata> boxArrivo; // Value injected by FXMLLoader

    @FXML // fx:id="boxPartenza"
    private ComboBox<Fermata> boxPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="colFermata"
    private TableColumn<Fermata, String> colFermata; // Value injected by FXMLLoader

    @FXML // fx:id="tablePercorso"
    private TableView<Fermata> tablePercorso; // Value injected by FXMLLoader
    
    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doRicercaPercorso(ActionEvent event) {
    	Fermata partenza = this.boxPartenza.getValue();
    	Fermata arrivo = this.boxArrivo.getValue();
    	if (partenza != null && arrivo != null && !partenza.equals(arrivo)) {
        	List<Fermata> percorso = this.model.calcolaPercorso(partenza, arrivo);
        	this.tablePercorso.setItems(FXCollections.observableArrayList(percorso));
        	this.txtResult.setText("Percorso trovato con " + percorso.size() + " stazioni.");
    	}
    	else {
    		this.txtResult.setText("Devi selezionare 2 stazioni, diverse tra loro\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxArrivo != null : "fx:id=\"boxArrivo\" was not injected: check your FXML file 'Metro.fxml'.";
        assert boxPartenza != null : "fx:id=\"boxPartenza\" was not injected: check your FXML file 'Metro.fxml'.";
        assert colFermata != null : "fx:id=\"colFermata\" was not injected: check your FXML file 'Metro.fxml'.";
        assert tablePercorso != null : "fx:id=\"tablePercorso\" was not injected: check your FXML file 'Metro.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Metro.fxml'.";

        this.colFermata.setCellValueFactory(new PropertyValueFactory<Fermata, String>("nome"));
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	for (Fermata f : this.model.getFermate()) {
    		this.boxPartenza.getItems().add(f);
    	}
    	
    	for (Fermata f : this.model.getFermate()) {
    		this.boxArrivo.getItems().add(f);
    	}
    }

}
