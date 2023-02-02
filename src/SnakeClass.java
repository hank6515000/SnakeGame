import java.awt.*;
import java.util.ArrayList;

public class SnakeClass {
    private ArrayList<Node>SnakeBady;

    public SnakeClass(){
        SnakeBady = new ArrayList<>();
        SnakeBady.add(new Node(80,0));
        SnakeBady.add(new Node(60,0));
        SnakeBady.add(new Node(40,0));
        SnakeBady.add(new Node(20,0));
    }

    public ArrayList<Node> getSnakeBady() {
        return SnakeBady;
    }

    public void drawSnake(Graphics g){
        for (int i = 0;i<SnakeBady.size();i++){
            //蛇頭為綠
            if (i ==0){
                g.setColor(Color.GREEN);

            }//設定蛇身為黃
            else {
                g.setColor(Color.PINK);
            }

            Node node = SnakeBady.get(i);
            //設定如果超出視窗後 出現在另一邊
            //超出視窗右邊
            if (node.x>=main.width){
                node.x = 0;
            }//超出視窗左邊
            if (node.x<0){
                node.x = main.width -main.CELL_SIZE;
            } //超出視窗下面
            if (node.y>=main.height){
                node.y = 0;
            }//超出視窗上面
            if (node.y<0){
                node.y = main.height -main.CELL_SIZE;
            }

            g.fillOval(node.x,node.y,main.CELL_SIZE,main.CELL_SIZE);
        }
    }
}
