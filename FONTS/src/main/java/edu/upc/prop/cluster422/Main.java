package edu.upc.prop.cluster422;

import edu.upc.prop.cluster422.Presentacio.Controladors.CtrlPresentacio;

import java.io.IOException;

public class Main {
    private static CtrlPresentacio ctrlPresentacio;

    public static void main(String[] args) throws IOException {
        ctrlPresentacio = CtrlPresentacio.getInstance();
        ctrlPresentacio.iniciarPresentacio();
    }
}
