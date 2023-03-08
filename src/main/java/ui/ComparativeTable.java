package ui;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;


public class ComparativeTable{
    TableModel tableModel;

    public  ComparativeTable(TableModel tableModel){
        this.tableModel = tableModel;
    }

    public void drawTable() {
        // Create an image icon from a file
        ImageIcon icon = new ImageIcon("/Users/williamcasasola/Downloads/up-arrow.png");

        // Create a label with the text "Hello" and the image icon
        JLabel label = new JLabel("Hello", icon, SwingConstants.LEFT);
        Object[][] data = {
            {icon, "Analyzer", "Test1", 20, 120, 5, new Date()},
            {"","PMD", "Test2", 15, 100, 6, new Date()},
            {"", "PMD", "Test3", 12, 90 , 10, new Date()},
        };

        JTable table = new JTable(tableModel);
       // javax.swing.JLabel[,0,0,0x0,invalid,alignmentX=0.0,alignmentY=0.0,border=,flags=8388608,maximumSize=,minimumSize=,preferredSize=,defaultIcon=up-arrow.png,disabledIcon=,horizontalAlignment=LEFT,horizontalTextPosition=TRAILING,iconTextGap=4,labelFor=,text=Hello,verticalAlignment=CENTER,verticalTextPosition=CENTER]
        // Add the label to the first cell of the table
        //table.setValueAt(label, 0, 0);

        //Allow to sort by columns
        table.setAutoCreateRowSorter(true);
        // Enable cell selection
        table.setCellSelectionEnabled(true);
        table.setColumnSelectionAllowed(true);
        table.setRowSelectionAllowed(true);
        // Set the TransferHandler for the table
        // Create a JPopupMenu
        JPopupMenu popupMenu = new JPopupMenu();

        // Create a JMenuItem for copying
        JMenuItem copyMenuItem = new JMenuItem("Copy");

        // Add an ActionListener to the copy menu item
        copyMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected cell(s)
                int[] rows = table.getSelectedRows();
                int[] cols = table.getSelectedColumns();

                // Copy the content of the selected cell(s) to the clipboard
                StringBuilder sb = new StringBuilder();
                for (int row : rows) {
                    for (int col : cols) {
                        sb.append(table.getValueAt(row, col)).append("\t");
                    }
                    sb.append("\n");
                }
                StringSelection selection = new StringSelection(sb.toString());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            }
        });

// Add the copy menu item to the popup menu
        popupMenu.add(copyMenuItem);

// Set the popup menu for the table
        table.setComponentPopupMenu(popupMenu);

        // Add the table to a scroll pane, so that it can be scrolled if necessary
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the scroll pane to a frame and display the frame
        JFrame frame = new JFrame();
        frame.add(scrollPane);
        frame.setSize(800, 200);
        frame.setVisible(true);
    }
}
