import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;

def xmlFile = new File(args[0]);
def xsltFile = args[1];

def docBuildFactory = DocumentBuilderFactory.newInstance();      
def parser = docBuildFactory.newDocumentBuilder();     
def document = parser.parse(xmlFile);     

def xformFactory = TransformerFactory.newInstance();      
def transformer = xformFactory.newTransformer(new StreamSource(xsltFile));      
def source = new DOMSource(document);      
def scrResult = new StreamResult(System.out);      

transformer.transform(source, scrResult);
