import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class main extends JPanel implements KeyListener {
    public static final int CELL_SIZE = 20;//設定每個Node的大小
    public static int width =400;
    public static int height = 400;
    public static int row = height/CELL_SIZE;
    public static int col = width/CELL_SIZE;
    private SnakeClass Snake;
    private Fruit fruit;
    private Timer t;
    private Integer speed = 100;
    private static String direction ;
    //避免BUG 設定如果按了keyPressed的時候,到下一次遊戲視窗被重畫之前都不能運作keyPressed
    private boolean allowKeyPress;
    private int sorce;
    private int HightSorce;
    String desktop = System.getProperty("user.home") +"/Desktop/";
    String myFile = desktop +"filename.txt";

    public main(){
        readHightSorce();
        reset();
        addKeyListener(this);
    }

    private void setTimer() {
        t = new Timer();
        //scheduleAtFixedRate:讓物件在某個固定時間執行動作 讓Timer去執行
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, speed);
    }
    private void reset(){
        sorce =  0;
        if (Snake!= null) {
            Snake.getSnakeBady().clear();
        }
        Snake = new SnakeClass();
        fruit = new Fruit();
        allowKeyPress = true;
        direction = "right";
        setTimer();
    }

    @Override
    //加入背景
    public void paintComponent(Graphics g){
        //確認蛇有沒有咬到自己
        ArrayList <Node> Snake_Body = Snake.getSnakeBady();
        Node head = Snake_Body.get(0);
        for(int i = 1 ; i <Snake_Body.size(); i++) {
            //咬到自己
            if ((Snake_Body.get(i).x == head.x) && (Snake_Body.get(i).y == head.y)) {
                allowKeyPress = false;
                t.cancel();
                t.purge();
                //Timer停止
                int response = JOptionPane.showOptionDialog
                        (this, "最高得分為:"+HightSorce+"\n你的得分為:"+sorce+"\n是否要再玩一局"
                                , "遊戲結束",JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE
                                , null, null, JOptionPane.YES_OPTION);
                WriteFile(sorce);
                switch (response) {
                    case JOptionPane.CLOSED_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.NO_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.YES_OPTION:
                        reset();
                        return;
                    //return之後下面代碼都不會執行
                }
            }
        }
        //背景黑色
        g.fillRect(0,0,width,height);
        //加入物件至背景中
        fruit.drawFruit(g);
        Snake.drawSnake(g);
        //把尾巴切下放到頭的位置
        int SnakeX =Snake.getSnakeBady().get(0).x;
        int SnakeY = Snake.getSnakeBady().get(0).y;
        //設定Snake的移動方式
        // left , x-=CELL+SIZE;
        if (direction.equals("left")){
            SnakeX-=CELL_SIZE;
        }  // top , y+=CELL+SIZE;
        else if (direction.equals("top")){
            SnakeY-=CELL_SIZE;
        } // right , x+=CELL+SIZE;
        else if (direction.equals("right")){
            SnakeX+=CELL_SIZE;
        }// down , y-=CELL+SIZE;
        else if (direction.equals("down")){
            SnakeY+=CELL_SIZE;
        }
        Node NewHead = new Node(SnakeX,SnakeY);
        //確定吃到水果的方法
        if (Snake.getSnakeBady().get(0).x==fruit.getX()
                && Snake.getSnakeBady().get(0).y == fruit.getY()){
            //1.設定水果放置在視窗的新位置
            fruit.setNewLoocation(Snake);
            //2.設定增加身長
            fruit.drawFruit(g);
            //3.增加成績
            sorce++;
        }else {
            //沒有吃到水果的話就把尾巴切下來
            Snake.getSnakeBady().remove(Snake.getSnakeBady().size()-1);
        }
        //然後移到蛇頭
        Snake.getSnakeBady().add(0,NewHead);
       //設定在下一次畫完之前都是false 畫完之後即可執行
        allowKeyPress = true;
        requestFocusInWindow();

    }
    //重寫方法設定視窗大小
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width,height);//
    }

    public static void main(String[] args) {
        JFrame mainJFeame = new JFrame("SnakeGame");
        mainJFeame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainJFeame.setContentPane(new main());
        mainJFeame.pack();
        mainJFeame.setLocationRelativeTo(null);
        mainJFeame.setVisible(true);
        mainJFeame.setResizable(false);//使用者無法調整大小

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //設定移動方向
        //判斷是否為KeyPress被按下
        if (allowKeyPress) {
            //left = 37 ,方向不是往右
            if (e.getKeyCode() == 37 && !(direction.equals("right"))) {
                direction = "left";
            }   //down = 38 ,方向不是往上
            else if (e.getKeyCode() == 38 && !(direction.equals("down"))) {
                direction = "top";
            }//down = 39 ,方向不是往左
            else if (e.getKeyCode() == 39 && !(direction.equals("left"))) {
                direction = "right";
            }//down = 40 ,方向不是往下
            else if (e.getKeyCode() == 40 && !(direction.equals("top"))) {
                direction = "down";
            }
            //在按下後改變成false
            allowKeyPress = false;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * 讀取最高分數
     */
    public void readHightSorce(){
        try {
            File Obj = new File(myFile);
            Scanner scanner = new Scanner(Obj);
            scanner.close();

        }catch (FileNotFoundException e){
            HightSorce = 0;
            try {
                File myObj = new File(myFile);
                if (myObj.createNewFile()){
                    System.out.println("file created:"+myObj.getName());
                }
                FileWriter fileWriter = new FileWriter(myObj.getName());
                fileWriter.write(""+0);
            }catch (IOException ex){
                System.out.println("Error occurred");
                ex.printStackTrace();
            }
        }
    }

    /**
     * 寫出最高分數
     * @param sorce
     */
    public void WriteFile(int sorce){
        try {
            FileWriter fileWriter = new FileWriter("filename.txt");
            if (sorce>HightSorce){
                System.out.println("rewriting score...");
                fileWriter.write(""+sorce);
                HightSorce = sorce;

            }else {
                fileWriter.write(""+HightSorce);
            }
            fileWriter.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}



