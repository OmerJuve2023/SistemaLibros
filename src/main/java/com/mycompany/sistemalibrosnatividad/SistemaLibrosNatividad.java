package com.mycompany.sistemalibrosnatividad;

import controller.LibraryController;
import javax.swing.SwingUtilities;

public class SistemaLibrosNatividad {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Código que se ejecutará en el Event Dispatch Thread
                LibraryController controller = new LibraryController();
                controller.run();
            }
        });
    }

}
