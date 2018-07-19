package com.colin.game;
import com.colin.game.core.FFApplication;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
public class MainGame extends FFApplication {
	@Override
	protected void before() {
		setWindowSize(700, 700);
	}

	@Override
	protected void after() {
		TetrisGame gameScreen = new TetrisGame(700, 700);
		getRoot().getChildren().add(gameScreen);
		gameScreen.start();
		gameScreen.initEvents();
		getScene().setFill(Color.ALICEBLUE);
	}
	@Override
	protected void showStage(Stage stage) {
		super.showStage(stage);
		stage.setTitle("JavaFX游戏开发 俄罗斯方块");
	}
	public static void main(String[] args) {
		launch(args);
	}
}
