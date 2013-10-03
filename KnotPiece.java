

public class KnotPiece{
 Board parent;
 int[] position = new int[2];
 int piecebefore=0, pieceafter=0;

 public KnotPiece(Board parenti, int[] positioni){
  parent = parenti;
  position[0]=positioni[0];
  position[1]=positioni[1];
 }

 public void attachBefore(int piecebeforei){
  piecebefore = piecebeforei;
 }

 public void attachAfter(int pieceafteri){
  pieceafter = pieceafteri;
 }

 public void adjust(int[] target){
  int xdistance, ydistance;
  xdistance = target[0]-position[0];
  ydistance = target[1]-position[1];
  if (Math.sqrt(xdistance*xdistance+ydistance*ydistance)>1){
   position[0]=position[0]+(int)(xdistance/5);
   position[1]=position[1]+(int)(ydistance/5); 
  } 
 }

 public void adjust(){
  int xdistance2, ydistance2;
  xdistance2 = parent.knot.get(pieceafter).position[0]-position[0];
  ydistance2 = parent.knot.get(pieceafter).position[1]-position[1];
  int xdistance1, ydistance1;
  xdistance1 = parent.knot.get(piecebefore).position[0]-position[0];
  ydistance1 = parent.knot.get(piecebefore).position[1]-position[1];
  int xdistance = (int)((xdistance1+xdistance2)/1.5);
  int ydistance = (int)((ydistance1+ydistance2)/1.5);
  if (Math.sqrt(xdistance*xdistance+ydistance*ydistance)>2){
   position[0]=position[0]+(int)(xdistance/2);
   position[1]=position[1]+(int)(ydistance/2);
  }
 }

 public void adjustNotConnected(){
  int xdistance1, ydistance1;
  xdistance1 = parent.knot.get(piecebefore).position[0]-position[0];
  ydistance1 = parent.knot.get(piecebefore).position[1]-position[1];
  if (Math.sqrt(xdistance1*xdistance1+ydistance1*ydistance1)>7){
   position[0]=position[0]+(int)(xdistance1/2.5);
   position[1]=position[1]+(int)(ydistance1/2.5);
  }
 }

}