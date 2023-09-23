package ui.mvc;

import ui.CodeSmellGeneralPanel;
import ui.ConfigurationMenuPanel;
import ui.mvc.brainController.BrainControllerPanel;
import ui.mvc.brainRepository.BrainRepositoryPanel;
import ui.mvc.fatRepository.FatRepositoryPanel;
import ui.mvc.laboriousRepositoryMethod.LaboriousRepositoryMethodPanel;
import ui.mvc.meddlingService.MeddlingServicePanel;
import ui.mvc.promiscuousController.PromiscuousControllerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMVC extends JPanel{
    JPanel currentPanel;
    JPanel mainMenuPanel;
    JPanel mainPanel;
    GridBagConstraints mainConstraints;

    public MainMVC() {
        initialize();
    }

    private void initialize() {
        currentPanel = initializeMainMenuPanel();
        initMainPanel();
        mainConstraints.gridy = 1;
        mainPanel.add(currentPanel, mainConstraints);
        add(mainPanel);
    }

    public void reinitMainMenu() {
        mainPanel.remove(currentPanel);
        mainConstraints.gridy = 1;
        mainPanel.add(mainMenuPanel, mainConstraints);
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    private void useNewPanel(CodeSmellGeneralPanel generalPanel) {
        mainPanel.remove(mainMenuPanel);
        currentPanel = new ConfigurationMenuPanel(generalPanel);
        mainConstraints.gridy = 1;
        mainPanel.add(currentPanel, mainConstraints);
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    public void initMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        mainConstraints = new GridBagConstraints();
        mainConstraints.anchor = GridBagConstraints.WEST;
        mainConstraints.insets = new Insets(0, 10, 10, 10);

        JButton btnMainMenu = new JButton("MVC Menu");
        btnMainMenu.setBackground(Color.LIGHT_GRAY);
        btnMainMenu.setOpaque(true);
        btnMainMenu.setBorderPainted(false);
        btnMainMenu.addActionListener(new BtnMainMVCMenuListener());

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

        JButton btnPromiscuousController = new JButton("Promiscuous Controller");
        btnPromiscuousController.addActionListener(new BtnPromiscuousControllerListener());
        mainMenuPanel.add(btnPromiscuousController, constraints);

        JButton btnBrainController = new JButton("Brain Controller");
        btnBrainController.addActionListener(new BtnBrainControllerListener());
        constraints.gridy = 1;
        mainMenuPanel.add(btnBrainController, constraints);

        JButton btnMeddlingService = new JButton("Meddling Service");
        btnMeddlingService.addActionListener(new BtnMeddlingServiceListener());
        constraints.gridy = 2;
        mainMenuPanel.add(btnMeddlingService, constraints);

        JButton btnBrainRepository = new JButton("Brain Repository");
        btnBrainRepository.addActionListener(new MainMVC.BtnBrainRepositoryListener());
        constraints.gridy = 3;
        mainMenuPanel.add(btnBrainRepository, constraints);

        JButton btnLaboriousRepoMethod = new JButton("Laborious Repository Method");
        btnLaboriousRepoMethod.addActionListener(new BtnLaboriousRepositoryMethodListener());
        constraints.gridy = 4;
        mainMenuPanel.add(btnLaboriousRepoMethod, constraints);

        JButton btnFatRepository = new JButton("Fat Repository");
        btnFatRepository.addActionListener(new BtnLFatRepositoryListener());
        constraints.gridy = 5;
        mainMenuPanel.add(btnFatRepository, constraints);

        JButton btnAllMVC = new JButton("All MVC");
        btnAllMVC.addActionListener(new BtnAllMVCListener());
        constraints.gridy = 6;
        mainMenuPanel.add(btnAllMVC, constraints);

        return mainMenuPanel;
    }

    private class BtnMainMVCMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            reinitMainMenu();
        }
    }

    private class BtnPromiscuousControllerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            useNewPanel(new PromiscuousControllerPanel());
        }
    }

    private class BtnBrainControllerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            useNewPanel(new BrainControllerPanel());
        }
    }

    private class BtnMeddlingServiceListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            useNewPanel(new MeddlingServicePanel());
        }
    }

    private class BtnBrainRepositoryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            useNewPanel(new BrainRepositoryPanel());
        }
    }

    private class BtnLaboriousRepositoryMethodListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            useNewPanel(new LaboriousRepositoryMethodPanel());
        }
    }

    private class BtnLFatRepositoryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            useNewPanel(new FatRepositoryPanel());
        }
    }

    private class BtnAllMVCListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            useNewPanel(new AllMVCPanel());
        }
    }
}