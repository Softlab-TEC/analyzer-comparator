package ui;

import ui.identity.IdentityResultsWindow;
import ui.identity.MainIdentity;
import ui.mvc.MVCResultsWindow;
import ui.mvc.MainMVC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame{
    JPanel currentPanel;
    JPanel mainMenuPanel;
    JPanel mainPanel;
    GridBagConstraints mainConstraints;

    public MainWindow() {
        initialize();
    }

    private void initialize() {
        this.setTitle("Analyzer-Comparator");
        this.setVisible(true);
        this.setSize(780, 2000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentPanel = initializeMainMenuPanel();
        initMainPanel();
        mainConstraints.gridy = 1;
        mainPanel.add(currentPanel, mainConstraints);
        add(mainPanel);
    }

    public void reinitMainMenu(){
        mainPanel.remove(currentPanel);
        mainConstraints.gridy = 1;
        mainPanel.add(mainMenuPanel, mainConstraints);
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    private void useNewPanel(JPanel smellsPanel) {
        mainPanel.remove(mainMenuPanel);
        currentPanel = smellsPanel;
        mainConstraints.gridy = 1;
        mainPanel.add(currentPanel, mainConstraints);
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    public void initMainPanel(){
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        mainConstraints = new GridBagConstraints();
        mainConstraints.anchor = GridBagConstraints.WEST;
        mainConstraints.insets = new Insets(0, 10, 10, 10);

        JButton btnMainMenu = new JButton("Main Menu");
        btnMainMenu.setBackground(Color.GRAY);
        btnMainMenu.setOpaque(true);
        btnMainMenu.setBorderPainted(false);
        btnMainMenu.addActionListener(new BtnMainMenuListener());

        mainConstraints.gridx = 0;
        mainConstraints.gridy = 0;
        mainPanel.add(btnMainMenu, mainConstraints);
    }

    private JPanel initializeMainMenuPanel() {
        mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;

        JButton btnIdentity = new JButton("Identity");
        btnIdentity.addActionListener(new BtnIdentityListener());
        mainMenuPanel.add(btnIdentity, constraints);

        JButton btnMVC = new JButton("MVC");
        btnMVC.addActionListener(new BtnMVCListener());
        constraints.gridy = 1;
        mainMenuPanel.add(btnMVC, constraints);

        JButton identityResults = new JButton("Identity Results");
        identityResults.addActionListener(new BtnIdentityResultsListener());
        constraints.gridy = 2;
        mainMenuPanel.add(identityResults, constraints);

        JButton MVCResults = new JButton("MVC Results");
        MVCResults.addActionListener(new BtnMVCResultsListener());
        constraints.gridy = 3;
        mainMenuPanel.add(MVCResults, constraints);

        JButton readSave = new JButton("Read and Save Results");
        readSave.addActionListener(new BtnReadSaveListener());
        constraints.gridy = 4;
        mainMenuPanel.add(readSave, constraints);

        return mainMenuPanel;
    }

    private class BtnMainMenuListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            reinitMainMenu();
        }
    }

    private class BtnIdentityListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            useNewPanel(new MainIdentity());
        }
    }

    private class BtnMVCListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) { useNewPanel(new MainMVC()); }
    }

    private class BtnReadSaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) { useNewPanel(new ReadSavePanel().getPanel()); }
    }

    private class BtnMVCResultsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) { useNewPanel(new MVCResultsWindow().getPanel()); }
    }

    private class BtnIdentityResultsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) { useNewPanel(new IdentityResultsWindow().getPanel()); }
    }

}
