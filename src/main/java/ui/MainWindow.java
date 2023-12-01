package ui;

import ui.identity.IdentityResultsWindow;
import ui.identity.MainIdentity;
import ui.mvc.MVCResultsWindow;
import ui.mvc.MainMVC;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame{
    JPanel currentPanel;
    JPanel mainMenuPanel;
    JPanel mainPanel;
    JButton btnMainMenu;
    GridBagConstraints mainConstraints;

    public MainWindow() {
        initialize();
    }

    private void initialize() {
        this.setTitle("Analyzer-Comparator");
        this.setVisible(true);
        this.setSize(600, 580);
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
        btnMainMenu.setIcon(null);
        mainPanel.add(mainMenuPanel, mainConstraints);
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    private void useNewPanel(JPanel smellsPanel) {
        mainPanel.remove(mainMenuPanel);
        currentPanel = smellsPanel;
        Icon arrowIcon = new ArrowIcon(10, 10);
        btnMainMenu.setIcon(arrowIcon);
        mainConstraints.gridy = 1;
        mainPanel.add(currentPanel, mainConstraints);
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    public void initMainPanel(){
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        mainConstraints = new GridBagConstraints();

        btnMainMenu = new JButton("Main Menu");
        Font buttonFont = new Font(btnMainMenu.getFont().getName(), Font.PLAIN, 14);
        btnMainMenu.setFont(buttonFont);
        Dimension buttonSize = new Dimension(150, 30);
        btnMainMenu.setPreferredSize(buttonSize);
        btnMainMenu.setBackground(Color.LIGHT_GRAY);
        btnMainMenu.setOpaque(true);
        btnMainMenu.setBorderPainted(false);
        btnMainMenu.setFocusPainted(false);
        btnMainMenu.addActionListener(new BtnMainMenuListener());

        mainConstraints.gridx = 0;
        mainConstraints.gridy = 0;
        mainPanel.add(btnMainMenu, mainConstraints);
    }

    private JPanel initializeMainMenuPanel() {
        mainMenuPanel = new JPanel();
        JPanel detectorsPanel = new JPanel();
        JPanel resultsPanel = new JPanel();
        JPanel importPanel = new JPanel();

        TitledBorder detectorsBorder = BorderFactory.createTitledBorder("Detect code smells");
        TitledBorder resultsBorder = BorderFactory.createTitledBorder("Results of detections");
        TitledBorder importBorder = BorderFactory.createTitledBorder("Import external results");
        Dimension panelSize = new Dimension(200, 80);
        detectorsPanel.setPreferredSize(panelSize);
        resultsPanel.setPreferredSize(panelSize);
        importPanel.setPreferredSize(panelSize);

        detectorsPanel.setBorder(detectorsBorder);
        resultsPanel.setBorder(resultsBorder);
        importPanel.setBorder(importBorder);

        mainMenuPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;

        constraints.gridx = 0;
        constraints.gridy = 0;
        Dimension buttonSize = new Dimension(150, 20);

        JButton btnIdentity = new JButton("Identity");
        btnIdentity.setPreferredSize(buttonSize);
        btnIdentity.setVerticalTextPosition(AbstractButton.CENTER);
        btnIdentity.setHorizontalTextPosition(AbstractButton.CENTER);
        btnIdentity.addActionListener(new BtnIdentityListener());

        JButton btnMVC = new JButton("MVC");
        btnMVC.setPreferredSize(buttonSize);
        btnMVC.setVerticalTextPosition(AbstractButton.CENTER);
        btnMVC.setHorizontalTextPosition(AbstractButton.CENTER);
        btnMVC.addActionListener(new BtnMVCListener());

        detectorsPanel.add(btnIdentity);
        detectorsPanel.add(btnMVC);


        mainMenuPanel.add(detectorsPanel, constraints);
        constraints.gridy = 1;


        JButton identityResults = new JButton("Identity Results");
        identityResults.setPreferredSize(buttonSize);
        identityResults.setVerticalTextPosition(AbstractButton.CENTER);
        identityResults.setHorizontalTextPosition(AbstractButton.CENTER);
        identityResults.addActionListener(new BtnIdentityResultsListener());

        JButton MVCResults = new JButton("MVC Results");
        MVCResults.setPreferredSize(buttonSize);
        MVCResults.setVerticalTextPosition(AbstractButton.CENTER);
        MVCResults.setHorizontalTextPosition(AbstractButton.CENTER);
        MVCResults.addActionListener(new BtnMVCResultsListener());

        resultsPanel.setLayout(new FlowLayout());
        resultsPanel.add(identityResults);
        resultsPanel.add(MVCResults);

        mainMenuPanel.add(resultsPanel, constraints);
        constraints.gridy = 2;


        JButton readSave = new JButton("Import Results");
        readSave.setPreferredSize(buttonSize);
        readSave.setVerticalTextPosition(AbstractButton.CENTER);
        readSave.setHorizontalTextPosition(AbstractButton.CENTER);
        readSave.addActionListener(new BtnReadSaveListener());

        importPanel.setLayout(new FlowLayout());
        importPanel.add(readSave);

        mainMenuPanel.add(importPanel, constraints);

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
