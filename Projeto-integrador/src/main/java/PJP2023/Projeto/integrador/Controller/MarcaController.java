package PJP2023.Projeto.integrador.Controller;

import PJP2023.Projeto.integrador.Models.Marcas;
import PJP2023.Projeto.integrador.Services.ServiceMarca;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.List;

public class MarcaController {
    @FXML
    private TextField campoMarca;
    @FXML
    private TableView<Marcas> tblMarca;
    @FXML
    private TableColumn<Marcas, String> clnMarca;
    @FXML
    private Button btnExcluirMarca;
    Marcas mar = new Marcas();
    private Stage stage;
    Integer index = -1;

    @FXML
    void initialize() {
        btnExcluirMarca.setDisable(true);
        //Colunas da TableView
        clnMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        this.carregarEnderecos();
        // Configurar o evento de clique duplo na tabela
        tblMarca.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    Marcas mar = tblMarca.getSelectionModel().getSelectedItem();
                    campoMarca.setText(mar.getMarca());
                    index = mar.getId();
                    btnExcluirMarca.setDisable(false);
                    System.out.println(mar.getMarca());
                }
            }
        });
    }

    @FXML
    void SalvarMarca(ActionEvent event) {
        mar.setMarca(campoMarca.getText());
        if(!campoVazio(mar.getMarca())){
            if (index == -1) {
                //Alerta de Inclusao
                Alert alertaSalvar = new Alert(Alert.AlertType.CONFIRMATION);
                alertaSalvar.setTitle("Confirmaçao de Inclusao");
                alertaSalvar.setHeaderText("Deseja incluir novo registro ?");
                alertaSalvar.showAndWait().ifPresent(resposta -> {
                    if (resposta == ButtonType.OK) {
                        //Adicionar novo item a Lista
                        ServiceMarca.inserirMarcas(mar);
                        stage.close();
                    }
                });
            }else {
                Alert alertaSalvar = new Alert(Alert.AlertType.CONFIRMATION);
                alertaSalvar.setTitle("Confirmaçao de Alteraçao");
                alertaSalvar.setHeaderText("Deseja Alterar o registro ?");
                alertaSalvar.showAndWait().ifPresent(resposta -> {
                    if (resposta == ButtonType.OK) {
                        //Atualiza item na lista
                        ServiceMarca.atualizarMarcas(index, mar);
                    }
                });
            }
        }else{
            campoMarca.setStyle("-fx-background-color: pink;");
        }
    }
    @FXML
    void CancelarMarca(ActionEvent event) {
        stage.close();
    }
    @FXML
    void ExcluirMarca(ActionEvent event) {
        Alert alertaExcluir = new Alert(Alert.AlertType.CONFIRMATION);
        alertaExcluir.setTitle("Confirmaçao de Exclusão");
        alertaExcluir.setHeaderText("Deseja Excluir o registro ?");
        alertaExcluir.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                ServiceMarca.deletarMarcas(index);
                index = -1;
                stage.close();
            }
        });
    }
    //Recebe parametros da janela marcas
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void carregarEnderecos() {
        try {
            tblMarca.getItems().remove(0, tblMarca.getItems().size());
            List<Marcas> marcasList = ServiceMarca.carregarMarcas();
            tblMarca.getItems().addAll(marcasList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean campoVazio (String texto){
        if (texto == null || texto.trim().isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }
}
