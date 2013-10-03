
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import javax.imageio.*;
import java.io.*;

public class Board extends JPanel implements ActionListener{
 boolean isStarted = false;
 boolean isPaused = false;
 JLabel statusbar;
 Timer timer;
 Knotted parent;
 String status = "";
 Image backgroundimg = null;
 ArrayList<KnotPiece> knot = new ArrayList<KnotPiece>();
 int numofknotpieces=100;
 boolean mousepressed = false;
 int[] mouseposition = new int[2];
 int grabbed = 25;
 int counterforchecking = 0;
 
 public Board(Knotted parenti){
  setFocusable(true);
  setFocusTraversalKeysEnabled(false);
  timer = new Timer(10, this);
  parent = parenti;
  statusbar = parent.getStatusBar();
  addKeyListener(new KAdapter());
  addMouseListener(new MAdapter());
  addMouseMotionListener(new MAdapter());
  try {
   backgroundimg = ImageIO.read(new File("pictures/background.jpg"));
  } catch (IOException e){
  }
  timer.start();
  //Toolkit tk = getToolkit();
  //Cursor cursor = tk.createCustomCursor(tk.getImage(""), new Point(), "cursor");
  //setCursor(cursor);
  start();
 }

 public void actionPerformed(ActionEvent e){
  counterforchecking++;
  if (parent.connected.getState()){
   int j=grabbed, k=grabbed;
   for (int i=0; i<numofknotpieces; i++){
    j++;
    j=j%numofknotpieces;
     k--;
    k=k%numofknotpieces;
    if (k<0){
     k=k+numofknotpieces;
     }
    if (Math.abs(j-k)<1){
     break;
    }
    knot.get(j).adjust();
    knot.get(k).adjust();
   }
  } else {
   for (int i=1; i<numofknotpieces; i++){
    knot.get(i).adjustNotConnected();
   }
  }

  if (counterforchecking==20){
   checkTangled();
  }
  counterforchecking=counterforchecking%20;

  repaint();
 }

 public void restart(){
  start();
 }

 public void start(){
  numofknotpieces=parent.numofknotpieces;
  knot = new ArrayList<KnotPiece>();
  double x=100, y=100;
  for (int i=0; i<numofknotpieces; i++){
   knot.add(new KnotPiece(this,new int[]{(int)x,(int)y}));
   if (i>0){
    knot.get(i).attachBefore(i-1);
   }
   if (i!=numofknotpieces-1){
    knot.get(i).attachAfter(i+1);
   }
   x=x*1.012;
   y=y*1.008;
  }
  knot.get(0).attachBefore(numofknotpieces-1);
  knot.get(numofknotpieces-1).attachAfter(0);

  isStarted=true;
  if (parent.mode==1){
   parent.connected.setState(true);
   for (int i=0; i<numofknotpieces; i++){
    knot.get(i).position[0]=(int)(Math.random()*200+parent.getSize().getWidth()/3);
    knot.get(i).position[1]=(int)(Math.random()*200+parent.getSize().getHeight()/3);
   }
  }
 }
 
 public void pause(){
  if (!isStarted){
   //return;
  }
  isPaused = !isPaused;
  if (isPaused){
   statusbar.setText("Paused");
   timer.stop();
  } else {
   statusbar.setText(status);
   timer.start();
  }
 }

 public void checkTangled(){
  if (parent.mode==1){
   boolean intersection=false;
   double t = 0;
   double w = 0;
   check:
   for (int i=0; i<numofknotpieces; i++){
    int k = i+1;
    k=k%numofknotpieces;
    int x11 = knot.get(i).position[0];
    int y11 = knot.get(i).position[1];
    int x12 = knot.get(k).position[0];
    int y12 = knot.get(k).position[1];
    for (int j=0; j<numofknotpieces-i-1; j++){
     int b = i+j;
     if (Math.abs(i-b)<=2){
      continue;
     }
     int l = b+1;
     l=l%numofknotpieces;
     int x21 = knot.get(b).position[0];
     int y21 = knot.get(b).position[1];
     int x22 = knot.get(l).position[0];
     int y22 = knot.get(l).position[1];
     if ((x12-x11)*(x22-x21)-(y12-y11)*(y22-y21)==0){
      continue;
     }
     t = ((double)(x21-x11)*(y22-y21)-(y21-y11)*(x22-x21))/((x12-x11)*(y22-y21)-(y12-y11)*(x22-x21));
     w = ((double)(x21-x11)*(y12-y11)-(y21-y11)*(x12-x11))/((x12-x11)*(y22-y21)-(y12-y11)*(x22-x21));
     if (t>-0.01 && t<1.01 && w>-.01 && w<1.01){
      intersection=true;
      break check;
     }
    }
   }
   if (!intersection){
    JOptionPane.showMessageDialog(null, "You untangled the loop!", "Nice!", 1);
    parent.mode=0;
   }
  }
 }

 public void paint(Graphics g){
  super.paint(g);
  int x = 0;
  for (int i=0; i<numofknotpieces-1; i++){
   g.setColor(new Color(x,x,x));
   g.drawLine(knot.get(i).position[0],knot.get(i).position[1],knot.get(i+1).position[0],knot.get(i+1).position[1]);
   x=x+10;
   if (x>250){
    x=0;
   }
  }
  if (parent.connected.getState()){
   g.setColor(new Color(x,x,x));
   g.drawLine(knot.get(numofknotpieces-1).position[0],knot.get(numofknotpieces-1).position[1],knot.get(0).position[0],knot.get(0).position[1]);
  }
 }

 public void grab(int[] target){
  if (parent.connected.getState()){
   knot.get(grabbed).adjust(target);
  } else {
   knot.get(0).adjust(target);
  }
 }

 class KAdapter extends KeyAdapter{
  public void keyPressed(KeyEvent e){
   int keycode = e.getKeyCode();
   int keychar = e.getKeyChar();
   if (keychar==' '){
    pause();
   }
  }
 }

 class MAdapter extends MouseAdapter implements MouseMotionListener{
  public void mousePressed(MouseEvent e) {
   mouseposition = new int[]{e.getX(),e.getY()};
   if (parent.connected.getState()){
    for (int i=0; i<numofknotpieces; i++){
     if (Math.abs(mouseposition[0]-knot.get(i).position[0])<6 && Math.abs(mouseposition[1]-knot.get(i).position[1])<6){
      grabbed=i;
      mousepressed=true;
      break;
     }
    }
   } else {
    mousepressed=true;
   }
   //grab(mouseposition);
  }
  public void mouseMoved(MouseEvent e){
   mouseposition = new int[]{e.getX(),e.getY()};
  }
  public void mouseDragged(MouseEvent e){
   mouseposition = new int[]{e.getX(),e.getY()};
   if (mousepressed){
    grab(mouseposition);
   }
  }
  public void mouseReleased(MouseEvent e){
   mousepressed=false;
   grabbed = -1;
  }
 }

}













