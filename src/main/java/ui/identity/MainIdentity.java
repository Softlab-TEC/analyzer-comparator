package ui.identity;

import ui.ArrowIcon;
import ui.CodeSmellGeneralPanel;
import ui.ConfigurationMenuPanel;
import ui.identity.brainClass.BrainClassPanel;
import ui.identity.brainMethod.BrainMethodPanel;
import ui.identity.dataClass.DataClassPanel;
import ui.identity.featureEnvy.FeatureEnvyPanel;
import ui.identity.godClass.GodClassPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainIdentity extends JPanel{
    JPanel currentPanel;
    JPanel mainMenuPanel;
    JPanel mainPanel;
    JButton btnMainMenu;
    GridBagConstraints mainConstraints;

    public MainIdentity() {
        initialize();
    }

    private void initialize() {
        currentPanel = initializeMainMenuPanel();
        initMainPanel();
        mainConstraints.gridy = 1;
        btnMainMenu.setIcon(null);
        mainPanel.add(currentPanel, mainConstraints);
        add(mainPanel);
    }

    public void reinitMainMenu() {
        mainPanel.remove(currentPanel);
        mainConstraints.gridy = 1;
        btnMainMenu.setIcon(null);
        mainPanel.add(mainMenuPanel, mainConstraints);
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    private void useNewPanel(CodeSmellGeneralPanel generalPanel) {
        mainPanel.remove(mainMenuPanel);
        currentPanel = new ConfigurationMenuPanel(generalPanel);
        Icon arrowIcon = new ArrowIcon(10, 10);
        btnMainMenu.setIcon(arrowIcon);
        mainConstraints.gridy = 1;
        mainPanel.add(currentPanel, mainConstraints);
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    public void initMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        mainConstraints = new GridBagConstraints();
        mainConstraints.anchor = GridBagConstraints.CENTER;
        mainConstraints.insets = new Insets(0, 10, 10, 10);

        btnMainMenu = new JButton("Identity Menu");
        Dimension buttonSize = new Dimension(150, 30);
        btnMainMenu.setPreferredSize(buttonSize);
        btnMainMenu.setBackground(Color.LIGHT_GRAY);
        btnMainMenu.setOpaque(true);
        btnMainMenu.setBorderPainted(false);
        btnMainMenu.setFocusPainted(false);
        btnMainMenu.addActionListener(new BtnMainIdentityMenuListener());

        mainConstraints.gridx = 0;
        mainConstraints.gridy = 0;
        mainPanel.add(btnMainMenu, mainConstraints);
    }

    private JPanel initializeMainMenuPanel() {
        mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        Dimension buttonSize = new Dimension(150, 20);

        JButton btnGodClass = new JButton("God Class");
        btnGodClass.setPreferredSize(buttonSize);
        btnGodClass.setVerticalTextPosition(AbstractButton.CENTER);
        btnGodClass.setHorizontalTextPosition(AbstractButton.CENTER);
        btnGodClass.addActionListener(new MainIdentity.BtnGodClassListener());
        mainMenuPanel.add(btnGodClass, constraints);

        JButton btnFeatureEnvy = new JButton("Feature Envy");
        btnFeatureEnvy.setPreferredSize(buttonSize);
        btnFeatureEnvy.setVerticalTextPosition(AbstractButton.CENTER);
        btnFeatureEnvy.setHorizontalTextPosition(AbstractButton.CENTER);
        btnFeatureEnvy.addActionListener(new MainIdentity.BtnFeatureEnvy());
        constraints.gridy = 1;
        mainMenuPanel.add(btnFeatureEnvy, constraints);

        JButton btnDataClass = new JButton("Data Class");
        btnDataClass.setPreferredSize(buttonSize);
        btnDataClass.setVerticalTextPosition(AbstractButton.CENTER);
        btnDataClass.setHorizontalTextPosition(AbstractButton.CENTER);
        btnDataClass.addActionListener(new MainIdentity.BtnDataClass());
        constraints.gridy = 2;
        mainMenuPanel.add(btnDataClass, constraints);

        JButton btnBrainMethod = new JButton("Brain Method");
        btnBrainMethod.setPreferredSize(buttonSize);
        btnBrainMethod.setVerticalTextPosition(AbstractButton.CENTER);
        btnBrainMethod.setHorizontalTextPosition(AbstractButton.CENTER);
        btnBrainMethod.addActionListener(new MainIdentity.BtnBrainMethod());
        constraints.gridy = 3;
        mainMenuPanel.add(btnBrainMethod, constraints);

        JButton btnBrainClass = new JButton("Brain Class");
        btnBrainClass.setPreferredSize(buttonSize);
        btnBrainClass.setVerticalTextPosition(AbstractButton.CENTER);
        btnBrainClass.setHorizontalTextPosition(AbstractButton.CENTER);
        btnBrainClass.addActionListener(new MainIdentity.BtnBrainClass());
        constraints.gridy = 4;
        mainMenuPanel.add(btnBrainClass, constraints);

        JButton btnAllIdentity = new JButton("All Identity");
        btnAllIdentity.setPreferredSize(buttonSize);
        btnAllIdentity.setVerticalTextPosition(AbstractButton.CENTER);
        btnAllIdentity.setHorizontalTextPosition(AbstractButton.CENTER);
        btnAllIdentity.addActionListener(new MainIdentity.BtnAllIdentityListener());
        constraints.gridy = 5;
        mainMenuPanel.add(btnAllIdentity, constraints);

        return mainMenuPanel;
    }


    private class BtnMainIdentityMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            reinitMainMenu();
        }
    }

    private class BtnGodClassListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            useNewPanel(new GodClassPanel());
        }
    }

    private class BtnFeatureEnvy implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            useNewPanel(new FeatureEnvyPanel());
        }
    }

    private class BtnDataClass implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            useNewPanel(new DataClassPanel());
        }
    }

    private class BtnBrainMethod implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            useNewPanel(new BrainMethodPanel());
        }
    }

    private class BtnBrainClass implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            useNewPanel(new BrainClassPanel());
        }
    }
    private class BtnAllIdentityListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            useNewPanel(new AllIdentityPanel());
        }
    }
}