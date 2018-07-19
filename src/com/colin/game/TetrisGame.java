package com.colin.game;

import com.colin.game.core.FFEvent;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Random;
import javax.swing.JOptionPane;
public class TetrisGame extends Canvas implements FFEvent
{

	protected enum GameState
	{
		GAME_MENU, GAME_START, GAME_CONTINUE, GAME_HELP, GAME_SET, GAME_EXIT, GAME_PAUSE
	}
	private Timeline timeline;// 定时处理
	private KeyFrame keyFrame;
	private int duration = 500;// 定时时间间隔
	protected GameState mGameState = GameState.GAME_MENU;

	int[][] fallblock = new int[20][10];
	int[][] solidblock = new int[20][10];
	boolean flag = false;
	int df = 0;
	int m = 0;
	int n = 0;
	int geshu = 0;
	boolean is = true;
	public TetrisGame(double width, double height)
	{
		super(width, height);
		initTimeLine();
		//String url="file:"+System.getProperty("user.dir")+"\\snake\\snak02.jpg";
		fallblock[1][3] = 1;
		fallblock[1][4] = 1;
		fallblock[1][5] = 1;
		fallblock[0][5] = 1;
		geshu = 3;
	}
	public void initEvents()
	{

		getParent().getScene().setOnKeyPressed(event -> {
			onKeyPressed(event);
		});

		getParent().getScene().setOnKeyReleased(event -> {
			onKeyReleased(event);
		});

		getParent().getScene().setOnMouseMoved(event -> {
			onMouseMoved(event);
		});
	}

