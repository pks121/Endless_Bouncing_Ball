package com.pks.bouncingball;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.Button;
import java.awt.Font;
import java.util.Random;

//import javax.xml.soap.Text;

import static java.awt.Color.RED;

public class GameView extends ApplicationAdapter {
	SpriteBatch batch;
	Texture bgt;
	int GameState = 3;
	Texture[] ball;
	Texture crashedball, increaseballvelocity;
	float gravity = 1.8f;
	float velocity = 0;
	float increasedvelocity = 0;
	int change = 0;
	float ballY = 0f;
	float ballX;
	int temp;
	float gap = 0f;
	long time = 80;
	Random random;
	Texture TopPipe;
	Texture BottomPipe;
	Float pipeX = 0f;
	int count = 0;
	int i = 0;
	private BitmapFont font;
	Texture replay;
	Texture start;
	int BestScore;
	//int found = 5;
	float increaseballvelocityX, increaseballvelocityY;
	//int timeb=400;
	int scored;
	Preferences GameScore;


	@Override
	public void create() {
		batch = new SpriteBatch();
		bgt = new Texture("bg1.png");
		ball = new Texture[2];
		ball[0] = new Texture("ball1.png");
		ball[1] = new Texture("ball2.png");
		TopPipe = new Texture("TopPipe.png");
		ballY = Gdx.graphics.getHeight() / 2f - ball[0].getHeight() / 2f;
		ballX = Gdx.graphics.getWidth() / 2f - ball[change].getWidth() / 2f;
		random = new Random();
		crashedball = new Texture("crashedball.png");
		increaseballvelocity = new Texture("increaseballvelocity.png");

		BottomPipe = new Texture("BottomPipe.png");
		font = new BitmapFont();
		font.setColor(Color.RED);
		font.getData().setScale(4);

		replay = new Texture("replay.png");

		start = new Texture("Start.png");
		GameScore = Gdx.app.getPreferences("bouncingball");


	}

	@Override
	public void render() {

		batch.begin();
		batch.draw(bgt, 0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.draw(BottomPipe, pipeX, -BottomPipe.getHeight() / 2f + gap);
		batch.draw(TopPipe, pipeX, BottomPipe.getHeight() / 2f+ BottomPipe.getWidth()*2 + gap);
		font.draw(batch, "Score:\n" + String.valueOf(count), Gdx.graphics.getWidth() - 300f, Gdx.graphics.getHeight() - 100f);
		if (count >= GameScore.getInteger("BestScore",0)) {
			GameScore.flush();
			BestScore = count;

		}
		GameScore.putInteger("BestScore", BestScore);
		scored = GameScore.getInteger("BestScore",BestScore);
		font.draw(batch, "BestScore\n" + String.valueOf(scored), 50f, Gdx.graphics.getHeight() - 100f);


//GamePlay
		if (GameState == 1) {
			ball[0].load(ball[0].getTextureData());
			batch.draw(ball[change], ballX, ballY);
			if (pipeX < -BottomPipe.getWidth() - 100) {
				gap = random.nextInt(BottomPipe.getWidth()/2) + BottomPipe.getWidth() / 3;
				if (i == 0) {
					gap -= 150;
					i = 1;
				} else {
					gap += 150;
					i = 0;
				}

				pipeX = Gdx.graphics.getWidth() + 2f;

			}
			//for speed of pipe when ball touched with super ball.
			/*if (count >= 1) {
				if(count%2==0) {
					timeb--;
					increaseballvelocityX = pipeX + BottomPipe.getWidth();
					increaseballvelocityY = Gdx.graphics.getHeight() / 2 + gap - 50;
					if ((Math.abs(ballX) + 10 >= increaseballvelocityX || Math.abs(ballX) - 10 <= increaseballvelocityX) && (Math.abs(ballY) + 10 >= increaseballvelocityY || Math.abs(ballY) - 10 <= increaseballvelocityY) && timeb >= 0) {
						increaseballvelocityX -= 4;
						temp = count;
						batch.draw(increaseballvelocity, increaseballvelocityX, increaseballvelocityY);
						increasedvelocity += .0001;
					}
				} else{
					timeb=400;
					increasedvelocity-=.0001;
				}

			}*/


				for (int j = 0; j <= 4; j++) {

					pipeX -= 2 + increasedvelocity;

				}

				if (change == 0) {
					change = 1;
				} else {
					change = 0;
				}

				if (Gdx.input.justTouched()) {
					velocity = -30;
				}
				ballY -= velocity;
				velocity = velocity + gravity;


				if (ballY < 0 || ((ballX + ball[0].getWidth() >= pipeX) && (ballX <= (pipeX + BottomPipe.getWidth()))) &&
						(((ballY <= (BottomPipe.getHeight() - BottomPipe.getHeight() / 2f + gap)) ||
								ballY + ball[0].getHeight() >= BottomPipe.getHeight() / 2f+ BottomPipe.getWidth()*2 + gap))) {

					GameState = 2;
				} else if (Math.floor(ballX) == Math.floor(pipeX) &&
						ballY > (BottomPipe.getHeight() - BottomPipe.getHeight() / 2f + gap)) {
					count++;

				}
			}
			// for replay
			if (GameState == 2) {
				ball[0].dispose();
				batch.draw(crashedball, ballX, ballY);
				increasedvelocity=0;
				ballX = Gdx.graphics.getWidth() / 2f - ball[change].getWidth() / 2f;
				batch.draw(replay, ballX, Gdx.graphics.getHeight() / 2f - ball[0].getHeight() / 2f);

				if (time != 0)
					time--;
				if (time == 0)
					font.draw(batch, "Touch to Play ", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
				else {
					font.draw(batch, "Starting in " + String.valueOf(time), Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
				}


				if (Gdx.input.isTouched()) {
					if (time <= 0) {
						ballY = Gdx.graphics.getHeight() / 2f - ball[0].getHeight() / 2f;
						ballX = Gdx.graphics.getWidth() / 2f - ball[change].getWidth() / 2f;
						pipeX = Gdx.graphics.getWidth() + 2f;
						batch.draw(ball[change], ballX, ballY);
						count = 0;
						gap = 0;
						batch.draw(BottomPipe, pipeX, -BottomPipe.getHeight() / 2f + gap);
						batch.draw(TopPipe, pipeX, ballX = Gdx.graphics.getWidth() / 2f - ball[change].getWidth() / 2f);
						time = 80;
						GameState = 1;

					}
				}
			}
			if (GameState == 3) {
				batch.draw(start, ballX, Gdx.graphics.getHeight() / 2f - ball[0].getHeight() / 2f);
				if (Gdx.input.isTouched()) {

					GameState = 1;

				}
			}

			batch.end();


		}

	}
