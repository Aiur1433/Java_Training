package com.aiur.jt.d3;

import java.io.File;
import java.io.FileInputStream;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Marquee3D extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    // 记录所有顶点的坐标
    float[] points = {
      0, -50, 0, // v0
      -50, 50, -50, // v1
      50, 50, -50, // v2
      50, 50, 50, // v3
      -50, 50, 50 // v4
    };

    // 记录纹理坐标
    float[] texCoords = {
      0.5f, 0f, // t0 (it0 = 0)
      0f, 1f, // t1 (it1 = 1)
      1f, 1f, // t2 (it2 = 2)
    };

    // 记录三角面
    int[] faces = {
      0, 0, 1, 1, 2, 2, // f0 front-face
      0, 0, 2, 1, 3, 2, // f0 back-face
      0, 0, 3, 1, 4, 2, // f1 front-face
      0, 0, 4, 1, 1, 2 // f1 back-face
    };

    TriangleMesh mesh = new TriangleMesh();
    mesh.getPoints().addAll(points);
    mesh.getTexCoords().addAll(texCoords);
    mesh.getFaces().addAll(faces);

    MeshView meshView = new MeshView();
    //    meshView.setDrawMode(DrawMode.LINE);
    meshView.setMesh(mesh);
    meshView.setTranslateX(256);
    meshView.setTranslateY(256);
    meshView.setTranslateZ(0);
    meshView.setScaleX(1);
    meshView.setScaleY(1);
    meshView.setScaleZ(1);

    // Creating a Group object
    Group group = new Group();
    group.getChildren().addAll(meshView);

    Scene scene = new Scene(group, 512, 512);

    ParallelCamera camera = new ParallelCamera();
    scene.setCamera(camera);

    stage.setScene(scene);

    // Preparing the phong material of type bump map
    PhongMaterial material1 = new PhongMaterial();

    material1.setDiffuseMap(
        new Image(
            new FileInputStream(
                new File(getClass().getResource("/img/naonaoiswatching.jpg").toURI()))));
    meshView.setMaterial(material1);

    RotateTransition rotateTransition = new RotateTransition();
    // Setting the duration for the transition
    rotateTransition.setDuration(Duration.millis(5000));
    // Setting the node for the transition
    rotateTransition.setNode(meshView);
    // Setting the angle of the rotation
    rotateTransition.setByAngle(360);
    // Setting the cycle count for the transition
    rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
    // Setting auto reverse value to false
    rotateTransition.setAutoReverse(false);
    // 绕Y轴旋转
    rotateTransition.setAxis(Rotate.Y_AXIS);
    // 匀速运动
    rotateTransition.setInterpolator(Interpolator.LINEAR);
    // Playing the animation
    rotateTransition.play();

    stage.initStyle(StageStyle.DECORATED);
    stage.setWidth(512);
    stage.setHeight(512);
    stage.setResizable(false);
    stage.show();
  }
}