	/**
	 * 键盘按下
	 */
	@Override
	public void onKeyPressed(KeyEvent event)
	{
		// TODO 自动生成的方法存根
		FFEvent.super.onKeyPressed(event);
		switch (event.getCode()) {
			case UP:
				change();
				break;
			case LEFT:
				leftmove();
				break;
			case RIGHT:
				rightmove();
				break;
			case DOWN:
				downmove();
				break;
			default:
				break;
		}
	}
	/**
	 * 键盘释放
	 */
	@Override
	public void onKeyReleased(KeyEvent event)
	{
		// TODO 自动生成的方法存根
		FFEvent.super.onKeyReleased(event);
	}
	/**
	 * 鼠标处理
	 */
	@Override
	public void onMouseMoved(MouseEvent event)
	{
		// TODO 自动生成的方法存根
		FFEvent.super.onMouseMoved(event);
	}
	/**
	 * init the timeline
	 */
	private void initTimeLine()
	{
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		keyFrame = new KeyFrame(Duration.millis(duration), new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				timeRun();
			}
		});
		timeline.getKeyFrames().add(keyFrame);
	}
	/**
	 * 定时运行
	 */
	public void timeRun()
	{
		if(!touchwalldown()&&!touchsolidblockdown()){
			downmove();
		}else {
			score(removesoildblock());
			if(	gameover()){
				score(removesoildblock());
				JOptionPane.showMessageDialog(null, "您的方块已经到顶了，没法玩了","游戏结束",JOptionPane.INFORMATION_MESSAGE);
				System.exit(1);
			}else {
				if (flag) {
					initfallBlock();
					createfallBlock();
					flag = false;
				}
			}
		}
		draw(getGraphicsContext2D());
	}
	/**
	 * draw the objects
	 * 
	 * @param gc
	 *            绘图处理
	 */
	public void draw(GraphicsContext gc) {
		gc.setEffect(null);
		gc.clearRect(0, 0, getWidth(), getHeight());
		Background(gc);
		fallblock(gc);
		solidblock(gc);
		gc.fillText("分数："+df,400,500);
	}
	//画背景
	public void Background(GraphicsContext gc) {
		gc.drawImage(new Image("file:tetris/俄罗斯方块.bmp"),0,0);
	}
	//画落块
	public void fallblock(GraphicsContext gc) {
		Image fallblockImage = new Image("file:tetris/红块.bmp");
		for(int h=0;h<fallblock.length;h++) {
			for(int l=0;l<fallblock[h].length;l++) {
				if(fallblock[h][l] == 1) {
					gc.drawImage(fallblockImage,l*30+30,h*30+30);
				}
			}
		}
	}
	//画实块
	public void solidblock(GraphicsContext gc) {
		Image solidblockImage = new Image("file:tetris/蓝块.bmp");
		for(int h=0;h<solidblock.length;h++) {
			for(int l=0;l<solidblock[h].length;l++) {
				if(solidblock[h][l] == 1) {
					gc.drawImage(solidblockImage,l*30+30,h*30+30);
				}
			}
		}
	}
	//创建落块
	public void createfallBlock() {
		Random rand = new Random();
		//落块的列坐标
		int a1 = rand.nextInt(3) + 3;//3 4 5
		//落块第二排的个数
		int number = rand.nextInt(5);//01234
		is = true;
		if (number == 0) {
			fallblock[1][4] = 1;
		} else if (number == 1) {
			fallblock[0][3] = 1;
			fallblock[0][4] = 1;
			fallblock[1][3] = 1;
			fallblock[1][4] = 1;
			geshu = 2;
		} else if (number == 2) {
			fallblock[1][3] = 1;
			fallblock[1][4] = 1;
			fallblock[1][5] = 1;
			fallblock[0][a1] = 1;
			geshu = 3;
		} else if(number == 3){
			fallblock[1][3] = 1;
			fallblock[1][4] = 1;
			fallblock[1][5] = 1;
			fallblock[1][6] = 1;
			geshu = 4;
		}else{
			fallblock[0][3] = 1;
			fallblock[0][4] = 1;
			fallblock[1][4] = 1;
			fallblock[1][5] = 1;
			geshu = 3;
		}
	}
	//碰墙左
	public boolean touchwallleft() {
		for(int h=0;h<20;h++) {
			if(fallblock[h][0] == 1)
				return true;
		}
		return false;
	}
	//碰实块左
	public boolean touchsolidblockleft() {
		for(int l=1;l<10;l++) {
			for(int h=0;h<20;h++) {
				if(fallblock[h][l]==1&&fallblock[h][l] == solidblock[h][l-1])
					return true;
			}
		}
		return false;
	}
	//左移
	public void leftmove() {
		is = true;
		if(!touchsolidblockleft()&&!touchwallleft()) {
			for(int l=1;l<10;l++) {
				for(int h=0;h<20;h++) {
					fallblock[h][l-1] = fallblock[h][l];
					fallblock[h][l] = 0;
				}
			}
		}
	}
	//碰墙右
	public boolean touchwallright() {
		for(int h=0;h<20;h++) {
			if(fallblock[h][9] == 1)
				return true;
		}
		return false;
	}
	//碰实块右
	public boolean touchsolidblockright() {
		for(int l=0;l<9;l++) {
			for(int h=0;h<20;h++) {
				if(fallblock[h][l]==1&&fallblock[h][l] == solidblock[h][l+1])
					return true;
			}
		}
		return false;
	}
	//右移
	public void rightmove() {
		is = true;
		//!touchwallright()&&!touchsolidblockright()
		if(!touchwallright()&&!touchsolidblockright()) {
			for(int l=9;l>0;l--) {
				for(int h=0;h<20;h++) {
					fallblock[h][l] = fallblock[h][l-1];
					fallblock[h][l-1] = 0;
				}
			}
		}
	}
	//碰墙下
	public boolean touchwalldown() {
		for(int l=0;l<10;l++) {
			if(fallblock[19][l] == 1){
				fallchangesoild();
				return true;
			}
			else
				flag = false;
		}
		return false;
	}
	//碰实块下
	public boolean touchsolidblockdown() {
		for(int l=0;l<10;l++) {
			for(int h=0;h<19;h++) {
				if(fallblock[h][l]==1&&fallblock[h][l] == solidblock[h+1][l]) {
					fallchangesoild();
					return true;
				}else
					flag = false;
			}
		}
		return false;
	}
	//下移
	public void downmove() {
		is = true;
		if(!touchwalldown()&&!touchsolidblockdown()) {
			for(int l=0;l<10;l++) {
				for(int h=19;h>0;h--) {
					fallblock[h][l] = fallblock[h-1][l];
					fallblock[h-1][l] = 0;
				}
			}
		}
	}
	//落块变实块
	public void fallchangesoild(){
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 20; j++) {
				if (solidblock[j][i] == 0) {
					solidblock[j][i] = fallblock[j][i];
				}
			}
		}
		flag = true;
	}
	//初始化落块为0
	public void initfallBlock() {
		for (int h = 0; h < 20; h++) {
			for (int l = 0; l < 10; l++) {
				fallblock[h][l] = 0;
			}
		}
	}
	//消实块
	public int removesoildblock() {
		int h, l;
		int counth = 0;
		for (h = 19; h >= 0; h--) {
			for (l = 0; l < 10; l++) {
				if (solidblock[h][l] == 0) {
					break;
				}
			}
			//l==10 一行堆满实块
			if (l == 10) {
				counth ++ ;
				for (int i = h; i > 0; i--) {
					for (int j = 0; j < 10; j++) {
						solidblock[i][j] = solidblock[i - 1][j];
					}
				}
				h++;
			}
		}
		return counth;
	}
	//方块翻转
	public void change(){
		if(is) {
			boolean changeflag = true;
			for (int h = 0; h < 20 && changeflag; h++) {
				for (int l = 0; l < 10; l++) {
					if (fallblock[h][l] == 1) {
						m = h;
						n = l;
						changeflag = false;
						break;
					}
				}
			}
			for (int h = 0; h < 20; h++) {
				for (int l = 0; l < 10; l++) {
					if (fallblock[h][l] == 1) {
						if (l <= n) {
							n = l;
						}
					}
				}
			}
			is = false;
		}
		int[][] a2=new int[geshu][geshu];
		for(int i=0;i<geshu;i++)
		{
			int k=0;
			for(int j=geshu-1;j>=0;j--)//4-1
			{   //进行转置，将a1每一行的值对应赋值给a2的每一列
				a2[j][i]=fallblock[m+i][k+n];
				k++;
			}
		}
		for(int h=m;h<m+geshu;h++){
			for(int l=n;l<n+geshu;l++){
				fallblock[h][l] = a2[h-m][l-n];
			}
		}
	}
	//得分
	public int score(int h){
		df = df + 50*h;
		return df;
	}
	//撞上墙游戏结束
	public boolean gameover(){
		for(int l=0;l<10;l++) {
			if (solidblock[0][l] == 1){
				return true;
			}
		}
		return false;
	}
	/**
	 * start the update timeline
	 */
	public void start() {
		timeline.play();
	}
	/**
	 * pause the update timeline
	 */
	public void pause() {
		timeline.pause();
	}
	/**
	 * stop the update timeline
	 */
	public void stop() {
		timeline.stop();
	}
}
