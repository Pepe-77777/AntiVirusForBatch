package br.com.pepe.bat.antivirus;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Check {

    public Check() throws AWTException, IOException {

        final File folder = new File("C:/Users/angel/Desktop");

        for (final File fileEntry : folder.listFiles()) {

            if (fileEntry.getName().toLowerCase().endsWith(".bat") || fileEntry.getName().toLowerCase().endsWith(".cmd")) {

                if (Read(fileEntry.getPath()).toLowerCase().contains("taskkill")) {

                    System.out.println("Uma ameaça foi detectada.");

                    notifyVirus("Uma ameaça foi detectada. (Nivel: Baixa)", TrayIcon.MessageType.WARNING);

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    notifyVirus("Arquivo: " + fileEntry.getPath(), TrayIcon.MessageType.INFO);

                } else if (Read(fileEntry.getPath()).toLowerCase().contains("[hkey")) {

                    System.out.println("Uma ameaça foi detectada.");

                    notifyVirus("Uma ameaça foi detectada. (Nivel: Alta)", TrayIcon.MessageType.WARNING);

                    System.out.println("Arquivo Deletado!");

                    Runtime.getRuntime().exec("taskkill /f /im cmd.exe");
                    fileEntry.setExecutable(false);

                    notifyVirus("Arquivo: " + fileEntry.getPath() + " deletado automaticamente!", TrayIcon.MessageType.INFO);

                } else {

                    System.out.println("Arquivo " + fileEntry.getPath() + " verificado com sucesso.");

                }

            } else {

                System.out.println("Arquivo " + fileEntry.getPath() + " verificado com sucesso.");

            }

        }

    }

    private void notifyVirus(String s, TrayIcon.MessageType type) throws AWTException {

        if (SystemTray.isSupported()) {

            displayTray(s, type);

        } else {

            System.err.println("System Tray is not supported!");

        }

    }

    public void displayTray(String s, TrayIcon.MessageType type) throws AWTException {

        try{
            //Obtain only one instance of the SystemTray object
            SystemTray tray = SystemTray.getSystemTray();

            // If you want to create an icon in the system tray to preview
            Image image = Toolkit.getDefaultToolkit().createImage("some-icon.png");
            //Alternative (if the icon is on the classpath):
            //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

            TrayIcon trayIcon = new TrayIcon(image, s);
            //Let the system resize the image if needed
            trayIcon.setImageAutoSize(true);
            //Set tooltip text for the tray icon
            trayIcon.setToolTip("AntiVirus For Batch");
            tray.add(trayIcon);

            // Display info notification:
            trayIcon.displayMessage("AntiVirus For Batch", s, type);
            // Error:
            // trayIcon.displayMessage("Hello, World", "Java Notification Demo", MessageType.ERROR);
            // Warning:
            // trayIcon.displayMessage("Hello, World", "Java Notification Demo", MessageType.WARNING);
        }catch(Exception ex){
            System.err.print(ex);
        }

    }

    public String Read(String fileName) {
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                return data;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return "404";
        }

        return "";
    }
}
