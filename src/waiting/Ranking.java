package waiting;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import login.JDBCModel;

public class Ranking {
    private JFrame frame;
    private DefaultTableModel rankModel;
    private JTable rankTable;
    
    public Ranking(Waiting waiting) {
        frame=new JFrame();
        rankModel = waiting.getModel().ShowRanking();
        rankTable = new JTable(rankModel);
        rankTable.getTableHeader().setReorderingAllowed(false);
        rankTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        rankTable.setRowHeight(20);
        JScrollPane scroll = new JScrollPane(rankTable);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        frame.getContentPane().add(scroll);
        
        frame.setTitle("랭킹");
        frame.setVisible(true);
        frame.setBounds(500,300,250,350);
        frame.setResizable(false);
    }


}