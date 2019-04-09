/*
<applet code = "js" width = 1000 height = 800 >
</applet>
*/
import java.awt.*;    
import java.applet.*;      
public class js extends Applet    
 {    
   Font f1,f2,f3;    
   int x, y;
   
   public void  init()    
   {    
      f1 = new Font("Arial",Font.BOLD,18);    
      f2 = new Font("Forte",Font.PLAIN,24);    
      f3 = new Font("Elephant",Font.ITALIC,28);  
      x = 100; 
      y = 100;
   }    

   public void paint(Graphics g)
   {
      g.setFont(f3);     
      g.drawString("Buffer Cache",300,50);
      g.setFont(f1);
      row = 4;
      for(int i=0;i<4;++i)          // for hashqueues
         for(int j=0;j<5;++j)
         {
            g.drawRect(x+(j*120), y+(i*120), 100, 100);

            if(j==0)
               {
                  g.setColor(Color.blue);
                  g.drawString("Block "+i+"%4", x+(j*120), y+55+(i*120));
               }
            else
               {
                  g.setColor(Color.black);
                  g.drawString("HQ "+i, (x+20)+(j*120), y+55+(i*120));
               }
         }
      // for free list 
      g.drawRect(x, y+500,100, 100);
      g.drawString("FreeList", x, y+550);
   }
 }    
    

