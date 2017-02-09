package com.company;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author keithmartin
 */
public class UserActivityWindow extends javax.swing.JFrame {

    /**
     * Creates new form OptionsWindow
     */

    Table table = new Table();
    ArrayList<String> recommendations = new ArrayList<>();

    String PUBLIC_DNS = "ec2-54-149-52-62.us-west-2.compute.amazonaws.com";
    Connection connection = null;
    public UserActivityWindow() {
        initComponents();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        try {
            connection = DriverManager.
                    getConnection("jdbc:mysql://" + PUBLIC_DNS + ":3306/keithmartinA1P2db", "kmartin5", "Colden21");





            table.fillWatchedAndRatingTable(jTable1, "SELECT * from watched_movie WHERE user_id=" + table.getUserId(connection), connection);
            table.fillMovieTable(jTable2, "SELECT * from movie", connection);

        } catch (SQLException e) {
            System.out.println("Connection Failed!:\n" + e.getMessage());
        }
        //table.fillWatchedAndRatingTable(jTable2, "SELECT * from movie;", connection);
        //table.fillRatingTable(jTable1, "SELECT movie_id from watched_movie;", connection);
    }

    public void run() {
        new UserActivityWindow().setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTable2 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        button1 = new java.awt.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                int row = jTable2.getSelectedRow();
                int column = jTable2.getSelectedColumn();
                try {
                    if (column == 4) {
                        Statement statement = connection.createStatement();
                        if (jTable2.getModel().getValueAt(row, column) != null && ((Integer) jTable2.getModel().getValueAt(row, column) >= 1 && (Integer) jTable2.getModel().getValueAt(row, column) <= 5)) {
                                if (jTable2.getModel().getValueAt(row, column) != null) {
                                    ResultSet result = statement.executeQuery("select star_rating from rating where user_id = " + table.getUserId(connection) + " and movie_id = " + jTable2.getModel().getValueAt(row, 0) + ";");
                                    if (result.next()) {
                                        statement.executeUpdate("UPDATE rating SET star_rating = " + jTable2.getModel().getValueAt(row, column) + " WHERE user_id = " + table.getUserId(connection) + " AND " + " movie_id = " + jTable2.getModel().getValueAt(row, 0));
                                        table.fillMovieTable(jTable2, "SELECT * from movie", connection);
                                        table.fillWatchedAndRatingTable(jTable1, "SELECT * from watched_movie WHERE user_id=" + table.getUserId(connection), connection);
                                    } else {
                                        statement.executeUpdate("INSERT into rating VALUES (" + table.getUserId(connection) + "," + jTable2.getModel().getValueAt(row, column) + "," + jTable2.getModel().getValueAt(row, 0) + ");");
                                        table.fillMovieTable(jTable2, "SELECT * from movie", connection);
                                        table.fillWatchedAndRatingTable(jTable1, "SELECT * from watched_movie WHERE user_id=" + table.getUserId(connection), connection);
                                    }
                                }
                        }
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        };


        KeyListener keyListenerjTable1 = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                int row = jTable1.getSelectedRow();
                int column = jTable1.getSelectedColumn();
                if (column == 3) {
                    if ((Integer) jTable1.getModel().getValueAt(row, column) >= 1 && (Integer) jTable1.getModel().getValueAt(row, column) <= 5) {
                        try {
                            Statement statement = connection.createStatement();
                            statement.executeUpdate("UPDATE rating SET star_rating=" + jTable1.getModel().getValueAt(row, column) + " WHERE movie_id=" + jTable1.getModel().getValueAt(row, 1) + ";");
                            table.fillWatchedAndRatingTable(jTable1, "SELECT * from watched_movie WHERE user_id=" + table.getUserId(connection), connection);
                            table.fillMovieTable(jTable2, "SELECT * from movie", connection);
                        } catch (SQLException e1) {
                            System.out.println(e1.getMessage());
                        }
                    }
                    System.out.println(row + " " + column);
                }
            }
        };

    jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]

    {
        {
            null, null, null, null
        },
        {
            null, null, null, null
        },
        {
            null, null, null, null
        },
        {
            null, null, null, null}
                },
            new String[]{
        "user_id", "movie_id", "position", "rating"
    }

    )

    {
        Class[] types = new Class[]{
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class
        };

    public Class getColumnClass(int columnIndex) {
        return types[columnIndex];
    }
});

        jScrollPane1.setViewportView(jTable1);
        jTable1.addKeyListener(keyListenerjTable1);


        jTable2.setModel(new javax.swing.table.DefaultTableModel(
        new Object[][]{
        {null,null,null,null,null},
        {null,null,null,null,null},
        {null,null,null,null,null},
        {null,null,null,null,null}
        },
                new String[]{
        "movie_id", "category_id","name", "length", "rating"
                }
        ) {
        Class[]types=new Class[]{
                    java.lang.Object.class, java.lang.Object.class,java.lang.Object.class, java.lang.Integer.class,java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex){
        return types[columnIndex];
        }
        });
        jScrollPane2.setViewportView(jTable2);

        jTable2.addKeyListener(keyListener);

        jLabel1.setText("Movies");

        jLabel2.setText("Watched Movies");
        jScrollPane3.setViewportView(jTextArea1);

        jLabel3.setText("Recommendations");


        button1.setLabel("Get recommendations");
        button1.addActionListener(new java.awt.event.ActionListener(){
public void actionPerformed(java.awt.event.ActionEvent evt){
        try{
        button1ActionPerformed(evt);
        }catch(SQLException e){
        e.printStackTrace();
        }
        }
        });

        javax.swing.GroupLayout layout=new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
        .addComponent(jScrollPane1,javax.swing.GroupLayout.PREFERRED_SIZE,387,javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(33,33,33)
        .addComponent(jScrollPane2,javax.swing.GroupLayout.PREFERRED_SIZE,javax.swing.GroupLayout.DEFAULT_SIZE,javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(64,64,64))
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,layout.createSequentialGroup()
        .addGap(161,161,161)
        .addComponent(jLabel2)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,javax.swing.GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
        .addComponent(jLabel1)
        .addGap(283,283,283))
        .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
        .addGap(204,204,204)
        .addComponent(jScrollPane3,javax.swing.GroupLayout.PREFERRED_SIZE,426,javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGroup(layout.createSequentialGroup()
        .addGap(357,357,357)
        .addComponent(jLabel3))
        .addGroup(layout.createSequentialGroup()
        .addGap(343,343,343)
        .addComponent(button1,javax.swing.GroupLayout.PREFERRED_SIZE,javax.swing.GroupLayout.DEFAULT_SIZE,javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
        .addGap(20,20,20)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
        .addComponent(jLabel1)
        .addComponent(jLabel2))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,false)
        .addComponent(jScrollPane2,javax.swing.GroupLayout.DEFAULT_SIZE,181,Short.MAX_VALUE)
        .addComponent(jScrollPane1,javax.swing.GroupLayout.PREFERRED_SIZE,0,Short.MAX_VALUE))
        .addGap(45,45,45)
        .addComponent(jLabel3)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(158, Short.MAX_VALUE))
        );


        pack();
    }// </editor-fold>


    private void button1ActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {
        // TODO add your handling code here:
        recommendations = table.queryForRecommendations("select m.name, m3.name\n" +
                "from user u\n" +
                "inner join preference p\n" +
                "on u.user_id = p.user_id\n" +
                "and u.user_id = " + table.getUserId(connection) + "\n" +
                "inner join movie m\n" +
                "on p.category_id = m.category_id\n" +
                "inner join watched_movie wm3\n" +
                "on wm3.user_id = u.user_id\n" +
                "inner join movie m3\n" +
                "on wm3.movie_id = m3.movie_id\n" +
                "where m.movie_id not in (select wm2.movie_id\n" +
                "                                     from watched_movie wm2\n" +
                "                                     where wm2.user_id = u.user_id)\n" +
                "and wm3.movie_id in (select wm2.movie_id\n" +
                "                                     from watched_movie wm2 \n" +
                "                                     where wm2.user_id = u.user_id) \n" +
                "and m3.category_id = p.category_id\n" +
                "and p.category_id in \n" +
                "     (select m2.category_id\n" +
                "      from movie m2\n" +
                "      inner join watched_movie wm\n" +
                "      on wm.movie_id = m2.movie_id\n" +
                "      inner join rating r\n" +
                "      on wm.movie_id = r.movie_id\n" +
                "      where wm.position = 'Full'\n" +
                "      and wm.user_id = u.user_id\n" +
                "      and r.user_id = u.user_id\n" +
                "      and r.star_rating in (5));", connection);

        jTextArea1.setText("");
        for (String recommend : recommendations) {
            jTextArea1.append(recommend +"\n");
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserActivityWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserActivityWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserActivityWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserActivityWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserActivityWindow().setVisible(true);
            }
        });

    }



    // Variables declaration - do not modify
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea1;
    private java.awt.Button button1;
    // End of variables declaration
}

