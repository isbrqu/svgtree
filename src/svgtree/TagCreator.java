package svgtree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.DOMException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import svgtree.Circle;
import svgtree.Text;
import svgtree.Line;
import svgtree.Svg;

public class TagCreator {

  private static final String SVG = "http://www.w3.org/2000/svg";

  private Document document;
  private String fontSize;
  private String strokeWidth;
  private Element tagTree;
  private Element tagSvg;
  private Circle circle;
  private Text text;
  private Line line;

  public
  TagCreator()
  throws ParserConfigurationException {
    this.document = DocumentBuilderFactory
      .newInstance()
      .newDocumentBuilder()
      .newDocument();
  }

  public
  TagCreator(float radio)
  throws ParserConfigurationException {
    this();
    this.circle = new Circle(this.document, radio);
    float fontSize = radio + radio / 2;
    this.text = new Text(this.document, fontSize);
    float strokeWidth = radio / 4;
    this.line = new Line(this.document, strokeWidth);
    this.tagSvg = (new Svg(this.document)).create();
    this.tagTree = this.document.createElement("g");
    this.tagSvg.appendChild(this.tagTree);
    this.document.appendChild(this.tagSvg);
  }

  public Document
  getDocument() {
    return this.document;
  }

  public Element
  getTagSVG() {
    return this.tagSvg;
  }

  public Element
  getTagTree() {
    return this.tagTree;
  }

  public Element
  createElementNS(String type) {
    Element element = null;
    try {
      element = this.document.createElementNS(SVG, type);
    } catch (DOMException e) {
      e.printStackTrace();
    }
    return element;
  }

  public Element
  createElement(String type) {
    Element element = null;
    try {
      element = this.document.createElement(type);
    } catch (DOMException e) {
      e.printStackTrace();
    }
    return element;
  }

  public Element
  createCircle(float cx, float cy, float r) {
    return this.circle.create(cx, cy, r);
  }

  public Element
  createText(float x, float y, int num) {
    return this.text.create(x, y, Integer.toString(num));
  }

  public Element
  createLine(float x1, float y1, float x2, float y2) {
    return this.line.create(x1, y1, x2, y2);
  }

}
