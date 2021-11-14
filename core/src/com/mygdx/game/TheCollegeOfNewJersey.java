package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.AI.Graph;
import com.mygdx.game.AI.GraphMaker;
import com.mygdx.game.AI.Node;
import com.mygdx.game.AI.NodeConnections;

import java.util.ArrayList;

public class TheCollegeOfNewJersey extends ApplicationAdapter {

	protected static ArrayList<Vector2> circles = new ArrayList<>();
	protected static ArrayList<Array<Vector2>> lines = new ArrayList<>();
	protected static Graph graph = new Graph();

	private Vector3 mouseCordinates =  new Vector3();
	protected static CreateFile filemanager = new CreateFile(graph);

	private GraphPath<Node> finalPath;

	private Skin uiSkin;

	private TextButton textButton, generate;


	SpriteBatch batch;
	Texture img;
	private OrthographicCamera camera;
	public static ExtendViewport viewport;

	float cameraWidth, cameraHeight, worldUnits;

	// Input file

	OurInputProcessor inputProcessor = new OurInputProcessor(circles, lines, graph, finalPath);
	InputMultiplexer multiplexer = new InputMultiplexer();


	Sprite sprite = new Sprite();

	static {

		float cameraWidth = 800;
		float cameraHeight = 800;
		float worldUnits = 1;

	}

	//ButtonGroup buttonGroup = new ButtonGroup();

	Stage stage;


	Table table;

	Group group;

	CheckBox checkBox1, checkBox2;
	
	@Override
	public void create () {

		table = new Table();

		uiSkin = new Skin(Gdx.files.internal("UI\\UISet.json"));

		GraphMaker graphMaker = new GraphMaker(circles, lines);

		batch = new SpriteBatch();
		sprite = new Sprite(new Texture(Gdx.files.internal("tcnj.png")));
		sprite.setPosition(0,0);

		//img = new Texture("map.png");

		cameraWidth = sprite.getWidth();
		cameraHeight = sprite.getHeight();

		camera = new OrthographicCamera(cameraWidth, cameraHeight);
		camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);

		viewport = new ExtendViewport(camera.viewportWidth, camera.viewportHeight, camera);
		stage = new Stage(viewport, batch);

		textButton = new TextButton("Bruh", uiSkin, "default");
		generate = new TextButton("Generate", uiSkin, "default");

		CheckBox checkBox1 = new CheckBox("A* Algorithim", uiSkin, "default");
		CheckBox checkBox2 = new CheckBox("Dykstras", uiSkin, "default");

		table.setTransform(true);

		textButton.getLabel().setFontScale(4f);


		generate.getLabel().setFontScale(4f);

		generate.setTransform(true);
table.setPosition(0, Gdx.graphics.getHeight());

//table.scaleBy(5f);

table.setWidth(stage.getWidth());
table.align(Align.center|Align.right);



		table.add(textButton).width(500).height(100).padBottom(40f);
		table.row();
		table.add(generate).width(500).height(100).padBottom(400f);
		table.padTop(900f);
		table.padRight(70f);
		table.row();
		table.add(checkBox1);
		table.row();
		table.add(checkBox2);


		stage.addActor(table);

		//camera.translate(0,0);

		viewport.apply();

		multiplexer.addProcessor(inputProcessor);
		multiplexer.addProcessor(stage);

		Gdx.input.setInputProcessor(stage);





	}

	private void updateMouse() {

		mouseCordinates.x = Gdx.input.getX();
		mouseCordinates.y = Gdx.input.getY();
		viewport.getCamera().unproject(mouseCordinates);


	}


	@Override
	public void render () {




		updateMouse();
		camera.update();

		//camera.zoom += inputProcessor.zoomValue();

		viewport.getCamera().unproject(mouseCordinates);

		ScreenUtils.clear(0, 0, 0, 1);

		batch.setProjectionMatrix(viewport.getCamera().combined);

		batch.begin();
		//batch.draw(img, 0, 0);


		sprite.draw(batch);



		batch.end();

		stage.draw();


		for (ObjectMap.Entry<Node, Array<Connection<Node>>> nodes : graph.getCollege().entries())


			DebugDrawer.DrawDebugCircle(nodes.key.getPostion(), 30, 7, Color.PURPLE, TheCollegeOfNewJersey.viewport.getCamera().combined);



		for (ObjectMap.Entry<Node, Array<Connection<Node>>> nodes : graph.getCollege().entries()) {



			for (Connection<Node> connection : nodes.value) {

				
				DebugDrawer.DrawDebugLine(connection.getToNode().getPostion() , connection.getFromNode().getPostion(), 7, Color.PURPLE, TheCollegeOfNewJersey.viewport.getCamera().combined);

			}



		}

		if (graph.getFinalPath() != null) {

			for (Node node : graph.getFinalPath()) {

				DebugDrawer.DrawDebugCircle(node.getPostion(), 30, 7, Color.RED, TheCollegeOfNewJersey.viewport.getCamera().combined);

			}

		}

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	@Override
	public void resize(int width, int height){

		viewport.update(width,height);
		camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
	}
}
