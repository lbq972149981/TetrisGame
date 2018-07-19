package com.colin.game.core;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 主页面父类
 * 
 * @author flyfox
 * @date 2014年11月4日
 */
public abstract class FFApplication extends Application {
	private Group mGroup;
	private Scene mScene;

	@Override
	public void start(Stage primaryStage) throws Exception {
		before();
		mGroup = new Group();
		mScene = new Scene(mGroup, FFContants.WIDTH, FFContants.HEIGHT);
		after();
		showStage(primaryStage);
	}

	protected void before() {

	}

	protected void after() {

	}

	protected void showStage(Stage stage) {
		stage.setScene(mScene);
		stage.show();
	}

	protected Scene getScene() {
		return mScene;
	}

	protected Group getRoot() {
		return mGroup;
	}

	public void setWindowSize(int width, int height) {
		FFContants.init(width, height);
	}

}
