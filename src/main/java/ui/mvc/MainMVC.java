package ui.mvc;

import ui.ArrowIcon;
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
    JButton btnMainMenu;
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

        btnMainMenu = new JButton("MVC Menu");
        Dimension buttonSize = new Dimension(150, 30);
        btnMainMenu.setPreferredSize(buttonSize);
        btnMainMenu.setBackground(Color.LIGHT_GRAY);
        btnMainMenu.setOpaque(true);
        btnMainMenu.setBorderPainted(false);
        btnMainMenu.setFocusPainted(false);
        btnMainMenu.addActionListener(new BtnMainMVCMenuListener());

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
        Dimension buttonSize = new Dimension(200, 20);

        JButton btnPromiscuousController = new JButton("Promiscuous Controller");
        btnPromiscuousController.setPreferredSize(buttonSize);
        btnPromiscuousController.setVerticalTextPosition(AbstractButton.CENTER);
        btnPromiscuousController.setHorizontalTextPosition(AbstractButton.CENTER);
        btnPromiscuousController.addActionListener(new BtnPromiscuousControllerListener());
        mainMenuPanel.add(btnPromiscuousController, constraints);

        JButton btnBrainController = new JButton("Brain Controller");
        btnBrainController.setPreferredSize(buttonSize);
        btnBrainController.setVerticalTextPosition(AbstractButton.CENTER);
        btnBrainController.setHorizontalTextPosition(AbstractButton.CENTER);
        btnBrainController.addActionListener(new BtnBrainControllerListener());
        constraints.gridy = 1;
        mainMenuPanel.add(btnBrainController, constraints);

        JButton btnMeddlingService = new JButton("Meddling Service");
        btnMeddlingService.setPreferredSize(buttonSize);
        btnMeddlingService.setVerticalTextPosition(AbstractButton.CENTER);
        btnMeddlingService.setHorizontalTextPosition(AbstractButton.CENTER);
        btnMeddlingService.addActionListener(new BtnMeddlingServiceListener());
        constraints.gridy = 2;
        mainMenuPanel.add(btnMeddlingService, constraints);

        JButton btnBrainRepository = new JButton("Brain Repository");
        btnBrainRepository.setPreferredSize(buttonSize);
        btnBrainRepository.setVerticalTextPosition(AbstractButton.CENTER);
        btnBrainRepository.setHorizontalTextPosition(AbstractButton.CENTER);
        btnBrainRepository.addActionListener(new MainMVC.BtnBrainRepositoryListener());
        constraints.gridy = 3;
        mainMenuPanel.add(btnBrainRepository, constraints);

        JButton btnLaboriousRepoMethod = new JButton("Laborious Repository Method");
        btnLaboriousRepoMethod.setPreferredSize(buttonSize);
        btnLaboriousRepoMethod.setVerticalTextPosition(AbstractButton.CENTER);
        btnLaboriousRepoMethod.setHorizontalTextPosition(AbstractButton.CENTER);
        btnLaboriousRepoMethod.addActionListener(new BtnLaboriousRepositoryMethodListener());
        constraints.gridy = 4;
        mainMenuPanel.add(btnLaboriousRepoMethod, constraints);

        JButton btnFatRepository = new JButton("Fat Repository");
        btnFatRepository.setPreferredSize(buttonSize);
        btnFatRepository.setVerticalTextPosition(AbstractButton.CENTER);
        btnFatRepository.setHorizontalTextPosition(AbstractButton.CENTER);
        btnFatRepository.addActionListener(new BtnLFatRepositoryListener());
        constraints.gridy = 5;
        mainMenuPanel.add(btnFatRepository, constraints);

        JButton btnAllMVC = new JButton("All MVC");
        btnAllMVC.setPreferredSize(buttonSize);
        btnAllMVC.setVerticalTextPosition(AbstractButton.CENTER);
        btnAllMVC.setHorizontalTextPosition(AbstractButton.CENTER);
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