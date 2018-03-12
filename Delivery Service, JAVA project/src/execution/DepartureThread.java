package execution;

import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import gui.PaddingWindows;
import transport.Transport;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;

public class DepartureThread{

    public DepartureThread(ProgressBar progressBar, Button cancelButton, Data data, ObservableList<TransportTableStuff> transportTableStuff, LinkedList<Transport> list) throws IOException{
        Service thread = new Service<Integer>() {
            @Override
            public Task createTask() {
                return new Task<Integer>() {
                    @Override
                    public Integer call() throws InterruptedException, IOException {
                        int i;
                        cancelButton.setDisable(false);


                        for (i = 0; i < 1000; i++) {
                            updateProgress(i, 1000);
                            Thread.sleep(10);
                        }
                        try {
                            data.departure(list);
                        } catch (ParseException ex) {}
                        progressBar.progressProperty().unbind();
                        progressBar.setProgress(0f);
                        cancelButton.setDisable(true);
                        PaddingWindows.refreshTransportTable(data, transportTableStuff, list);

                        return i;
                    }
                };
            }
        };

        cancelButton.setOnAction(e -> {
            thread.cancel();
            progressBar.progressProperty().unbind();
            progressBar.setProgress(0f);
            cancelButton.setDisable(true);
        });

        progressBar.progressProperty().bind(thread.progressProperty());
        thread.restart();
    }
}
