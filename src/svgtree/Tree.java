package svgtree;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.lang.Math;

import svgtree.Circle;
import svgtree.Text;
import svgtree.Svg;
import svgtree.Line;
import svgtree.TagTree;

public class Tree {

  private int height;
  private float radio;
  private float diameter;
  private float half;

  private Document document;
  private Element svg;
  private TagTree tagTree;

  public Tree(int height, float radio) throws ParserConfigurationException {
    this.height = height;
    this.radio = radio;
    this.diameter = 2 * radio;
    this.half = radio / 2;
    // Tags and elements.
    this.document = DocumentBuilderFactory
      .newInstance()
      .newDocumentBuilder()
      .newDocument();
    this.svg = (new Svg(document)).create();
    this.document.appendChild(svg);
    this.tagTree = new TagTree(document);
    this.svg.appendChild(tagTree.getElement());
    // Posible efecto secundario en PseudoTag.
    // Investigar: Cambiar document entre elementos distintos.
    Circle.setDocument(document);
    Circle.setRadio(radio);
    Text.setDocument(document);
    Text.setFontSize(radio + half);
    Line.setDocument(document);
    Line.setStrokeWidth(half / 2);
  }

  public Tree(int height) throws ParserConfigurationException {
    this(height, 0.5f);
  }

  public void drawTree() throws ParserConfigurationException {
    float x = (float) Math.pow(2, this.height + 1) * this.radio;
    float y = this.diameter;
    Point point = new Point(x, y);
    draw(point, this.height);
    // Establece los límites de vista del svg.
    // Cálcula el punto inferior derecho y hace un corrimiento
    // a la derecha.
    float power = (float) Math.pow(2, this.height);
    float nextPower = 2 * power;
    float xshift = (power - 1) * diameter;
    float distance = (nextPower - 2) * diameter;
    Point dim = point
      .bottomRight(xshift, distance)
      .translate(diameter, diameter);
    String viewBox = "0 0 " + dim.getX() + " " + dim.getY();
    this.svg.setAttribute("viewBox", viewBox);
  }

  private void draw(Point point, int height) {
    Point textPoint = point.translateInY(this.half);
    Circle circle = new Circle(point);
    this.tagTree.appendChild(circle);
    Text text = new Text(textPoint, Integer.toString(height));
    this.tagTree.appendChild(text);
    if (height == 0) return;
    Point center, start, end;
    float xshift = (float) Math.pow(2, height) * this.radio;
    float distance = (float) Math.pow(2, height) * this.diameter;
    // left node
    center = point.bottomLeft(xshift, distance);
    start = point.bottomLeft(this.half, this.radio);
    end = center.bottomLeft(this.half, this.radio);
    Line line1 = new Line(start, end);
    this.tagTree.appendChild(line1);
    draw(center, height - 1);
    // right node
    center = point.bottomRight(xshift, distance);
    start = point.bottomRight(this.half, this.radio);
    end = center.bottomRight(this.half, this.radio);
    Line line2 = new Line(start, end);
    this.tagTree.appendChild(line2);
    draw(center, height - 1);
  }

  public void save(String filename) throws TransformerException {
    DOMSource source = new DOMSource(document);
    Transformer transformer = TransformerFactory
      .newInstance()
      .newTransformer();
    StreamResult result = new StreamResult(filename);
    transformer.transform(source, result);
  }

} 
