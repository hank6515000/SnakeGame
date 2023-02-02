import java.awt.*;
import java.util.ArrayList;

public class Fruit {
    private int x;
    private int y;
    public Fruit(){
        //x,y座標設定為隨機出現
        this.x=(int) (Math.floor(Math.random()*main.col)*main.CELL_SIZE);
        this.y=(int) (Math.floor(Math.random()*main.row)*main.CELL_SIZE);
    }

    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    //設定水果的背景及位置
    public void drawFruit(Graphics g){
        g.setColor(Color.green);
        g.fillOval(this.x,this.y,main.CELL_SIZE,main.CELL_SIZE);
    }
    public void setNewLoocation(SnakeClass snake){
        //避開蛇所在的位置放置水果
        int NewX;
        int NewY;
        boolean overLapping;
        //使用dowhile迴圈持續尋找新位置直到沒重疊在蛇身上
        do{
            //找新的X,Y座標隨機放置水果
            NewX = (int) (Math.floor(Math.random()*main.col)*main.CELL_SIZE);
            NewY = (int) (Math.floor(Math.random()*main.row)*main.CELL_SIZE);
            overLapping = checkOverLap(NewX,NewY,snake);
        }while (overLapping);
         this.x = NewX;
         this.y = NewY;
    }


         private boolean checkOverLap(int x, int y,SnakeClass snake){
             ArrayList<Node> SnakeBady = snake.getSnakeBady();
        for (int j = 0;j<snake.getSnakeBady().size();j++){
            if (x == SnakeBady.get(j).x && y ==  SnakeBady.get(j).y){
                return true;
            }
        }
        return false;
         }
}
