package ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;


public class ComparativeTable{
    TableModel tableModel;
    String title;

    public  ComparativeTable(TableModel tableModel, String title){
        this.tableModel = tableModel;
        this.title = title;
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

        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), title, TitledBorder.LEFT,
                TitledBorder.TOP));


        JLabel rowCountLabel = new JLabel("Total Rows: " + tableModel.getRowCount());
        scrollPane.add(rowCountLabel);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(rowCountLabel, BorderLayout.SOUTH);

        // Add the scroll pane to a frame and display the frame
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setSize(800, 200);
        frame.setVisible(true);

        this.title = "Testing";

        String updateQuery = "UPDATE DATACLASS SET projectname = SUBSTR(x, 1, INSTR(x, '/')-1) " +
                "FROM (SELECT SUBSTR(path, INSTR(path, '/Systems/') + 9) x, projectname, id FROM DATACLASS WHERE analyzerid IN (1, 2)) AS paths " +
                "WHERE DATACLASS.id = paths.id";

        String comparativeQuery = "WITH GROUPS AS (\n" +
                "\tSELECT DISTINCT\n" +
                "\t\tdc1.projectname,\n" +
                "\t\tCASE WHEN dc1.analyzerid > dc2.analyzerid THEN\n" +
                "\t\t\tdc2.analyzerid\n" +
                "\t\tELSE\n" +
                "\t\t\tdc1.analyzerid\n" +
                "\t\tEND id1,\n" +
                "\t\tCASE WHEN dc1.analyzerid > dc2.analyzerid THEN\n" +
                "\t\t\tdc1.analyzerid\n" +
                "\t\tELSE\n" +
                "\t\t\tdc2.analyzerid\n" +
                "\t\tEND id2,\n" +
                "\t\tCOUNT(1) same\n" +
                "\tFROM\n" +
                "\t\tDATACLASS dc1\n" +
                "\t\tINNER JOIN DATACLASS dc2 ON (dc1.projectname = dc2.projectname\n" +
                "\t\t\t\tOR dc1.projectname = SUBSTR(dc2.projectname,\n" +
                "\t\t\t\t\tINSTR(dc2.projectname,\n" +
                "\t\t\t\t\t\tdc1.projectname),\n" +
                "\t\t\t\t\tlength(dc1.projectname) + 1))\n" +
                "\t\t\tAND dc1.language = dc2.language\n" +
                "\t\t\tAND dc1.analyzerid <> dc2.analyzerid\n" +
                "\t\t\tAND(dc1.classname = dc2.classname\n" +
                "\t\t\t\tOR dc1.classname = dc2.mainclassname\n" +
                "\t\t\t\tOR dc1.mainclassname = dc2.classname\n" +
                "\t\t\t\tOR dc1.mainclassname = dc2.mainclassname)\n" +
                "\t\tGROUP BY\n" +
                "\t\t\tdc1.analyzerid,\n" +
                "\t\t\tdc2.analyzerid,\n" +
                "\t\t\tdc1.projectname\n" +
                ")\n" +
                "SELECT\n" +
                "\tprojectname,\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\tname\n" +
                "\t\tFROM\n" +
                "\t\t\tANALYZER a\n" +
                "\tWHERE\n" +
                "\t\ta.id = g.id1) AnalyzerName1, (\n" +
                "\t\tSELECT\n" +
                "\t\t\tname\n" +
                "\t\tFROM\n" +
                "\t\t\tANALYZER a\n" +
                "\t\tWHERE\n" +
                "\t\t\ta.id = g.id2) AnalyzerName2, same\n" +
                "FROM\n" +
                "\tGROUPS g\n" +
                "ORDER BY\n" +
                "\tsame DESC";


        String version2 = "WITH GROUPS AS (\n" +
                "\tSELECT DISTINCT\n" +
                "\t\tdc1.projectname,\n" +
                "\t\tCASE WHEN dc1.analyzerid > dc2.analyzerid THEN\n" +
                "\t\t\tdc2.analyzerid\n" +
                "\t\tELSE\n" +
                "\t\t\tdc1.analyzerid\n" +
                "\t\tEND id1,\n" +
                "\t\tCASE WHEN dc1.analyzerid > dc2.analyzerid THEN\n" +
                "\t\t\tdc1.analyzerid\n" +
                "\t\tELSE\n" +
                "\t\t\tdc2.analyzerid\n" +
                "\t\tEND id2,\n" +
                "\t\tdc1.classname\n" +
                "\tFROM\n" +
                "\t\tGODCLASS dc1\n" +
                "\t\tINNER JOIN GODCLASS dc2 ON (dc1.projectname = dc2.projectname\n" +
                "\t\t\t\tOR dc1.projectname = SUBSTR(dc2.projectname,\n" +
                "\t\t\t\t\tINSTR(dc2.projectname,\n" +
                "\t\t\t\t\t\tdc1.projectname),\n" +
                "\t\t\t\t\tlength(dc1.projectname) + 1))\n" +
                "\t\t\tAND dc1.language = dc2.language\n" +
                "\t\t\tAND dc1.analyzerid <> dc2.analyzerid\n" +
                "\t\t\tAND(dc1.classname = dc2.classname\n" +
                "\t\t\t\tOR dc1.classname = dc2.mainclassname\n" +
                "\t\t\t\tOR dc1.mainclassname = dc2.classname\n" +
                "\t\t\t\tOR dc1.mainclassname = dc2.mainclassname)\n" +
                "\t\t\tAND dc1.mainclassname NOT LIKE '%test%' AND dc2.mainclassname NOT LIKE '%test%'\n" +
                ")\n" +
                "SELECT\n" +
                "\tprojectname,\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\tname\n" +
                "\t\tFROM\n" +
                "\t\t\tANALYZER a\n" +
                "\tWHERE\n" +
                "\t\ta.id = g.id1) AnalyzerName1, (\n" +
                "\t\tSELECT\n" +
                "\t\t\tname\n" +
                "\t\tFROM\n" +
                "\t\t\tANALYZER a\n" +
                "\t\tWHERE\n" +
                "\t\t\ta.id = g.id2) AnalyzerName2, COUNT(1) same\n" +
                "FROM\n" +
                "\tGROUPS g\n" +
                "GROUP BY\n" +
                "\tg.id1,\n" +
                "\tg.id2,\n" +
                "\tg.projectname\n" +
                "ORDER BY\n" +
                "\tsame DESC";
    }
}
