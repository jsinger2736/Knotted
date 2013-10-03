
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Knotted extends JFrame{
 JMenuBar menubar;
 JMenu file, options;
 JMenuItem newgame, exit, length;
 JCheckBoxMenuItem connected;
 JLabel statusbar;
 Board board;
 MenuHandler menuhandler;
 int numofknotpieces=100;
 int mode=1; //0 play. 1 untangle.
 
 public Knotted(){
  menubar = new JMenuBar();
  menuhandler = new MenuHandler();
  file = new JMenu("File");
  newgame = new JMenuItem("New Game");
  newgame.addActionListener(menuhandler);
  exit = new JMenuItem("Exit");
  exit.addActionListener(menuhandler);
  file.add(newgame);
  file.add(exit);
  menubar.add(file);
  options = new JMenu("Options");
  length = new JMenuItem("Length");
  length.addActionListener(menuhandler);
  connected = new JCheckBoxMenuItem("Loop",false);
  connected.addActionListener(menuhandler);
  options.add(length);
  options.add(connected);
  menubar.add(options);
  statusbar = new JLabel("statusbar", SwingConstants.CENTER);
  add(statusbar, BorderLayout.SOUTH);
  board = new Board(this);
  add(board);
  setJMenuBar(menubar);
  setSize(400,600);
  //setExtendedState(JFrame.MAXIMIZED_BOTH);
  setTitle("Knotted");
  setDefaultCloseOperation(EXIT_ON_CLOSE);
 }

 public JLabel getStatusBar(){
  return statusbar;
 }

 public static void main(String[] args){
  Knotted game = new Knotted();
  game.setLocationRelativeTo(null);
  game.setVisible(true);
  //game.board.start();
 }

 public class MenuHandler implements ActionListener{
  public void actionPerformed(ActionEvent e){
   Object source = e.getSource();
   Object[] list;
   if (source==newgame){
    list = new Object[2];
    list[0]="Freeplay";
    list[1]="Unknotting";
    try{
     String blah = String.valueOf(JOptionPane.showInputDialog(null,"Select a mode:","Mode",JOptionPane.PLAIN_MESSAGE,null,list,list[0]));
     if (blah.equals("Freeplay")){
      mode=0;
     } else if (blah.equals("Unknotting")){
      mode=1;
     }
     board.start();
    } catch (Exception exception){
    }
   }
   if (source==exit){
    System.exit(0);
   }
   if (source==length){
    list = new Object[25];
    for (int i=0; i<20; i++){
     list[i]=(i+1)*5;
    }
    list[20]=125;
    list[21]=150;
    list[22]=175;
    list[23]=200;
    list[24]=250;
    try{
     numofknotpieces = (int)JOptionPane.showInputDialog(null,"How long should the string be","String length",JOptionPane.PLAIN_MESSAGE,null,list,list[numofknotpieces/5-1]);
     board.start();
    } catch (Exception exception){
     try{
      numofknotpieces = (int)JOptionPane.showInputDialog(null,"How long should the string be","String length",JOptionPane.PLAIN_MESSAGE,null,list,list[0]);
      board.start();
     } catch (Exception exception2){
      System.out.println(exception);
     }
    }
   }
   if (source==connected){
    mode=0;
   }
  }
 }
}


















