package com.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import sun.rmi.runtime.Log;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int manState =0;
	int pause = 0;
	float gravity = 0.4f;
	float velocity = 0f;
	int manY = 0;
	boolean goUp;
	Rectangle manRectangle;
	Texture dizzy;

	ArrayList<Integer> coinXs = new ArrayList<Integer>();
	ArrayList<Integer> coinYs = new ArrayList<Integer>();
	ArrayList<Integer> bombXs = new ArrayList<Integer>();
	ArrayList<Integer> bombYs = new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangles = new ArrayList<Rectangle>();
	ArrayList<Rectangle> bombRectangles = new ArrayList<Rectangle>();
	Texture coin;
	Texture bomb;
	int coinCount;
	int bombCount;
	Random random = new Random();
	int score=0;
	BitmapFont font;
	int gameState = 0;

	//play again button
	Stage stage;
	TextButton button;
	TextButton.TextButtonStyle textButtonStyle;
	BitmapFont buttonfont;
	Skin skin;
	TextureAtlas buttonAtlas;

	@Override
	public void create () {
		//button
		/*stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		font = new BitmapFont();
		skin = new Skin();
		buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.pack"));
		skin.addRegions(buttonAtlas);
		textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.up = skin.getDrawable("up-button");
		textButtonStyle.down = skin.getDrawable("down-button");
		textButtonStyle.checked = skin.getDrawable("checked-button");
		button = new TextButton("Button1", textButtonStyle);
		stage.addActor(button);*/
		//button
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] =new Texture("frame-1.png");
		man[1] =new Texture("frame-2.png");
		man[2] =new Texture("frame-3.png");
		man[3] =new Texture("frame-4.png");
		manY = Gdx.graphics.getHeight()/2;
		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		manRectangle = new Rectangle();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		dizzy = new Texture("dizzy-1.png");
        //playagain.setBounds(100,100,100,100);


	}

	public void makeCoin(){
		float height = random.nextFloat() *(Gdx.graphics.getHeight()- coin.getHeight());
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());
	}
	public void makeBomb(){
		float height = random.nextFloat() *(Gdx.graphics.getHeight()-bomb.getHeight());
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());

	}
	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(gameState ==1){
			if(coinCount<100) {
				coinCount++;
			}
			else{
				coinCount=0;
				makeCoin();
			}
			if(bombCount<350) {
				bombCount++;
			}
			else{
				bombCount=0;
				makeBomb();
			}
			coinRectangles.clear();
			for(int i=0;i<coinXs.size();i++){
				batch.draw(coin,coinXs.get(i),coinYs.get(i));
				coinXs.set(i,coinXs.get(i)-4);
				coinRectangles.add(new Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));
			}
			bombRectangles.clear();
			for(int i=0;i<bombXs.size();i++){
				batch.draw(bomb,bombXs.get(i),bombYs.get(i));
				bombXs.set(i,bombXs.get(i)-4);
				bombRectangles.add(new Rectangle(bombXs.get(i),bombYs.get(i),bomb.getWidth(),bomb.getHeight()));
			}
			if(Gdx.input.justTouched() && manY < Gdx.graphics.getHeight()-man[manState].getHeight()){
				velocity = -10;
			}
			if(pause<5) {
				pause++;
			}else {
				pause =0;
				if (manState < 3) {
					manState++;
				} else {
					manState = 0;
				}
			}
			velocity += gravity;
			manY -= velocity;
			if(manY<=0) {
				manY = 0;
			}
		}else if(gameState ==0){
			if(Gdx.input.justTouched()){
				gameState=1;
			}
		}else if(gameState ==2){
			//font.draw(batch,"Just tap to play again!",300,300);
			stage.draw();
//button.addListener(new ChangeListener() {
	//@Override
	//public void changed(ChangeEvent event, Actor actor) {
		gameState=1;
		manY = Gdx.graphics.getHeight()/2;
		score=0;
		velocity =0;
		coinXs.clear();
		coinYs.clear();
		coinRectangles.clear();
		coinCount =0;
		bombXs.clear();
		bombYs.clear();
		bombRectangles.clear();
		bombCount =0;
		}

		if(gameState ==2){
			batch.draw(dizzy,Gdx.graphics.getWidth()/2 - man[manState].getWidth()/2,manY);
		}else{
			batch.draw(man[manState],Gdx.graphics.getWidth()/2 - man[manState].getWidth()/2,manY);
		}

		manRectangle = new Rectangle(Gdx.graphics.getWidth()/2 - man[manState].getWidth()/2,manY,man[manState].getWidth(),man[manState].getHeight());
		for(int i=0;i<coinRectangles.size();i++){
			if(Intersector.overlaps(manRectangle,coinRectangles.get(i))){
				Gdx.app.log("coin","collision");
				score++;
				coinRectangles.remove(i);
				coinXs.remove(i);
				coinYs.remove(i);
				break;
			}
		}
		for(int i=0;i<bombRectangles.size();i++){
			if(Intersector.overlaps(manRectangle,bombRectangles.get(i))){
				Gdx.app.log("bomb","collision");
				gameState=2;
			}
		}
		font.draw(batch,String.valueOf(score),100,200);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
