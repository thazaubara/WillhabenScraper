package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

public class Controller {

    @FXML
    private Button btn;
    @FXML private ListView listView_filter;
    @FXML private TextField textfield_input;

    private ObservableList<String> preisliste_items;

    //button press
    @FXML private void getData(ActionEvent event){
        System.out.println("Button pressed!");
        preisliste_items = parseHTML("http://www.fotoversicherung.com/fotoversicherung/gebrauchtpreisliste-nikon/");
        listView_filter.setItems(preisliste_items);
    };

    //enter bestätigt
    @FXML private void onEnterPressed(KeyEvent event){
        ObservableList<String> search_items = FXCollections.observableArrayList();
        if (event.getCode().equals(KeyCode.ENTER)) {
            if (!textfield_input.getText().toString().equals("")){
                for (String name : preisliste_items){
                    if(name.toLowerCase().contains(textfield_input.getText().toString().toLowerCase())){
                        search_items.add(name);
                    }
                }
                listView_filter.setItems(search_items);
            }
            else{
                listView_filter.setItems(preisliste_items);
            }
        }
    }

    private ObservableList<String> parseHTML(String url){
        ObservableList<String> list = FXCollections.observableArrayList();
        //File input = new File("D:\\Mein Zeug\\#MEINEARBEIT\\IntelliJ IDEA\\html\\web.html");
        File input = new File("/Users/Zaubara/Desktop/web.html");
        try {
            Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
            //Document doc = Jsoup.connect(url).get();

            for (Element table: doc.select("table")){
                for (Element row: table.select("tr")){
                    if (row.hasText()) {
                        Elements tds = row.select("td ");
                        //exclude fault in website
                        if (!tds.get(0).text().equals("AF-I TC-14E AF-I TC-14E 125 € AF-S TC-14E II 210 € AF-S TC-14E " +
                                "III 355 € AF-S TC-17E II 195 € AF-I TC-20E 140 € AF-S TC-20E II 195 € AF-S TC-20E III " +
                                "310 € AF-S TC800-1.25E ED (Set-Artikel) X")) {
                            list.add(list.size(),tds.get(0).text() + "  (" + tds.get(1).text() + ")");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


}