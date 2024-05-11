package com.aiur.jt.d3;

import java.awt.*;

// 三角形
public class Triangle {
  Vertex v1;
  Vertex v2;
  Vertex v3;
  Color color;

  Triangle(Vertex v1, Vertex v2, Vertex v3, Color color) {
    this.v1 = v1;
    this.v2 = v2;
    this.v3 = v3;
    this.color = color;
  }
}
