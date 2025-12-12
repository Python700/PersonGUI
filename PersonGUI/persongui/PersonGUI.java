package persongui;

/**
 * Caleb Murnan
 * occc Advance Java
 * 12/12/25
 * @author caleb
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class PersonGUI extends JFrame {

    private JMenuItem miSave, miSaveAs;
    private File currentFile = null;
    private boolean dirty = false;

    private final DefaultListModel<Person> listModel = new DefaultListModel<>();
    private final JList<Person> personList = new JList<>(listModel);

    public PersonGUI() {
        super("Person File Manager");

        buildMenu();
        buildMainPanel();

        setSize(600, 400);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (confirmSaveBeforeExit()) {
                    System.exit(0);
                }
            }
        });

        setVisible(true);
    }

    private void buildMenu() {
        JMenuBar mb = new JMenuBar();

        // FILE MENU
        JMenu file = new JMenu("File");

        JMenuItem miNew = new JMenuItem("New");
        miNew.addActionListener(e -> doNew());

        JMenuItem miOpen = new JMenuItem("Open...");
        miOpen.addActionListener(e -> doOpen());

        miSave = new JMenuItem("Save");
        miSave.addActionListener(e -> doSave());
        miSave.setEnabled(false);

        miSaveAs = new JMenuItem("Save As...");
        miSaveAs.addActionListener(e -> doSaveAs());
        miSaveAs.setEnabled(false);

        JMenuItem miExit = new JMenuItem("Exit");
        miExit.addActionListener(e -> {
            if (confirmSaveBeforeExit()) System.exit(0);
        });

        file.add(miNew);
        file.add(miOpen);
        file.add(miSave);
        file.add(miSaveAs);
        file.addSeparator();
        file.add(miExit);

        // HELP MENU
        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> JOptionPane.showMessageDialog(
                this, "Person Manager GUI\nOCCC - Final Project"));
        help.add(about);

        mb.add(file);
        mb.add(help);

        setJMenuBar(mb);
    }

    private void buildMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        personList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Buttons
        JPanel buttons = new JPanel();
        JButton addPerson = new JButton("New Person");
        JButton addReg = new JButton("New RegisteredPerson");
        JButton addOccc = new JButton("New OCCCPerson");
        JButton delete = new JButton("Delete Selected");

        addPerson.addActionListener(e -> startCreatePerson("person"));
        addReg.addActionListener(e -> startCreatePerson("reg"));
        addOccc.addActionListener(e -> startCreatePerson("occc"));

        delete.addActionListener(e -> {
            Person p = personList.getSelectedValue();
            if (p != null) {
                listModel.removeElement(p);
                dirty = true;
                updateMenuState();
            }
        });

        buttons.add(addPerson);
        buttons.add(addReg);
        buttons.add(addOccc);
        buttons.add(delete);

        panel.add(new JScrollPane(personList), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        add(panel);
    }

    // -----------------------------------------------------
    //   MENU ACTIONS
    // -----------------------------------------------------

    private void doNew() {
        if (!confirmSaveBeforeExit()) return;

        listModel.clear();
        currentFile = null;
        dirty = false;
        updateMenuState();
    }

    private void doOpen() {
        if (!confirmSaveBeforeExit()) return;

        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentFile = fc.getSelectedFile();
            loadFromFile(currentFile);
        }
    }

    private void doSave() {
        if (currentFile == null) {
            doSaveAs();
            return;
        }
        saveToFile(currentFile);
    }

    private void doSaveAs() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentFile = fc.getSelectedFile();
            saveToFile(currentFile);
        }
    }

    // -----------------------------------------------------
    //   FILE SAVE / LOAD
    // -----------------------------------------------------

    @SuppressWarnings("unchecked")
    private void loadFromFile(File f) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            ArrayList<Person> persons = (ArrayList<Person>) ois.readObject();

            listModel.clear();
            for (Person p : persons) listModel.addElement(p);

            dirty = false;
            updateMenuState();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading file:\n" + ex.getMessage());
        }
    }

    private void saveToFile(File f) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
            ArrayList<Person> persons = new ArrayList<>();
            for (int i = 0; i < listModel.size(); i++) persons.add(listModel.get(i));

            oos.writeObject(persons);

            dirty = false;
            updateMenuState();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving file:\n" + ex.getMessage());
        }
    }

    // -----------------------------------------------------
    //   PERSON CREATION DIALOGS
    // -----------------------------------------------------

    private void startCreatePerson(String type) {
        // While we're in a dialog, disable save options
        miSave.setEnabled(false);
        miSaveAs.setEnabled(false);

        Person p = showPersonDialog(type);

        if (p != null) {
            listModel.addElement(p);
            dirty = true;
        }

        updateMenuState();
    }

    private Person showPersonDialog(String type) {
        JTextField tfFirst = new JTextField(10);
        JTextField tfLast = new JTextField(10);
        JTextField tfMonth = new JTextField(3);
        JTextField tfDay = new JTextField(3);
        JTextField tfYear = new JTextField(5);

        JTextField tfGov = new JTextField(10);
        JTextField tfStu = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(0,2));
        panel.add(new JLabel("First:")); panel.add(tfFirst);
        panel.add(new JLabel("Last:")); panel.add(tfLast);
        panel.add(new JLabel("Month:")); panel.add(tfMonth);
        panel.add(new JLabel("Day:"));   panel.add(tfDay);
        panel.add(new JLabel("Year:"));  panel.add(tfYear);

        if (type.equals("reg") || type.equals("occc")) {
            panel.add(new JLabel("Gov ID:")); panel.add(tfGov);
        }
        if (type.equals("occc")) {
            panel.add(new JLabel("Student ID:")); panel.add(tfStu);
        }

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Create " + type.toUpperCase(), JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) return null;

        // Validate date
        OCCCDate dob;
        try {
            dob = new OCCCDate(
                    Integer.parseInt(tfDay.getText()),
                    Integer.parseInt(tfMonth.getText()),
                    Integer.parseInt(tfYear.getText())
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid Date!");
            return null;
        }

        // Return constructed type
        switch (type) {
            case "person":
                return new Person(tfFirst.getText(), tfLast.getText(), dob);

            case "reg":
                return new RegisteredPerson(
                        tfFirst.getText(), tfLast.getText(), dob, tfGov.getText());

            case "occc":
                return new OCCCPerson(
                        new RegisteredPerson(tfFirst.getText(), tfLast.getText(), dob, tfGov.getText()),
                        tfStu.getText()
                );

            default:
                return null;
        }
    }

    // -----------------------------------------------------
    //   SAVE-BEFORE-EXIT PROMPT
    // -----------------------------------------------------

    private boolean confirmSaveBeforeExit() {
        if (!dirty) return true;

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Save before exit?",
                "Unsaved Changes",
                JOptionPane.YES_NO_CANCEL_OPTION
        );

        if (choice == JOptionPane.CANCEL_OPTION) return false;
        if (choice == JOptionPane.YES_OPTION) {
            doSave();
            return !dirty;
        }

        return true; // NO
    }

    // -----------------------------------------------------
    //   UPDATE MENU STATE
    // -----------------------------------------------------

    private void updateMenuState() {
        miSave.setEnabled(dirty && currentFile != null);
        miSaveAs.setEnabled(dirty);
    }

    // -----------------------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(PersonGUI::new);
    }
}
