import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SouborFrame extends JFrame {
    private JTextArea taObsah;
    private JPanel pnMain;

    private String cesta;
    private List<String> obsahSouboru = new ArrayList<>();

    public SouborFrame() {
        initComponents();
        initMenu();
    }

    private void initComponents() {
        setContentPane(pnMain);
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Prohlížeč souborů");

        taObsah.setEditable(true);
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuSoubor = new JMenu("File");
        menuBar.add(menuSoubor);

        JMenuItem itemOpen = new JMenuItem("Open...");
        menuSoubor.add(itemOpen);
        itemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        itemOpen.addActionListener(e -> otevriSoubor());


        JMenuItem itemSave = new JMenuItem("Save");
        menuSoubor.add(itemSave);
        itemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        itemSave.addActionListener(e -> ulozitJako(cesta));

        JMenuItem itemSaveAs = new JMenuItem("Save as...");
        menuSoubor.add(itemSaveAs);
        itemSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
        itemSaveAs.addActionListener(e -> ulozitJako(null));
    }

    private void otevriSoubor() {
        JFileChooser fc = new JFileChooser("resources/");
        int vysledek = fc.showOpenDialog(this);
        if (vysledek == JFileChooser.APPROVE_OPTION) {
            File soubor = fc.getSelectedFile();
            //String[] celaCesta = soubor.getAbsolutePath().split("resources" + (Pattern.quote(File.separator)));
            //cesta = "resources/" + celaCesta[1]; //asi nemusí být tak složitě (?)
            cesta = soubor.getPath();
            try(Scanner scanner = new Scanner(new BufferedReader(new FileReader(soubor)))) {
                taObsah.setText(null);
                while (scanner.hasNextLine()) {
                    String radek = scanner.nextLine();
                    obsahSouboru.add(radek);
                    taObsah.append(radek + "\n");
                }
            } catch (FileNotFoundException e) {
                System.err.println("Soubor nenalezen!");
            }
        }
    }

    private void ulozitJako(String cesta) {
        if (cesta != null) {
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(cesta)))) {
                writer.println(taObsah.getText());
            } catch (IOException e) {
                System.err.println("nastala chyba");
            }
        } else {
            JFileChooser fc = new JFileChooser("resources/");
            int vysledek = fc.showOpenDialog(this);
            if (vysledek == JFileChooser.APPROVE_OPTION) {
                File soubor = fc.getSelectedFile();
                cesta = soubor.getPath();
                try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(cesta)))) {
                    writer.println(taObsah.getText());
                } catch (IOException e) {
                    System.err.println("nastala chyba");
                }
            }
        }
    }
}
